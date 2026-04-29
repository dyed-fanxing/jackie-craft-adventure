package com.fanxing.jackie_craft_talismans.integration.curio.event;

import com.fanxing.jackie_craft_talismans.Config;
import com.fanxing.jackie_craft_talismans.item.AbstractTalismanItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 23:27
 */
public class TalismanCurioForceRefreshHandler {
    /**
     * 手动同步玩家所有 talisman 饰品的实际属性（根据主手冲突状态）
     * 此方法直接操作 AttributeInstance，不影响物品 tooltip。
     */
    private static void syncCurioAttributes(LivingEntity entity) {
        CuriosApi.getCuriosInventory(entity).flatMap(inv -> inv.getStacksHandler(Config.CURIO_TALISMAN_SLOT)).ifPresent(handler -> {
            IDynamicStackHandler slotStacks = handler.getStacks();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = slotStacks.getStackInSlot(i);
                if (stack.isEmpty() || !(stack.getItem() instanceof AbstractTalismanItem)) continue;
                ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath("curios", Config.CURIO_TALISMAN_SLOT + i);
                ItemAttributeModifiers vanilla = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
                if (vanilla == null) continue;
                boolean conflict = entity.getMainHandItem().getItem() == stack.getItem();
                for (ItemAttributeModifiers.Entry entry : vanilla.modifiers()) {
                    AttributeInstance inst = entity.getAttributes().getInstance(entry.attribute());
                    if (inst == null) continue;
                    if (conflict) {
                        // 移除该饰品添加的修饰符
                        inst.removeModifier(modifierId);
                    } else if (!inst.hasModifier(modifierId)) {
                        // 确保修饰符存在
                        AttributeModifier newMod = new AttributeModifier(modifierId, entry.modifier().amount(), entry.modifier().operation());
                        inst.addTransientModifier(newMod);
                    }
                }
            }
        });
    }

    public static void onMainhandChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot() != EquipmentSlot.MAINHAND) return;
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        syncCurioAttributes(entity);
    }
}