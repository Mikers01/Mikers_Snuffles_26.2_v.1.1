package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SnufflesRenderer extends GeoEntityRenderer<SnufflesEntity, GeoRenderState> {
    public SnufflesRenderer(EntityRendererProvider.Context context) {
        super(context, new SnufflesModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GeoRenderState renderState) {
        // В GeckoLib 5 текстура по умолчанию берётся из модели.
        // Для смены вариантов нужно использовать DataTickets, но пока оставим базовую.
        return ResourceLocation.fromNamespaceAndPath("mikers_snuffles",
                "textures/entity/mikers_snuffles1.png");
    }
}
