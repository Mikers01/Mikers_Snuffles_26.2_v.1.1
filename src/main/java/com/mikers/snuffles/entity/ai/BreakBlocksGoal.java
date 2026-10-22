package com.mikers.snuffles.entity.ai;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
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
        if (this.mob.getState().equals("sleep") || this.mob.getTarget() == null) {
            return false;
        }
        // Ищем блоки перед собой
        return findBlockToBreak() != null;
    }

    @Override
    public void start() {
        this.breakProgress = 0;
    }

    @Override
    public void tick() {
        BlockPos targetPos = findBlockToBreak();
        if (targetPos != null) {
            BlockState state = this.mob.level().getBlockState(targetPos);
            if (isValidBlock(state)) {
                this.breakProgress++;
                if (this.breakProgress >= 10) {
                    this.mob.level().destroyBlock(targetPos, true);
                    this.breakProgress = 0;
                }
            }
        }
    }

    private BlockPos findBlockToBreak() {
        Level level = this.mob.level();
        BlockPos front = this.mob.blockPosition().relative(this.mob.getDirection());
        BlockPos frontUp = front.above(); // Блок над головой (чтобы пролезть)
        
        // Если впереди блок и над ним тоже блок (проход 1 блок высотой) - ломаем оба
        if (isValidBlock(level.getBlockState(front))) {
            if (isValidBlock(level.getBlockState(frontUp))) {
                return frontUp; // Сначала ломаем верхний
            }
            return front; // Потом нижний
        }
        return null;
    }

    private boolean isValidBlock(BlockState state) {
        if (state.isAir()) return false;
        return state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.DIRT) ||
               state.is(BlockTags.LOGS) || state.is(BlockTags.SAND) ||
               state.is(Blocks.GRAVEL) || state.is(Blocks.CLAY) ||
               state.is(Blocks.COAL_ORE) || state.is(Blocks.IRON_ORE) || state.is(Blocks.DIAMOND_ORE);
    }
}
