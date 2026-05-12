package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.LongTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.LongTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LongTalismanItemRender extends GeoItemRenderer<LongTalismanItem> {

    public LongTalismanItemRender() {
        super(new LongTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/long_talisman_glow_add.png"));
    }
}
