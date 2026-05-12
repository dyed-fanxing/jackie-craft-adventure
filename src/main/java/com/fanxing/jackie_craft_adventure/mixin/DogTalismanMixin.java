package com.fanxing.jackie_craft_adventure.mixin;

/**
 * @author dyed_fanxing
 * @date 2026/5/3 22:42
 */

import com.fanxing.jackie_craft_adventure.item.DogTalismanItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.EffectCures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DogTalismanMixin {

    @Inject(method = "checkTotemDeathProtection", at = @At("RETURN"), cancellable = true)
    private void onCheckTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        // 原版图腾已生效，直接返回
        if (cir.getReturnValueZ()) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // 遍历快捷栏（0-8）查找狗符咒
        ItemStack dogStack = null;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof DogTalismanItem) {
                dogStack = stack;
                break;
            }
        }
        // 如果快捷栏没有，可选再查副手（按你要求，如果只想要快捷栏则删掉）
        if (dogStack == null) {
            ItemStack offhand = player.getOffhandItem();
            if (offhand.getItem() instanceof DogTalismanItem) {
                dogStack = offhand;
            }
        }
        if (dogStack == null) return;
        // 狗符咒生效：锁血 + 增益 + 动画 + 消耗
        player.setHealth(1.0F);
        player.removeEffectsCuredBy(EffectCures.PROTECTED_BY_TOTEM);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level().broadcastEntityEvent(player, (byte) 35);
        cir.setReturnValue(true);
    }
}