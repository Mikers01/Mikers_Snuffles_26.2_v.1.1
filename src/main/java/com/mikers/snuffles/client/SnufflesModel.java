package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;

public class SnufflesModel extends GeoModel<SnufflesEntity> {

    // GeckoLib 5 сам добавит префикс geckolib/models/ и суффикс .geo.json
    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    // GeckoLib 5 сам добавит префикс geckolib/animations/ и суффикс .animation.json
    @Override
    public Identifier getAnimationResource(SnufflesEntity animatable) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    // Для текстур пути остаются как в ваниле, с полным именем файла
    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "textures/entity/mikers_snuffles1.png");
    }
}
