package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.DefaultedEntityGeoModel;

public class SnufflesModel extends DefaultedEntityGeoModel<SnufflesEntity> {
    public SnufflesModel() {
        super(Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles"));
    }
}
