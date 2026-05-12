package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.RabbitTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.RabbitTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RabbitTalismanItemRender extends GeoItemRenderer<RabbitTalismanItem> {
    public RabbitTalismanItemRender() {
        super(new RabbitTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/rabbit_talisman_glow_add.png"));
    }
}
