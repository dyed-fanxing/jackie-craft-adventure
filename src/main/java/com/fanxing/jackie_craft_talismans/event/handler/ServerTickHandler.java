package com.fanxing.jackie_craft_talismans.event.handler;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * @author Sakpeipei
 * @since 2026/1/16 15:13
 */
@EventBusSubscriber(modid = JackieCraftTalismans.MOD_ID)
public class ServerTickHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
    }
}
