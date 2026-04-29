package com.fanxing.jackie_craft_talismans.client.render.item;

import com.fanxing.jackie_craft_talismans.Config;
import com.fanxing.jackie_craft_talismans.client.model.item.PigTalismanItemModel;
import com.fanxing.jackie_craft_talismans.client.render.layer.TalismanGlowingLayer;
import com.fanxing.jackie_craft_talismans.entity.attachment.HeadEyeOffset;
import com.fanxing.jackie_craft_talismans.item.AbstractTalismanItem;
import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import com.fanxing.jackie_craft_talismans.registry.ItemTypesJCT;
import com.fanxing.lib.ConfigFxLib;
import com.fanxing.lib.client.render.component.Lightning;
import com.fanxing.lib.client.render.shape.CapsuleRenderer;
import com.fanxing.lib.client.render.shape.SphereRenderer;
import com.fanxing.lib.client.render.type.BeamRenderType;
import com.fanxing.lib.integration.curio.CurioSetup;
import com.fanxing.lib.integration.curio.util.UsingInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.List;

import static com.fanxing.jackie_craft_talismans.item.PigTalismanItem.*;

@EventBusSubscriber(Dist.CLIENT)
public class PigTalismanItemRender extends GeoItemRenderer<PigTalismanItem> {
    public PigTalismanItemRender() {
        super(new PigTalismanItemModel());
        this.addRenderLayer(new TalismanGlowingLayer<>(this, "textures/item/pig_talisman_glow_add.png"));
    }

