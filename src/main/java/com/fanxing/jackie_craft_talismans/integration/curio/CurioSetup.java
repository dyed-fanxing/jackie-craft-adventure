package com.fanxing.jackie_craft_talismans.integration.curio;

import com.fanxing.jackie_craft_talismans.integration.curio.event.TalismanActivationHandler;
import com.fanxing.jackie_craft_talismans.integration.curio.event.TalismanCurioForceRefreshHandler;
import com.fanxing.jackie_craft_talismans.integration.curio.register.ItemCapabilities;
import com.fanxing.jackie_craft_talismans.integration.curio.register.KeyBindings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 12:15
 */
public class CurioSetup {
    public static void register(IEventBus bus) {
        bus.addListener(TalismanCurioForceRefreshHandler::onMainhandChange);
        bus.addListener(ItemCapabilities::registerCapabilities);
        // 仅客户端的事件
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(KeyBindings::registerKeys);
            bus.addListener(TalismanActivationHandler::onClientTick);
        }
    }
}
