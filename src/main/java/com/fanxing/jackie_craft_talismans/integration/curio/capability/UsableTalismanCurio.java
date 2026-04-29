package com.fanxing.jackie_craft_talismans.integration.curio.capability;

import com.fanxing.jackie_craft_talismans.Config;
import com.fanxing.jackie_craft_talismans.item.AbstractTalismanItem;
import com.fanxing.lib.integration.curio.capability.UsableCurio;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 16:04
 */
public class UsableTalismanCurio extends UsableCurio {
    public UsableTalismanCurio(ItemStack stack) {
        super(stack);
    }

    @Override
    public void useCurio(LivingEntity entity, String slotType, int slotIndex) {
        startUsingCurio(entity, slotType, slotIndex);
    }

    @Override
    protected void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {
        if (stack.getItem() instanceof AbstractTalismanItem item) {
            item.useTick(level, entity, stack, getUsingTicks(entity, remainingTicks));
        }
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        super.onEquip(slotContext, prevStack);
        LivingEntity wearer = slotContext.entity();
        if (wearer.getMainHandItem().getItem() == this.stack.getItem()) {
            // 立即移除刚添加的该饰品属性（通过 ID）
            ResourceLocation modifierId = CuriosImplMixinHooks.getSlotId(slotContext);
            ItemAttributeModifiers vanilla = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (vanilla != null) {
                for (ItemAttributeModifiers.Entry entry : vanilla.modifiers()) {
                    AttributeInstance inst = wearer.getAttributes().getInstance(entry.attribute());
                    if (inst != null) inst.removeModifier(modifierId);
                }
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .flatMap(inv -> inv.getStacksHandler(Config.CURIO_TALISMAN_SLOT))
                .map(handler -> {
                    IDynamicStackHandler stacks = handler.getStacks();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if (stacks.getStackInSlot(i).getItem() == stack.getItem()) {
                            return false; // 已存在相同物品
                        }
                    }
                    return true;
                })
                .orElse(true); // 如果槽位类型不存在，默认允许装备
    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext, ResourceLocation id) {
        Multimap<Holder<Attribute>, AttributeModifier> map = LinkedHashMultimap.create();
        ItemAttributeModifiers vanilla = this.stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (vanilla != null) {
            for (ItemAttributeModifiers.Entry entry : vanilla.modifiers()) {
                Holder<Attribute> attr = entry.attribute();
                AttributeModifier oldMod = entry.modifier();
                AttributeModifier newMod = new AttributeModifier(id, oldMod.amount(), oldMod.operation());
                map.put(attr, newMod);

            }
        }
        return map;
    }
}
