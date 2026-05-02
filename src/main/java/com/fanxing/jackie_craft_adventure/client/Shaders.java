package com.fanxing.jackie_craft_adventure.client;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = JackieCraftAdventure.MOD_ID, value = Dist.CLIENT)
public class Shaders {
    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
    }

}