package com.fanxing.jackie_craft_talismans.integration;

import com.fanxing.lib.integration.IntegrationFx;
import com.fanxing.lib.integration.curio.CurioSetup;
import net.neoforged.bus.api.IEventBus;

/**
 * @author dyed_fanxing
 * @date 2026/4/28 18:55
 * 联动其他模组时的注册
 */
public class Integration {
    public static void register(IEventBus bus) {
        if (IntegrationFx.IS_LOAD_CURIOS) CurioSetup.register(bus);
    }
}
