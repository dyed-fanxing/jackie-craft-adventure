package com.fanxing.jackie_craft_talismans.client.render.layer;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.fanxing.jackie_craft_talismans.item.AbstractTalismanItem;
import com.fanxing.lib.registry.DataComponentsFxLib;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TalismanGlowingLayer<T extends AbstractTalismanItem & GeoItem> extends GeoRenderLayer<T> {
    protected final ResourceLocation glowTexture;
    public TalismanGlowingLayer(GeoRenderer<T> entityRendererIn, String glowTexturePath) {
        super(entityRendererIn);
        this.glowTexture = ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID, glowTexturePath);
    }


    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        // 获取当前渲染的物品栈
        ItemStack stack = ((GeoItemRenderer<?>) getRenderer()).getCurrentItemStack();
        Integer id = stack.get(DataComponentsFxLib.USING_ENTITY_ID);
        LivingEntity entity = null;
        if (Minecraft.getInstance().level != null && id != null)
            entity = (LivingEntity) Minecraft.getInstance().level.getEntity(id);
        if (entity == null || !entity.isUsingItem()) return;
        // 获取使用进度
        int usingTicks = entity.getTicksUsingItem();
        float progress =  Math.min(1.0f,(float) usingTicks / AbstractTalismanItem.CHARGE_DURATION);
        if (progress <= 0) return;
        int alpha = (int) (progress * 255);
        int color = (alpha << 24) | 0x00FFFFFF;
        RenderType glowRenderType = RenderType.EYES.apply(glowTexture, RenderStateShard.LIGHTNING_TRANSPARENCY);
        VertexConsumer glowBuffer = bufferSource.getBuffer(glowRenderType);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable,
                glowRenderType, glowBuffer, partialTick, packedLight, packedOverlay, color);
    }
}