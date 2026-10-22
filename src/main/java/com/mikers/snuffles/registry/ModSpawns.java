package com.mikers.snuffles.registry;

import com.mikers.snuffles.MikersSnufflesMod;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

@EventBusSubscriber(modid = MikersSnufflesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModSpawns {

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.SNUFFLES.get(), 
            SpawnPlacements.Type.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            (type, level, spawnType, pos, random) -> {
                // Проверка ночи и лишайника
                if (!level.isNight()) return false;
                
                // Проверка радиуса 3 блоков на наличие GLOW_LICHEN
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
                for (int x = -3; x <= 3; x++) {
                    for (int y = -3; y <= 3; y++) {
                        for (int z = -3; z <= 3; z++) {
                            mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            if (level.getBlockState(mutable).is(Blocks.GLOW_LICHEN)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            },
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