    // ======================= 第二三人称渲染 =======================
    @SubscribeEvent
    public static void onRenderPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity.isUsingItem()) {
            ItemStack stack = entity.getUseItem();
            if (stack.getItem() instanceof PigTalismanItem) {
                PoseStack poseStack = event.getPoseStack();
                float partialTick = event.getPartialTick();
                //KEY 这里的渲染偏移必须要再转换回原点，因为我这个计算方式是用的逻辑眼睛高度相对于原点的偏移计算出来的，必须转换回原点
                Vec3 renderPoseOffset = event.getRenderer().getRenderOffset(entity, partialTick);
                poseStack.pushPose();
                poseStack.translate(-renderPoseOffset.x, -renderPoseOffset.y, -renderPoseOffset.z);
                render(poseStack, event.getMultiBufferSource(), entity, HeadEyeOffset.getOffset(entity), getColors(stack), entity.getTicksUsingItem(), event.getPartialTick());
                poseStack.popPose();
            }
        }
        UsingInfo usingCurio = CurioSetup.HELPER.getUsingCurio(entity, Config.CURIO_TALISMAN_SLOT, ItemTypesJCT.PIG_TALISMAN.get());
        if (usingCurio != null) {
            ItemStack stack = usingCurio.stack();
            PoseStack poseStack = event.getPoseStack();
            float partialTick = event.getPartialTick();
            Vec3 renderPoseOffset = event.getRenderer().getRenderOffset(entity, partialTick);
            poseStack.pushPose();
            poseStack.translate(-renderPoseOffset.x, -renderPoseOffset.y, -renderPoseOffset.z);
            render(poseStack, event.getMultiBufferSource(), entity, HeadEyeOffset.getOffset(entity), getColors(stack), usingCurio.usingTicks(), event.getPartialTick());
            poseStack.popPose();
        }
    }

    // ======================= 第一人称渲染 =======================
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.getCameraType().isFirstPerson()) return;
        Entity cameraEntity = mc.getCameraEntity();
        if (!(cameraEntity instanceof LivingEntity entity)) return;
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        if (entity.isUsingItem()) {
            ItemStack stack = entity.getUseItem();
            if (stack.getItem() instanceof PigTalismanItem) {
                float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
                //KEY 这里不需要移动的相机与目标相减的位置的原因是：已经判定了是否是第一人称了，所以能进入的poseStack已经是第一人称了
                // 目前只能做到站立、蹲下、坐下等姿态的眼睛激光同步

                PoseStack poseStack = event.getPoseStack();
                poseStack.pushPose();
                Vec3 offset = entity.getPosition(partialTick).subtract(event.getCamera().getPosition());
                poseStack.translate(offset.x, offset.y, offset.z);
                render(poseStack, bufferSource, entity, HeadEyeOffset.getOffset(entity), getColors(stack), entity.getTicksUsingItem(), partialTick);
                poseStack.popPose();
            }
        }
        UsingInfo usingCurio = CurioSetup.HELPER.getUsingCurio(entity, Config.CURIO_TALISMAN_SLOT, ItemTypesJCT.PIG_TALISMAN.get());
        if (usingCurio != null) {
            ItemStack stack = usingCurio.stack();
            float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
            Vec3 offset = entity.getPosition(partialTick).subtract(event.getCamera().getPosition());
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(offset.x, offset.y, offset.z);
            render(poseStack, bufferSource, entity, HeadEyeOffset.getOffset(entity), getColors(stack), usingCurio.usingTicks(), partialTick);
            poseStack.popPose();
        }
        bufferSource.endBatch();
    }

    // ======================= 公共渲染方法 =======================
    public static void render(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity, HeadEyeOffset headEyeOffset, List<Integer> colors, float usingTicks, float partialTick) {
        poseStack.pushPose();
        poseStack.translate(0, entity.getEyeHeight() + headEyeOffset.headPivotLogicEyeOffset(), 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getViewYRot(partialTick)));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getViewXRot(partialTick) + 90f));
        poseStack.translate(0, headEyeOffset.eyeSurfaceOffset(), -headEyeOffset.eyeHeightOffset());
        float animTick = entity.tickCount + partialTick;
        float gap = headEyeOffset.eyeGap();
        float progress;
        Vec3[] eyes = new Vec3[]{new Vec3(gap, 0, 0), new Vec3(-gap, 0, 0)};
        if (usingTicks < AbstractTalismanItem.CHARGE_DURATION) {
            progress = Math.min(1.0f, (usingTicks + partialTick) / AbstractTalismanItem.CHARGE_DURATION);
            renderLightningSphere(poseStack.last(), buffer, eyes[0], progress, (long) (animTick + 2), colors);
            renderLightningSphere(poseStack.last(), buffer, eyes[1], progress, (long) (animTick + 4), colors);
        } else {
            progress = Math.min(1.0f, (usingTicks + partialTick - AbstractTalismanItem.CHARGE_DURATION) / GROW_DURATION);
            List<Vec3> eyesPosition = HeadEyeOffset.getEyesPosition(entity, partialTick);
            for (int i = 0; i < eyesPosition.size(); i++) {
                Vec3 start = eyesPosition.get(i);
                Vec3 direction = entity.getViewVector(partialTick);
                Vec3 end = start.add(direction.scale(PigTalismanItem.LASER_RANGE));
                BlockHitResult clip = entity.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
                float length = (float) clip.getLocation().subtract(start).length() * progress * progress;
                float vOffset = -(animTick * 0.3f) % 1.0f;
                renderLightningBeam(poseStack.last(), buffer, eyes[i], length, vOffset, (long) (animTick + start.x * 100), colors);
            }
        }
        poseStack.popPose();
    }

    /**
     * 蓄力效果：左右眼发光的球体 + 径向闪电
     */
    private static void renderLightningSphere(PoseStack.Pose pose, MultiBufferSource buffer, Vec3 start, float progress, long seed, List<Integer> colors) {
        float radius = RADIUS * progress;
        int segments = ConfigFxLib.Client.SEGMENTS.getAsInt();
        // 内层球体
        SphereRenderer.render(pose, buffer.getBuffer(BeamRenderType.BEAM_NO_TRANSPARENCY_WHITE), start.toVector3f(), radius * 0.3f, segments,
                colors.get(0), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        // 外层球体
        SphereRenderer.render(pose, buffer.getBuffer(BeamRenderType.ENERGY_BEAM_WHITE), start.toVector3f(), radius, segments,
                colors.get(1), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        Lightning.renderRadial(pose, buffer.getBuffer(BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP_WHITE), start, 3, radius, radius * 10f, radius, RandomSource.create(seed),
                colors.get(2), colors.get(3));
    }

    /**
     * 发射效果：左右眼胶囊体光束 + 沿光束的闪电
     */
    private static void renderLightningBeam(PoseStack.Pose pose, MultiBufferSource buffer, Vec3 start, float length, float offset, long seed, List<Integer> colors) {
        int segments = ConfigFxLib.Client.SEGMENTS.getAsInt();
        // 内层胶囊
        CapsuleRenderer.render(pose, buffer.getBuffer(BeamRenderType.BEAM_NO_TRANSPARENCY_WHITE), start.toVector3f(), RADIUS * 0.3f, length, segments,
                colors.get(0), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        // 外层流动胶囊体
        CapsuleRenderer.render(pose, buffer.getBuffer(BeamRenderType.ENERGY_FLOW_BEAM_WHITE), start.toVector3f(), RADIUS, length, segments,
                colors.get(1), OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT, 1.0f, length * 0.5f, 0f, offset);
        // 雷电特效
        Lightning.render(pose, buffer.getBuffer(BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP_WHITE), start, start.add(0, length, 0), RADIUS, RandomSource.create(seed),
                colors.get(2), colors.get(3));
    }

}
