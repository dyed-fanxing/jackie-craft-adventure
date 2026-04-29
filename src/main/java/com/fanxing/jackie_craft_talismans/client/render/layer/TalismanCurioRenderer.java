//package com.fanxing.jackie_craft_talismans.client.render.layer;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.entity.RenderLayerParent;
//import net.minecraft.world.item.ItemDisplayContext;
//import top.theillusivec4.curios.api.client.ICurioRenderer;
//
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import top.theillusivec4.curios.api.SlotContext;
//
/// **
// * @author dyed_fanxing
// * @date 2026/4/27 15:50
// */
//public class TalismanCurioRenderer implements ICurioRenderer {
//
//    @Override
//    public <T extends LivingEntity, M extends EntityModel<T>> void render(
//            ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource bufferSource,
//            int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//
//        // 这里写渲染逻辑
//        // 简单示例：在玩家胸前渲染一个物品模型
//        LivingEntity entity = slotContext.entity();
//        poseStack.pushPose();
//        // 根据需要平移旋转，例如挂载到身体上
//        ICurioRenderer.translateIfSneaking(poseStack, entity);
//        ICurioRenderer.followBodyRotations(entity, (HumanoidModel<LivingEntity>) renderLayerParent.getModel());
//        poseStack.translate(0,0,-0.1);
//        poseStack.scale(1, -1, 1);
//
//        // 获取物品模型渲染器（可复用原版的物品模型）
//        var dispatcher = Minecraft.getInstance().getItemRenderer();
//        dispatcher.renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 0);
//
//        poseStack.popPose();
//    }
//}