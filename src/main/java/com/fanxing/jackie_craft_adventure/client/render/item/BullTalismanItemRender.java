package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.BullTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.BullTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BullTalismanItemRender extends GeoItemRenderer<BullTalismanItem> {
    public BullTalismanItemRender() {
        super(new BullTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/bull_talisman_glow_add.png"));
    }
}
