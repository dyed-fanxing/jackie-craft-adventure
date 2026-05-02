package com.fanxing.jackie_craft_adventure.client.model.item;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.item.PigTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PigTalismanItemModel extends DefaultedItemGeoModel<PigTalismanItem> {
    public PigTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "pig_talisman"));
    }
}
