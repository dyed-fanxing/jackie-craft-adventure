package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.lib.particle.options.TrackEntityParticleOption;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ParticleTypesJCA {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, JackieCraftAdventure.MOD_ID);






    public static final DeferredHolder<ParticleType<?>, ParticleType<TrackEntityParticleOption>> FLAME_BALL = register("flame_ball", false,
            TrackEntityParticleOption::codec, TrackEntityParticleOption::streamCodec);








    private static DeferredHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimiter) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(overrideLimiter));
    }
    private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> register(
            String name, boolean overrideLimiter, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec
    ) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override public @NotNull MapCodec<T> codec() { return codec; }
            @Override public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return streamCodec; }
        });
    }
    private static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> register(
            String name, boolean overrideLimiter,
            final Function<ParticleType<T>, MapCodec<T>> codecFactory,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecFactory
    ) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            public @NotNull MapCodec<T> codec() {
                return codecFactory.apply(this);
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecFactory.apply(this);
            }
        });
    }


    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
