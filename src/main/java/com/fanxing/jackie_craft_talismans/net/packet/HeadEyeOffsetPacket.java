package com.fanxing.jackie_craft_talismans.net.packet;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.fanxing.jackie_craft_talismans.entity.attachment.HeadEyeOffset;
import com.fanxing.jackie_craft_talismans.registry.AttachmentTypesJCT;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * 客户端 → 服务端 同步 HeadEyeOffset 数据包
 * @author FanXing
 * @since 2026-04-25
 */
public record HeadEyeOffsetPacket(HeadEyeOffset offset) implements CustomPacketPayload {
    public static final Type<HeadEyeOffsetPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID, "head_eye_offset_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HeadEyeOffsetPacket> STREAM_CODEC = StreamCodec.composite(
            HeadEyeOffset.STREAM_CODEC, HeadEyeOffsetPacket::offset,
            HeadEyeOffsetPacket::new
    );
    // 服务端处理逻辑
    public static void handle(HeadEyeOffsetPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> context.player().setData(AttachmentTypesJCT.EYE_OFFSET, packet.offset));
    }
    @Override
    @NotNull
    public Type<HeadEyeOffsetPacket> type() {
        return TYPE;
    }
}