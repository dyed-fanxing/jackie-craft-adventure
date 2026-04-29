package com.fanxing.jackie_craft_talismans.mixin;

import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void shouldRender(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity living && living.isUsingItem()) {
            if (living.getUseItem().getItem() instanceof PigTalismanItem) {
                // 强制渲染，或扩大视锥体范围
                // 这里简单返回 true，但可能导致性能问题。更好的做法：扩大视锥体范围。
                cir.setReturnValue(true);
            }
        }
    }
}