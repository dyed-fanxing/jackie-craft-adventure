package com.fanxing.jackie_craft_adventure.client;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.client.particle.FlameBallParticle;
import com.fanxing.jackie_craft_adventure.client.render.entity.LongBeamRenderer;
import com.fanxing.jackie_craft_adventure.net.packet.HeadEyeOffsetPacket;
import com.fanxing.jackie_craft_adventure.registry.EntityTypes;
import com.fanxing.jackie_craft_adventure.registry.ParticleTypesJCA;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = JackieCraftAdventure.MOD_ID, value = Dist.CLIENT)
public class Setup {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityTypes.LONG_BEAM.get(), LongBeamRenderer::new);
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void registerParticleProviderHandler(final RegisterParticleProvidersEvent event) {
    }

    @SubscribeEvent
    public static void registerPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(HeadEyeOffsetPacket.TYPE, HeadEyeOffsetPacket.STREAM_CODEC, HeadEyeOffsetPacket::handle);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(final RegisterMenuScreensEvent event) {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
//            CuriosRendererRegistry.register(ItemTypesJCT.PIG_TALISMAN.get(), PigTalismanCurioRenderer::new);
        });
    }
    // 在客户端事件（ClientModEventBusSubscriber）中
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpecial(ParticleTypesJCA.FLAME_BALL.get(),new FlameBallParticle.Provider());
    }
}
