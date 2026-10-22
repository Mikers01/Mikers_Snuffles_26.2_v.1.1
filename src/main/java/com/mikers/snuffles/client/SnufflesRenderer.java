package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;

public class SnufflesRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<SnufflesEntity, R> {

    public SnufflesRenderer(EntityRendererProvider.Context context) {
        super(context, new SnufflesModel());
    }

    @Override
    public Identifier getTextureLocation(R renderState) {
        // В GeckoLib 5 RenderState для живых сущностей содержит ссылку на сущность?
        // Нет, но мы можем использовать DataTickets.ENTITY_ID или просто вернуть дефолт.
        // Временное решение: возвращаем 1-ю текстуру, чтобы проверить, что рендер работает.
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "textures/entity/mikers_snuffles1.png");
    }
}
