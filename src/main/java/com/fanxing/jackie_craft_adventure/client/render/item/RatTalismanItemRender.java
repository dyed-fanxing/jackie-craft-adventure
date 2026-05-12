package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.RatTalismanItemModel;
import com.fanxing.jackie_craft_adventure.item.RatTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RatTalismanItemRender extends GeoItemRenderer<RatTalismanItem> {
    public RatTalismanItemRender() {
        super(new RatTalismanItemModel());
        // 没有 glow_add 贴图，不加发光层
    }
}
