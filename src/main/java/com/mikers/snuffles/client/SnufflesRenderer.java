package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class SnufflesRenderer<R extends EntityRenderState & GeoRenderState> extends GeoEntityRenderer<SnufflesEntity, R> {
    public SnufflesRenderer(EntityRendererProvider.Context context) {
        super(context, new SnufflesModel());
    }

    @Override
    public Identifier getTextureLocation(R renderState) {
        // В GeckoLib 5 текстура по умолчанию берется из модели.
        // Для смены вариантов нужно использовать DataTickets, но пока оставим базовую.
        return Identifier.fromNamespaceAndPath("mikers_snuffles",
                "textures/entity/mikers_snuffles1.png");
    }
}
