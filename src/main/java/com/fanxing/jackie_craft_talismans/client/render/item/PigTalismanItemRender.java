package com.fanxing.jackie_craft_talismans.client.render.item;

import com.fanxing.jackie_craft_talismans.client.model.item.PigTalismanItemModel;
import com.fanxing.jackie_craft_talismans.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PigTalismanItemRender extends GeoItemRenderer<PigTalismanItem> {
    public PigTalismanItemRender() {
        super(new PigTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/pig_talisman_glow_add.png"));
    }
}
