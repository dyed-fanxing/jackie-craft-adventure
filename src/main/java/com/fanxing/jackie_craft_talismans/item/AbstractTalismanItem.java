package com.fanxing.jackie_craft_talismans.item;

import com.fanxing.lib.registry.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTalismanItem extends Item{
    public static final int CHARGE_DURATION = 10;   // 蓄力刻数

    public AbstractTalismanItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    /**
     * 右键开始使用（长按）
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        stack.set(DataComponents.USING_ENTITY_ID,player.getId());
        return InteractionResultHolder.consume(stack);
    }

    /**
     * 停止使用（松开右键）
     */
    @Override
    public void onStopUsing(@NotNull ItemStack stack, @NotNull LivingEntity entity, int count) {
        stack.remove(DataComponents.USING_ENTITY_ID);
    }

}
