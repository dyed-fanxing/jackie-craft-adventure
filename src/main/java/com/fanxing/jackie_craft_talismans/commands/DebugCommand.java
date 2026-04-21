package com.fanxing.jackie_craft_talismans.commands;

import com.fanxing.jackie_craft_talismans.utils.GravityUtils;
import com.mojang.brigadier.arguments.*;
import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;

/**
 * @author FanXing
 * @since 2026-01-08 22:13
 */
@EventBusSubscriber(modid = JackieCraftTalismans.MOD_ID)
public class DebugCommand {

    @SubscribeEvent
    public static void onRegisterCommandsG(RegisterCommandsEvent event) {

    }
}
