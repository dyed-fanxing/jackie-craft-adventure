package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.HorseTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.HorseTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HorseTalismanItemRender extends GeoItemRenderer<HorseTalismanItem> {
    public HorseTalismanItemRender() {
        super(new HorseTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/horse_talisman_glow_add.png"));
    }
}
