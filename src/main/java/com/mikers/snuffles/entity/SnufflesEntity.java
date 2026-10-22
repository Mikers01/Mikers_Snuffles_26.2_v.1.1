package com.mikers.snuffles.entity;

import com.mikers.snuffles.entity.ai.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;

public class SnufflesEntity extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(SnufflesEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> STATE =
            SynchedEntityData.defineId(SnufflesEntity.class, EntityDataSerializers.STRING);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SnufflesEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
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
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Variant", getVariant());
        output.putString("State", getState());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setVariant(input.getInt("Variant").orElse(0));
        this.setState(input.getString("State").orElse("panic"));
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
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            // getGameTime() точно есть у Level, это базовый метод
            long time = this.level().getGameTime() % 24000;
            boolean isDay = time < 12000;
            boolean isNight = time >= 12000;

            if (isDay && this.getState().equals("panic")) {
                if (!this.level().canSeeSky(this.blockPosition())) {
                    this.setState("sleep");
                }
            } else if (isNight && this.getState().equals("sleep")) {
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(state -> {
            String currentState = this.getState();
            if (currentState.equals("sleep")) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("Sleeping"));
            } else if (currentState.equals("attack")) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("Sprinting"));
            } else if (currentState.equals("panic")) {
                if (this.isMoving()) {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("Running"));
                }
                return state.setAndContinue(RawAnimation.begin().thenLoop("Idle"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("Idle"));
        }).triggerableAnim("attack", RawAnimation.begin().thenPlay("Attack")));
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 0.001D;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
    public SnufflesEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        // Рандомный выбор текстуры от 1 до 4 при спавне
        this.setVariant(this.random.nextInt(4) + 1);
    }
