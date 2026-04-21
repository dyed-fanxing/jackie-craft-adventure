package com.fanxing.jackie_craft_talismans.client.render.layer;

import com.fanxing.jackie_craft_talismans.common.render.CapsuleRenderer;
import com.fanxing.jackie_craft_talismans.common.render.SphereRenderer;
import com.fanxing.jackie_craft_talismans.common.render.component.Beam;
import com.fanxing.jackie_craft_talismans.common.render.component.Lightning;
import com.fanxing.jackie_craft_talismans.common.render.types.BeamRenderType;
import com.fanxing.jackie_craft_talismans.item.AbstractTalismanItem;
import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import com.fanxing.jackie_craft_talismans.mixin.ModelPartAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.fanxing.jackie_craft_talismans.item.PigTalismanItem.*;

@EventBusSubscriber(Dist.CLIENT)
public class PigTalismanBeamRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public PigTalismanBeamRenderLayer(LivingEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    // ======================= 第三人称渲染 =======================
    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isUsingItem()) return;
        ItemStack stack = entity.getUseItem();
        if (!(stack.getItem() instanceof PigTalismanItem )) return;
        M model = getParentModel();
        if (!(model instanceof HeadedModel headedModel)) return;

        ModelPart head = headedModel.getHead();
        poseStack.pushPose();
        head.translateAndRotate(poseStack);
        List<ModelPart.Cube> cubes = ((ModelPartAccessor) (Object) head).getCubes();
        if (!cubes.isEmpty()) {
            ModelPart.Cube cube = cubes.getFirst();
            poseStack.translate(0, 0, -Math.abs(cube.maxZ - cube.minZ) * 0.5f / 16);
        }
        poseStack.translate(0, -0.2F, 0); // 眼睛偏移
        poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        render(poseStack,buffer,entity,stack,partialTick);
        poseStack.popPose();
    }

    // ======================= 第一人称渲染 =======================
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.getCameraType().isFirstPerson()) return;

        Entity cameraEntity = mc.getCameraEntity();
        if (!(cameraEntity instanceof LivingEntity living)) return;
        if (!living.isUsingItem()) return;
        ItemStack stack = living.getUseItem();
        if (!(stack.getItem() instanceof PigTalismanItem )) return;
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        //KEY 这里不需要移动的相机与目标相减的位置的原因是：已经判定了是否是第一人称了，所以能进入的poseStack已经是第一人称了
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-Mth.rotLerp(partialTick, living.yRotO, living.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, living.xRotO + 90f, living.getXRot() + 90f)));
        poseStack.translate(0, 0.1f, 0);
        render(poseStack,bufferSource,living,stack,partialTick);
        poseStack.popPose();
        bufferSource.endBatch();
    }




    // ======================= 公共渲染方法 =======================
    private static void render(PoseStack poseStack, MultiBufferSource buffer,LivingEntity entity,ItemStack stack,float partialTick){
        float animTick = entity.tickCount + partialTick;
        int usingTicks = entity.getTicksUsingItem();
        float progress;
        Vec3 leftEye = new Vec3(-GAP, 0, 0);
        Vec3 rightEye = new Vec3(GAP, 0, 0);
        if (usingTicks < AbstractTalismanItem.CHARGE_DURATION) {
            progress = Math.min(1.0f, (usingTicks + partialTick) / AbstractTalismanItem.CHARGE_DURATION);
            renderLightningSphere(poseStack.last(), buffer, leftEye,progress, (long) (animTick+2), stack);
            renderLightningSphere(poseStack.last(), buffer, rightEye,progress, (long) (animTick+4), stack);
        } else {
            progress = Math.min(1.0f, (usingTicks + partialTick - AbstractTalismanItem.CHARGE_DURATION) / GROW_DURATION);
            Vec3 start = entity.getEyePosition(partialTick);
            Vec3 direction = entity.getViewVector(partialTick);
            Vec3 end = start.add(direction.scale(PigTalismanItem.LASER_RANGE));
            BlockHitResult clip = entity.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
            float length = (float) clip.getLocation().subtract(start).length()* progress*progress;
            float offset = -(animTick * 0.3f) % 1.0f;
            renderLightningBeam(poseStack.last(), buffer, leftEye,length,offset, (long) (animTick + 2),stack);
            renderLightningBeam(poseStack.last(), buffer, rightEye,length,offset, (long) (animTick + 4),stack);
        }
    }
    /** 蓄力效果：左右眼发光的球体 + 径向闪电 */
    private static void renderLightningSphere(PoseStack.Pose pose, MultiBufferSource buffer,Vec3 start,float progress,long seed,ItemStack stack) {
        float radius = RADIUS * progress;
        int[] beamColor = PigTalismanItem.getBeamColor(stack);
        int[] lightingColor = PigTalismanItem.getLightingColor(stack);
        // 内层球体（亮黄）
        SphereRenderer.render(pose, buffer.getBuffer(Beam.BEAM_NO_TRANSPARENCY), start.toVector3f(), radius * 0.3f, SEGMENTS,
                beamColor[0],OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        // 外层球体（暗橙黄）
        SphereRenderer.render(pose, buffer.getBuffer(Beam.BEAM_ENERGY),start.toVector3f(), radius, SEGMENTS,
                beamColor[1],OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        Lightning.renderRadial(pose, buffer.getBuffer(BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP_WHITE), start, 3, radius, radius * 10f, radius, RandomSource.create(seed),
                lightingColor[0],lightingColor[1]);
    }
    /** 发射效果：左右眼胶囊体光束 + 沿光束的闪电 */
    private static void renderLightningBeam(PoseStack.Pose pose, MultiBufferSource buffer,Vec3 start,float length,float offset,long seed,ItemStack stack) {
        int[] beamColor = PigTalismanItem.getBeamColor(stack);
        int[] lightingColor = PigTalismanItem.getLightingColor(stack);
        // 内层胶囊体（亮黄）
        CapsuleRenderer.render(pose, buffer.getBuffer(Beam.BEAM_NO_TRANSPARENCY_TRIANGLE_STRIP), buffer.getBuffer(Beam.BEAM_NO_TRANSPARENCY),start.toVector3f(), RADIUS * 0.3f, length, SEGMENTS,
                beamColor[0],OverlayTexture.NO_OVERLAY, LightTexture.FULL_BRIGHT);
        // 外层胶囊体（带流动纹理）
        CapsuleRenderer.render(pose, buffer.getBuffer(Beam.FLOW_BEAM_TRIANGLE_STRIP), buffer.getBuffer(Beam.BEAM_ENERGY),start.toVector3f(), RADIUS, length, SEGMENTS,
                beamColor[1],OverlayTexture.NO_OVERLAY,  LightTexture.FULL_BRIGHT, 1.0f, length * 0.5f,0f, offset);
        Lightning.render(pose, buffer.getBuffer(BeamRenderType.ENERGY_BEAM_TRIANGLE_STRIP_WHITE), start, start.add(0, length, 0), RADIUS, RandomSource.create(seed),
                lightingColor[0],lightingColor[1]);
    }
}