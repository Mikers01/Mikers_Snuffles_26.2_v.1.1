package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SnufflesModel extends DefaultedEntityGeoModel<SnufflesEntity> {
    public SnufflesModel() {
        // GeckoLib 5 ищет модель в geckolib/models/entity/
        super(ResourceLocation.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles"));
    }
}
