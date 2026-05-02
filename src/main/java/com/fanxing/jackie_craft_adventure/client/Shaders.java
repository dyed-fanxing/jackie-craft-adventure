package com.fanxing.jackie_craft_adventure.client;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = JackieCraftAdventure.MOD_ID, value = Dist.CLIENT)
public class Shaders {
    public static ShaderInstance screenShader;

    public static ShaderInstance getScreenShader() {
        return screenShader;
    }

}