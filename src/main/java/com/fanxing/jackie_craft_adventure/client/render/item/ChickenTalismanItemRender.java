package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.ChickenTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.ChickenTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ChickenTalismanItemRender extends GeoItemRenderer<ChickenTalismanItem> {
    public ChickenTalismanItemRender() {
        super(new ChickenTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/chicken_talisman_glow_add.png"));
    }
}
