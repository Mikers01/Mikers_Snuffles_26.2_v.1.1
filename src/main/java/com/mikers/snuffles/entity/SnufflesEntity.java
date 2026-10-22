package com.mikers.snuffles.entity;

import com.mikers.snuffles.entity.ai.*;
import com.mikers.snuffles.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SnufflesEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT = 
        SynchedEntityData.defineId(SnufflesEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> STATE = 
        SynchedEntityData.defineId(SnufflesEntity.class, EntityDataSerializers.STRING);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SnufflesEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 24.0D)
            .add(Attributes.ATTACK_DAMAGE, 13.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.3F)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(STATE, "panic");
    }

    public int getVariant() { return this.entityData.get(VARIANT); }
    public void setVariant(int variant) { this.entityData.set(VARIANT, variant); }
    
    public String getState() { return this.entityData.get(STATE); }
    public void setState(String state) { this.entityData.set(STATE, state); }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariant());
        tag.putString("State", getState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) setVariant(tag.getInt("Variant"));
        if (tag.contains("State")) setState(tag.getString("State"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new BreakBlocksGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (this.level().isDay() && this.getState().equals("panic")) {
                // Упрощенная логика: днем пытаемся найти укрытие или спим
                if (!this.level().canSeeSky(this.blockPosition())) {
                    this.setState("sleep");
                }
            } else if (this.level().isNight() && this.getState().equals("sleep")) {
                this.setState("panic");
            }

            if (this.getState().equals("panic") && this.getTarget() != null) {
                this.setState("attack");
            } else if (this.getState().equals("attack") && this.getTarget() == null) {
                this.setState("panic");
            }
        }
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && this.getState().equals("sleep")) {
            this.setState("panic");
        }
        return hurt;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, state -> {
            String currentState = this.getState();
            if (currentState.equals("sleep")) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("sleeping"));
            } else if (currentState.equals("attack")) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("sprinting"));
            } else if (currentState.equals("panic")) {
                if (this.isMoving()) {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("running"));
                }
                return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }).triggerableAnim("attack", RawAnimation.begin().thenPlay("attack")));
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 0.001D;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
