package com.fanxing.jackie_craft_adventure.integration.curio.register;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

import static com.fanxing.jackie_craft_adventure.client.KeyBindings.KEY_CATEGORY;

/**
 * @author dyed_fanxing
 * @date 2026/4/29 16:13
 */
public class KeyBindingsJCTItg {
    public static KeyMapping CURIO_USE_TALISMAN;

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        CURIO_USE_TALISMAN = new KeyMapping(
                "key." + JackieCraftAdventure.MOD_ID + ".curio.use.talisman",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY
        );
        event.register(CURIO_USE_TALISMAN);
    }
}
