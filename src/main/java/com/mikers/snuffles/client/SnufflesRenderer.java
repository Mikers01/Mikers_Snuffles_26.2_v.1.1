package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.constant.DataTickets;

public class SnufflesRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<SnufflesEntity, R> {

    public SnufflesRenderer(EntityRendererProvider.Context context) {
        super(context, new SnufflesModel());
    }

    @Override
    public Identifier getTextureLocation(R renderState) {
        // Достаем ID сущности через встроенный DataTickets.ENTITY_ID
        int variant = 1;
        if (renderState.getGeckolibData(DataTickets.ENTITY_ID) != null) {
            // В GeckoLib 5 достать сущность из RenderState можно только так (через ID)
            var entity = renderState.getGeckolibData(DataTickets.ENTITY);
            if (entity instanceof SnufflesEntity snuffles) {
                variant = snuffles.getVariant();
            }
        }
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "textures/entity/mikers_snuffles" + variant + ".png");
    }
}
