package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class SnufflesRenderer extends GeoEntityRenderer<SnufflesEntity, GeoRenderState> {
    public SnufflesRenderer(EntityRendererProvider.Context context) {
        super(context, new SnufflesModel());
    }

    @Override
    public ResourceLocation getTextureLocation(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath("mikers_snuffles",
                "textures/entity/mikers_snuffles1.png");
    }
}
