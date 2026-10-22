package com.mikers.snuffles.entity.ai;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class BreakBlocksGoal extends Goal {
    private final SnufflesEntity mob;
    private int breakProgress;

    public BreakBlocksGoal(SnufflesEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.getState().equals("panic") && !this.mob.getState().equals("attack")) {
            return false;
        }

        // Используем поле level напрямую из Entity
        Level level = this.mob.level();
        if (level == null) return false;

        // blockPosition() и getDirection() наследуются от Entity/LivingEntity
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        return isValidBlock(level.getBlockState(front));
    }

    @Override
    public void start() {
        this.breakProgress = 0;
    }

    @Override
    public void tick() {
        Level level = this.mob.level();
        if (level == null) return;

        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        BlockState state = level.getBlockState(front);

        if (isValidBlock(state)) {
            this.breakProgress++;
            if (this.breakProgress >= 10) {
                level.destroyBlock(front, true);
                this.breakProgress = 0;
            }
        }
    }

    private boolean isValidBlock(BlockState state) {
        if (state.isAir()) return false;
        // Проверка на природные блоки через теги
        if (state.is(BlockTags.BASE_STONE_OVERWORLD) ||
                state.is(BlockTags.DIRT) ||
                state.is(BlockTags.LOGS) ||
                state.is(BlockTags.SAND) ||
                state.is(BlockTags.ORES)) {
            return true;
        }
        // Или конкретные блоки
        return state.is(Blocks.GRAVEL) || state.is(Blocks.CLAY);
    }
}
