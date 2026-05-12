package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.MonkeyTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.MonkeyTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MonkeyTalismanItemRender extends GeoItemRenderer<MonkeyTalismanItem> {
    public MonkeyTalismanItemRender() {
        super(new MonkeyTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/monkey_talisman_glow_add.png"));
    }
}
