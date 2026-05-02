package com.fanxing.jackie_craft_adventure.integration.curio;

import com.fanxing.jackie_craft_adventure.integration.curio.event.TalismanActivationHandler;
import com.fanxing.jackie_craft_adventure.integration.curio.event.TalismanCurioForceRefreshHandler;
import com.fanxing.jackie_craft_adventure.integration.curio.register.ItemCapabilities;
import com.fanxing.jackie_craft_adventure.integration.curio.register.KeyBindingsJCTItg;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 12:15
 */
public class CurioSetup {
    public static void register(IEventBus bus) {
        bus.addListener(ItemCapabilities::registerCapabilities);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(KeyBindingsJCTItg::registerKeys);
            NeoForge.EVENT_BUS.addListener(TalismanActivationHandler::onClientTick);
        }
        NeoForge.EVENT_BUS.addListener(TalismanCurioForceRefreshHandler::onMainhandChange);
    }
}
