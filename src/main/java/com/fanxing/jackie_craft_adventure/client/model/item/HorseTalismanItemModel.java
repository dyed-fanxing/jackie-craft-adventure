package com.fanxing.jackie_craft_adventure.client.model.item;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.item.HorseTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class HorseTalismanItemModel extends DefaultedItemGeoModel<HorseTalismanItem> {
    public HorseTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "horse_talisman"));
    }
}
