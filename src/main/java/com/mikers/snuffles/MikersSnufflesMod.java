package com.mikers.snuffles;

import com.mikers.snuffles.registry.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(MikersSnufflesMod.MOD_ID)
public class MikersSnufflesMod {
    public static final String MOD_ID = "mikers_snuffles";

    public MikersSnufflesMod(IEventBus modEventBus) {
        ModEntities.ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Здесь можно добавить инициализацию, если нужно
    }
}
