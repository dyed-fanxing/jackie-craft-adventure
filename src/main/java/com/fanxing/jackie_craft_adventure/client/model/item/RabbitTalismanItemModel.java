package com.fanxing.jackie_craft_adventure.client.model.item;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.item.RabbitTalismanItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class RabbitTalismanItemModel extends DefaultedItemGeoModel<RabbitTalismanItem> {
    public RabbitTalismanItemModel() {
        super(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "rabbit_talisman"));
    }
}
