package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.SnakeTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.SnakeTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SnakeTalismanItemRender extends GeoItemRenderer<SnakeTalismanItem> {
    public SnakeTalismanItemRender() {
        super(new SnakeTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/snake_talisman_glow_add.png"));
    }
}
