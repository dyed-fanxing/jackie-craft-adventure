package com.fanxing.jackie_craft_talismans.integration.curio.register;

import com.fanxing.jackie_craft_talismans.integration.curio.capability.UsableTalismanCurio;
import com.fanxing.jackie_craft_talismans.registry.ItemTypesJCT;
import com.fanxing.lib.integration.curio.register.CapabilitiesFxLibItg;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;

import java.util.WeakHashMap;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 16:02
 */
public class ItemCapabilities {
    private static final WeakHashMap<ItemStack, UsableTalismanCurio> CACHE = new WeakHashMap<>();
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, ctx) -> CACHE.computeIfAbsent(stack, UsableTalismanCurio::new), ItemTypesJCT.PIG_TALISMAN.get());
        event.registerItem(CapabilitiesFxLibItg.USABLE_CURIO, (stack, ctx) -> CACHE.computeIfAbsent(stack, UsableTalismanCurio::new), ItemTypesJCT.PIG_TALISMAN.get());
    }
}
