package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SnufflesModel extends DefaultedEntityGeoModel<SnufflesEntity> {
    public SnufflesModel() {
        // Путь к модели: geo/mikers_snuffles.geo.json
        super(ResourceLocation.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles"));
    }
}
