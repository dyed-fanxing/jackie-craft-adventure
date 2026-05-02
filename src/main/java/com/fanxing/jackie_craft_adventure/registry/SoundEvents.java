package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundEvents {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, JackieCraftAdventure.MOD_ID);
    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static DeferredHolder<SoundEvent, SoundEvent> register(String name){
        return SOUNDS.register(name,() ->
                SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID,name)));
    }
}
