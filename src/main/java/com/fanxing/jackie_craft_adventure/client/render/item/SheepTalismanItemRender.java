package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.SheepTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.SheepTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SheepTalismanItemRender extends GeoItemRenderer<SheepTalismanItem> {
    public SheepTalismanItemRender() {
        super(new SheepTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/sheep_talisman_glow_add.png"));
    }
}
