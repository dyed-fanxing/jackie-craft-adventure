package com.fanxing.jackie_craft_talismans.client.model.item;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PigTalismanItemModel extends DefaultedItemGeoModel<PigTalismanItem> {
    public PigTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID, "pig_talisman"));
    }
}
