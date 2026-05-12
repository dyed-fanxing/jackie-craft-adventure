package com.fanxing.jackie_craft_adventure.client.model.item;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.item.RatTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class RatTalismanItemModel extends DefaultedItemGeoModel<RatTalismanItem> {
    public RatTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "rat_talisman"));
    }
}
