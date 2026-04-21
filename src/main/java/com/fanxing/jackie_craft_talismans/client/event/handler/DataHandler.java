package com.fanxing.jackie_craft_talismans.client.event.handler;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.mojang.logging.LogUtils;
import com.fanxing.jackie_craft_talismans.data.DamageTypeTagsProvider;
import com.fanxing.jackie_craft_talismans.data.EntityTypeTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = JackieCraftTalismans.MOD_ID)
public class DataHandler {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        LogUtils.getLogger().info("GatherDataEvent 已触发！");  // 确认事件是否触发
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        LogUtils.getLogger().info("数据前");
        event.getGenerator().addProvider(event.includeServer(), new EntityTypeTagsProvider(output, lookupProvider, JackieCraftTalismans.MOD_ID, existingFileHelper));
        event.getGenerator().addProvider(event.includeServer(), new DamageTypeTagsProvider(output, lookupProvider, JackieCraftTalismans.MOD_ID, existingFileHelper));
    }
}
