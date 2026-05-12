package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.DogTalismanItemModel;
import com.fanxing.jackie_craft_adventure.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_adventure.item.DogTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DogTalismanItemRender extends GeoItemRenderer<DogTalismanItem> {
    public DogTalismanItemRender() {
        super(new DogTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/dog_talisman_glow_add.png"));
    }
}
