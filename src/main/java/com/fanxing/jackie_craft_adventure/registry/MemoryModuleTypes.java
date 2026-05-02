package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MemoryModuleTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, JackieCraftAdventure.MOD_ID);

    public static void register(IEventBus bus) {
        MEMORY_MODULE_TYPES.register(bus);
    }
}
