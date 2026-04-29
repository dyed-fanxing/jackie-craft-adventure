package com.fanxing.jackie_craft_talismans.registry;

import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import com.fanxing.lib.item.compoent.ColorPalette;
import com.fanxing.lib.registry.DataComponentsFxLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

import static com.fanxing.jackie_craft_talismans.JackieCraftTalismans.MOD_ID;

public class ItemTypesJCT {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID); // 物品注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);// 创造标签页注册器

    public static void register(IEventBus bus) {
        // 将物品延迟注册器注册到模组事件总线
        ITEMS.register(bus);
        // 将创造创造标签页延迟注册器注册到模组事件总线
        CREATIVE_MODE_TABS.register(bus);
    }

    public static final DeferredHolder<Item, PigTalismanItem> PIG_TALISMAN = ITEMS.register(
            "pig_talisman",
            () -> new PigTalismanItem(new Item.Properties().stacksTo(1)
                    .component(DataComponentsFxLib.COLOR_SCHEME, PigTalismanItem.DEFAULT)
                    .component(DataComponentsFxLib.COLOR_PALETTES, new ArrayList<>(List.of(new ColorPalette("options.gamma.default", PigTalismanItem.DEFAULT))))
                    .attributes(PigTalismanItem.createAttributes())
            )
    );


    // 创建创造标签页，并添加物品，放置在战斗标签页之后
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("jackie_craft_talismans_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jackie_craft_talismans")) // 标签页标题
                    .icon(() -> new ItemStack(PIG_TALISMAN.get()))
                    .withTabsBefore(CreativeModeTabs.COMBAT) // 定位在战斗标签页前
                    .displayItems((parameters, output) -> {
                        output.accept(PIG_TALISMAN.get());
                    }).build());
}
