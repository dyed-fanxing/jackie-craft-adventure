package com.fanxing.jackie_craft_adventure.mixin;

import com.fanxing.jackie_craft_adventure.item.DogTalismanItem;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author dyed_fanxing
 * @date 2026/5/3 23:50
 */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    /**
     * 添加不死图腾查找
     */
    @Inject(method = "findTotem", at = @At("HEAD"), cancellable = true)
    private static void onFindTotem(Player player, CallbackInfoReturnable<ItemStack> cir) {
        // 1. 先按原版逻辑：检查主手和副手是否有不死图腾
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(Items.TOTEM_OF_UNDYING)) {
                cir.setReturnValue(stack);
                return;
            }
        }
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof DogTalismanItem) {
            cir.setReturnValue(offhand);
            return;
        }
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof DogTalismanItem) {
                cir.setReturnValue(stack);
                return;
            }
        }
        cir.setReturnValue(new ItemStack(Items.TOTEM_OF_UNDYING));
    }
}