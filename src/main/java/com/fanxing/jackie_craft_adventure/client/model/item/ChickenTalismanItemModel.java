package com.fanxing.jackie_craft_adventure.client.model.item;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.item.ChickenTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ChickenTalismanItemModel extends DefaultedItemGeoModel<ChickenTalismanItem> {
    public ChickenTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "chicken_talisman"));
    }
}
