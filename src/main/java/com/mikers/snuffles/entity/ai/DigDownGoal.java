package com.mikers.snuffles.entity.ai;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class DigDownGoal extends Goal {
    private final SnufflesEntity mob;
    private int breakProgress;

    public DigDownGoal(SnufflesEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Копаем днем, если нет цели и видим небо
        if (!this.mob.getState().equals("panic") || this.mob.getTarget() != null) {
            return false;
        }
        return this.mob.level().canSeeSky(this.mob.blockPosition());
    }

    @Override
    public void start() {
        this.breakProgress = 0;
    }

    @Override
    public void tick() {
        BlockPos posBelow = this.mob.blockPosition().below();
        BlockState state = this.mob.level().getBlockState(posBelow);
        
        if (isValidBlock(state)) {
            this.breakProgress++;
            if (this.breakProgress >= 10) {
                this.mob.level().destroyBlock(posBelow, true);
                this.breakProgress = 0;
            }
        }
    }

    private boolean isValidBlock(BlockState state) {
        if (state.isAir()) return false;
        return state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.DIRT) ||
               state.is(BlockTags.SAND) || state.is(Blocks.GRAVEL);
    }
}
