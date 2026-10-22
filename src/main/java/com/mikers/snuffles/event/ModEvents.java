package com.mikers.snuffles.event;

import com.mikers.snuffles.MikersSnufflesMod;
import com.mikers.snuffles.entity.SnufflesEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = MikersSnufflesMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onEntityDamage(LivingIncomingDamageEvent event) {
        // Если урон получает наш Снаффлс и он спит — будим его
        if (event.getEntity() instanceof SnufflesEntity snuffles) {
            if (snuffles.getState().equals("sleep")) {
                snuffles.setState("panic");
            }
        }
    }
}
