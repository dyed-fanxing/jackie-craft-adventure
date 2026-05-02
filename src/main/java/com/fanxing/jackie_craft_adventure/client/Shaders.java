package com.fanxing.jackie_craft_adventure.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = JackieCraftAdventure.MOD_ID, value = Dist.CLIENT)
public class Shaders {
    public static ShaderInstance screenShader;

    public static ShaderInstance getScreenShader() { return screenShader;}

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(
                event.getResourceProvider(),
                ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, "screen"),
                DefaultVertexFormat.NEW_ENTITY
        ), shader -> screenShader = shader);

    }

}