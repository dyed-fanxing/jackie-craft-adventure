package com.fanxing.jackie_craft_talismans.common.render.component;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.fanxing.jackie_craft_talismans.common.RenderTypes;
import com.fanxing.jackie_craft_talismans.common.ResourceLocations;
import com.fanxing.jackie_craft_talismans.common.render.CapsuleRenderer;
import com.fanxing.jackie_craft_talismans.common.render.types.BeamRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Beam {
    public static final RenderType BEAM_NO_TRANSPARENCY = BeamRenderType.BEAM_NO_CULL.apply(ResourceLocations.WHITE_TEXTURE, false);
    public static final RenderType BEAM_NO_TRANSPARENCY_TRIANGLE_STRIP = BeamRenderType.BEAM_NO_CULL_TRIANGLE_STRIP.apply(ResourceLocations.WHITE_TEXTURE, false);


    public static final RenderType BEAM_ENERGY = BeamRenderType.ENERGY_BEAM.apply(ResourceLocations.WHITE_TEXTURE);
    public static final RenderType BEAM_ENERGY_TRIANGLE_STRIP = BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP.apply(ResourceLocations.WHITE_TEXTURE);


    public static final ResourceLocation FLOW_BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID,"textures/misc/flow_beam_glow.png");
    public static final RenderType FLOW_BEAM_NO_TRANSPARENCY = BeamRenderType.BEAM_NO_CULL.apply(FLOW_BEAM_TEXTURE, false);
    public static final RenderType FLOW_BEAM_TRANSPARENCY_TRIANGLE_STRIP = BeamRenderType.BEAM_NO_CULL_TRIANGLE_STRIP.apply(FLOW_BEAM_TEXTURE, false);
    public static final RenderType FLOW_BEAM_TRIANGLE_STRIP = BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP.apply(FLOW_BEAM_TEXTURE);
    public static final RenderType FLOW_BEAM = BeamRenderType.ENERGY_BEAM.apply(FLOW_BEAM_TEXTURE);

    public static final RenderType RAY = BeamRenderType.ENERGY_BEAM_TRIANGLE.apply(ResourceLocations.WHITE_TEXTURE);

    public float innerSize = 0.6f;

    /**
     * 渲染 激光
     */
    public void render(PoseStack poseStack, MultiBufferSource buffer,float radius,float length, float animTick,int[][] color) {
        poseStack.pushPose(); // 在这里压栈
        int segments = 32; //todo 待配置
        float offset = (animTick * 0.3f) % 1.0f;
        poseStack.pushPose();
        Beam.render(poseStack.last(),buffer,radius,innerSize,length,offset,color);
        poseStack.popPose();
    }

    /**
     * 渲染 激光
     */
    public static void render(PoseStack.Pose pose, MultiBufferSource buffer,float radius,float innerScale, float length,float offset,int[][] color) {
        int segments = 32; //todo 待配置
    }

}