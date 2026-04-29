package com.fanxing.jackie_craft_talismans.client;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

@EventBusSubscriber(value = Dist.CLIENT)
public class KeyBindings {
    public static final String KEY_CATEGORY = "key.category."+ JackieCraftTalismans.MOD_ID;
    public static final KeyMapping TALISMAN_CONFIG = new KeyMapping(
            "key."+JackieCraftTalismans.MOD_ID+".talisman_config",
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_GRAVE,
            KEY_CATEGORY
    );
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TALISMAN_CONFIG);
    }
}