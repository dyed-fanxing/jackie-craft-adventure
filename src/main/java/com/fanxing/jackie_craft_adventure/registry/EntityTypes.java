package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.entity.summon.LongBeamEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, JackieCraftAdventure.MOD_ID);

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(ResourceLocation.fromNamespaceAndPath(JackieCraftAdventure.MOD_ID, name).toString()));
    }

    // ======================= 实体注册 =======================
    public static final DeferredHolder<EntityType<?>, EntityType<LongBeamEntity>> LONG_BEAM = register("long_beam",
            EntityType.Builder.<LongBeamEntity>of(LongBeamEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(10)
                    .updateInterval(1)
    );
}
