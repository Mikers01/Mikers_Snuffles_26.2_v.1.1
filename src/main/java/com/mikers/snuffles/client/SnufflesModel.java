package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.ResourceLocation;
import com.geckolib.model.DefaultedEntityGeoModel;

public class SnufflesModel extends DefaultedEntityGeoModel<SnufflesEntity> {
    public SnufflesModel() {
        super(ResourceLocation.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles"));
    }
}
