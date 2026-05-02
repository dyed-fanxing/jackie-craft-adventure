package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import com.fanxing.jackie_craft_adventure.entity.attachment.HeadEyeOffset;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * @author FanXing
 * @since 2025-09-13 22:07
 */
@EventBusSubscriber
public class AttachmentTypesJCA {
    private static final Logger log = LoggerFactory.getLogger(AttachmentTypesJCA.class);
    public static DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, JackieCraftAdventure.MOD_ID);

    public static final Supplier<AttachmentType<HeadEyeOffset>> EYE_OFFSET =
            ATTACHMENT_TYPES.register("eye_offset", () -> AttachmentType.builder(HeadEyeOffset::getDefaultPlayer)
                    .serialize(HeadEyeOffset.CODEC).sync(HeadEyeOffset.STREAM_CODEC).copyOnDeath().build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    }
}
