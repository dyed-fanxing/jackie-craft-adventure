package com.fanxing.jackie_craft_adventure.client.render.entity;

import com.fanxing.jackie_craft_adventure.entity.summon.LongBeamEntity;
import com.fanxing.lib.ConfigFxLib;
import com.fanxing.lib.client.render.ResourceLocations;
import com.fanxing.lib.client.render.shape.CapsuleRenderer;
import com.fanxing.lib.client.render.shape.CylinderRenderer;
import com.fanxing.lib.client.render.shape.SphereRenderer;
import com.fanxing.lib.client.render.type.BeamRenderType;
import com.fanxing.lib.client.render.type.DebugRenderType;
import com.fanxing.lib.client.render.type.LightingRenderType;
import com.fanxing.lib.client.render.type.RenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import static com.fanxing.jackie_craft_adventure.item.LongTalismanItem.DEFAULT;

public class LongBeamRenderer extends EntityRenderer<LongBeamEntity> {
    public static final float BEAM_LENGTH = 16F;   // 光束固定渲染长度

    public static final RenderType BEAM_FLOW_ROLL = BeamRenderType.ENERGY_BEAM.apply(BeamRenderType.FLOW_ROLL_BEAM_TEXTURE);

    public LongBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(@NotNull LongBeamEntity entity, float yRot, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        Vec3 velocity = entity.getDeltaMovement();
        if (velocity.lengthSqr() < 0.001) return;
        Vec3 dir = velocity.normalize();
        // entity 位置是光束尾部，光束向前延伸 BEAM_LENGTH
        Vector3f beamDir = dir.toVector3f();
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf().rotateTo(new Vector3f(0, 1, 0), beamDir));
        PoseStack.Pose pose = poseStack.last();

        int segments = ConfigFxLib.Client.SEGMENTS.getAsInt();
        float radius = entity.getBeamRadius();
        float vOffset = -(entity.tickCount * 0.1f) % 1.0f;
        // 第一遍：深度预填充（只写深度，不写颜色）
        CylinderRenderer.render(pose,bufferSource,BeamRenderType.BEAM_NO_TRANSPARENCY_TRIANGLE_STRIP_WHITE,BeamRenderType.BEAM_NO_TRANSPARENCY_TRIANGLE_WHITE,
                new Vector3f(0, 0f, 0), radius * 0.7f, BEAM_LENGTH, segments,
                DEFAULT.getFirst(), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        CylinderRenderer.render(pose,bufferSource,BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP_WHITE,BeamRenderType.ENERGY_BEAM_TRIANGLE_WHITE,
                new Vector3f(0, 0.001f, 0), radius, BEAM_LENGTH, segments,
                DEFAULT.getLast(), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT,
                1f, BEAM_LENGTH*0.5F, 0f, vOffset);

        // 渲染光束前
        poseStack.popPose();

        super.render(entity, yRot, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull LongBeamEntity entity) {
        return null;
    }
}
