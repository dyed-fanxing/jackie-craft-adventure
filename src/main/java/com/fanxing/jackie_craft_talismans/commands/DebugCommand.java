package com.fanxing.jackie_craft_talismans.commands;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * @author FanXing
 * @since 2026-01-08 22:13
 */
@EventBusSubscriber(modid = JackieCraftTalismans.MOD_ID)
public class DebugCommand {

    @SubscribeEvent
    public static void onRegisterCommandsG(RegisterCommandsEvent event) {

    }
}
