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
        // Получаем вариант текстуры из рендер-стейта (нужно будет прокинуть в DataTicket)
        // Для простоты используем фиксированный путь, но лучше передавать variant
        return ResourceLocation.fromNamespaceAndPath("mikers_snuffles", 
            "textures/entity/mikers_snuffles" + (renderState.getOrDefault(/* variant */ 1)) + ".png");
    }
}
