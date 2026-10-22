package com.mikers.snuffles.client;

import com.mikers.snuffles.entity.SnufflesEntity;
import net.minecraft.resources.Identifier;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.constant.DataTicket; // Правильный импорт

public class SnufflesModel extends GeoModel<SnufflesEntity> {
    
    // Создаем DataTicket для хранения варианта текстуры
    public static final DataTicket<Integer> VARIANT_TICKET = DataTicket.create("variant", Integer.class);

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    @Override
    public Identifier getAnimationResource(SnufflesEntity animatable) {
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "mikers_snuffles");
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        // Достаем вариант из RenderState (по умолчанию 1)
        int variant = renderState.getOrDefaultGeckolibData(VARIANT_TICKET, 1);
        // Формируем путь: textures/entity/mikers_snuffles1.png и т.д.
        return Identifier.fromNamespaceAndPath("mikers_snuffles", "textures/entity/mikers_snuffles" + variant + ".png");
    }
}
