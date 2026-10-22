package com.mikers.snuffles.entity.ai;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SmartDigGoal extends Goal {
    private final SnufflesEntity mob;
    private int breakProgress;

    public SmartDigGoal(SnufflesEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getState().equals("sleep")) return false;
        
        // Если есть цель и путь заблокирован, или если день и нужно спрятаться
        if (this.mob.getTarget() != null) {
            return this.mob.getNavigation().isDone() && isBlockedAhead();
        } else {
            // Если нет цели, но день и мы на поверхности - копаем вниз
            long time = this.mob.level().getGameTime() % 24000;
            return time < 12000 && this.mob.level().canSeeSky(this.mob.blockPosition());
        }
    }

    @Override
    public void start() {
        this.breakProgress = 0;
    }

    @Override
    public void tick() {
        Level level = this.mob.level();
        BlockPos targetPos = null;

        if (this.mob.getTarget() != null) {
            targetPos = getBlockAhead();
        } else {
            targetPos = this.mob.blockPosition().below(); // Копаем вниз
        }

        if (targetPos != null) {
            BlockState state = level.getBlockState(targetPos);
            if (isValidBlock(state)) {
                this.breakProgress++;
                if (this.breakProgress >= 10) {
                    level.destroyBlock(targetPos, true);
                    this.breakProgress = 0;
                }
            }
        }
    }

    private boolean isBlockedAhead() {
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        return isValidBlock(this.mob.level().getBlockState(front)) || isValidBlock(this.mob.level().getBlockState(front.above()));
    }

    private BlockPos getBlockAhead() {
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        // Копаем 2 блока в высоту
        if (isValidBlock(this.mob.level().getBlockState(front.above()))) {
            return front.above();
        }
        return front;
    }

    private boolean isValidBlock(BlockState state) {
        if (state.isAir()) return false;
        return state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.DIRT) ||
               state.is(BlockTags.LOGS) || state.is(BlockTags.SAND) ||
               state.is(Blocks.GRAVEL) || state.is(Blocks.CLAY) ||
               state.is(Blocks.COAL_ORE) || state.is(Blocks.IRON_ORE) || state.is(Blocks.DIAMOND_ORE);
    }
}
