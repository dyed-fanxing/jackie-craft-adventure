package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.lib.FxLib;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentsJCA {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, FxLib.MOD_ID);


    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
