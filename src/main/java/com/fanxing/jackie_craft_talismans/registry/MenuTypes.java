package com.fanxing.jackie_craft_talismans.registry;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, JackieCraftTalismans.MOD_ID);

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
