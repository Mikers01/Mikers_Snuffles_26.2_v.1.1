package com.mikers.snuffles.entity.ai;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

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
        // Проверяем блок прямо перед мобом
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        return isValidBlock(this.mob.level().getBlockState(front));
    }

    @Override
    public void start() {
        this.breakProgress = 0;
    }

    @Override
    public void tick() {
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        BlockState state = this.mob.level().getBlockState(front);
        if (isValidBlock(state)) {
            this.breakProgress++;
            if (this.breakProgress >= 10) { // 0.5 секунды
                this.mob.level().destroyBlock(front, true);
                this.breakProgress = 0;
            }
        }
    }

    private boolean isValidBlock(BlockState state) {
        if (state.isAir()) return false;
        // Разрешенные теги блоков (природные)
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
