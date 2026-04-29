package com.fanxing.jackie_craft_talismans.integration.curio.event;

import com.fanxing.jackie_craft_talismans.Config;
import com.fanxing.jackie_craft_talismans.registry.ItemTypesJCT;
import com.fanxing.lib.integration.curio.CurioSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import static com.fanxing.jackie_craft_talismans.integration.curio.register.KeyBindings.CURIO_USE_TALISMAN;

/**
 * @author dyed_fanxing
 * @date 2026/4/29 16:08
 */
public class TalismanActivationHandler {
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity living) {
            CurioSetup.HELPER.handleInputKey(living, CURIO_USE_TALISMAN, (slotInfo -> slotInfo.stack().getItem() == ItemTypesJCT.PIG_TALISMAN.get()), Config.CURIO_TALISMAN_SLOT);
        }
    }

}
