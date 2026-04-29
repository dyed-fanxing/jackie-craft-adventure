package com.fanxing.jackie_craft_talismans.registry;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, JackieCraftTalismans.MOD_ID);
    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder){
        return ENTITY_TYPES.register(name,() -> builder.build(ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID,name).toString()));
    }

}
