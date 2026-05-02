package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, JackieCraftAdventure.MOD_ID);

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
