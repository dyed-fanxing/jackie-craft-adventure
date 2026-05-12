package com.fanxing.jackie_craft_adventure.client.render.item;

import com.fanxing.jackie_craft_adventure.client.model.item.TigerTalismanItemModel;
import com.fanxing.jackie_craft_adventure.item.TigerTalismanItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TigerTalismanItemRender extends GeoItemRenderer<TigerTalismanItem> {
    public TigerTalismanItemRender() {
        super(new TigerTalismanItemModel());
        // 没有 glow_add 贴图，不加发光层
    }
}
