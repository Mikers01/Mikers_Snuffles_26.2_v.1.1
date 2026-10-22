package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class SnufflesModel extends GeoModel<SnufflesEntity> {

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    @Override
    public Identifier getAnimationResource(SnufflesEntity animatable) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    // Возвращаем дефолтную текстуру, но рендерер её переопределит
    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "textures/entity/mikers_snuffles1.png");
    }
}
