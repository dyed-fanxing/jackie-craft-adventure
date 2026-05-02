package com.fanxing.jackie_craft_adventure.event.handler;

import com.mojang.logging.LogUtils;
import com.fanxing.jackie_craft_adventure.JackieCraftAdventure;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author FanXing
 * @since 2025-10-19 21:40
 */
@EventBusSubscriber(modid = JackieCraftAdventure.MOD_ID)
public class PlayerHandler {
    private static final Logger log = LoggerFactory.getLogger(PlayerHandler.class);
    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
    }
    @SubscribeEvent
    public static void onPlayerTickPos(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
    }
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {

    }
    @SubscribeEvent
    public static void onStopTracking(PlayerEvent.StopTracking event) {
    }


    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        LogUtils.getLogger().info("玩家数据{}",event.getEntity().getPersistentData());
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
//        LogUtils.getLogger().info("玩家数据{}",event.getEntity().getPersistentData());
        LogUtils.getLogger().info("玩家手中物品数据{}",event.getEntity().getUseItem().getComponents());
    }

}
