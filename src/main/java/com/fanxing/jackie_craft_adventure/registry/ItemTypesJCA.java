package com.fanxing.jackie_craft_adventure.registry;

import com.fanxing.jackie_craft_adventure.item.*;
import com.fanxing.lib.item.compoent.ColorPalette;
import com.fanxing.lib.registry.DataComponentsFxLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

import static com.fanxing.jackie_craft_adventure.JackieCraftAdventure.MOD_ID;

public class ItemTypesJCA {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID); // 物品注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);// 创造标签页注册器

    public static void register(IEventBus bus) {
        // 将物品延迟注册器注册到模组事件总线
        ITEMS.register(bus);
        // 将创造创造标签页延迟注册器注册到模组事件总线
        CREATIVE_MODE_TABS.register(bus);
    }

    // ======================= 十二生肖符咒 =======================

    public static final DeferredHolder<Item, PigTalismanItem> PIG_TALISMAN = ITEMS.register("pig_talisman",
            () -> new PigTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
                    .component(DataComponentsFxLib.COLOR_SCHEME, PigTalismanItem.DEFAULT)
                    .component(DataComponentsFxLib.COLOR_PALETTES, new ArrayList<>(List.of(
                            new ColorPalette(Component.translatable("options.gamma.default"), PigTalismanItem.DEFAULT))
                    ))
                    .attributes(PigTalismanItem.createAttributes())
            )
    );

    public static final DeferredHolder<Item, RatTalismanItem> RAT_TALISMAN = ITEMS.register("rat_talisman",
            () -> new RatTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, BullTalismanItem> BULL_TALISMAN = ITEMS.register("bull_talisman",
            () -> new BullTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, TigerTalismanItem> TIGER_TALISMAN = ITEMS.register("tiger_talisman",
            () -> new TigerTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, RabbitTalismanItem> RABBIT_TALISMAN = ITEMS.register("rabbit_talisman",
            () -> new RabbitTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, LongTalismanItem> LONG_TALISMAN = ITEMS.register("long_talisman",
            () -> new LongTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
                    .attributes(LongTalismanItem.createAttributes())
            )
    );

    public static final DeferredHolder<Item, SnakeTalismanItem> SNAKE_TALISMAN = ITEMS.register("snake_talisman",
            () -> new SnakeTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, HorseTalismanItem> HORSE_TALISMAN = ITEMS.register("horse_talisman",
            () -> new HorseTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, SheepTalismanItem> SHEEP_TALISMAN = ITEMS.register("sheep_talisman",
            () -> new SheepTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, MonkeyTalismanItem> MONKEY_TALISMAN = ITEMS.register("monkey_talisman",
            () -> new MonkeyTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, ChickenTalismanItem> CHICKEN_TALISMAN = ITEMS.register("chicken_talisman",
            () -> new ChickenTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    public static final DeferredHolder<Item, DogTalismanItem> DOG_TALISMAN = ITEMS.register("dog_talisman",
            () -> new DogTalismanItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
    );

    // 创建创造标签页，并添加物品，放置在战斗标签页之后
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("jackie_craft_adventure_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.jackie_craft_adventure")) // 标签页标题
                    .icon(() -> new ItemStack(PIG_TALISMAN.get()))
                    .withTabsBefore(CreativeModeTabs.COMBAT) // 定位在战斗标签页前
                    .displayItems((parameters, output) -> {
                        output.accept(RAT_TALISMAN.get());
                        output.accept(BULL_TALISMAN.get());
                        output.accept(TIGER_TALISMAN.get());
                        output.accept(RABBIT_TALISMAN.get());
                        output.accept(LONG_TALISMAN.get());
                        output.accept(SNAKE_TALISMAN.get());
                        output.accept(HORSE_TALISMAN.get());
                        output.accept(SHEEP_TALISMAN.get());
                        output.accept(MONKEY_TALISMAN.get());
                        output.accept(CHICKEN_TALISMAN.get());
                        output.accept(DOG_TALISMAN.get());
                        output.accept(PIG_TALISMAN.get());
                    }).build());
}
