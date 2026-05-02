package com.fanxing.jackie_craft_adventure;

import com.fanxing.jackie_craft_adventure.entity.attachment.HeadEyeOffset;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadEyeOffsetConfig {
    public static final HeadEyeOffsetConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.ConfigValue<List<? extends String>> entityEntries;

    static {
        Pair<HeadEyeOffsetConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(HeadEyeOffsetConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

    private HeadEyeOffsetConfig(ModConfigSpec.Builder builder) {
        builder.comment("实体ID,头部枢轴高度,眼睛高度,眼距 (逗号分隔，空格会被自动忽略)");
        entityEntries = builder
                .translation(JackieCraftAdventure.MOD_ID + ".config.entities_eye_offset")
                .defineList("entries",
                        () -> List.of(
                                "minecraft:zombie,1.4,0.2,0.1,0.1",
                                "minecraft:skeleton,1.4,0.2,0.1,0.1",
                                "minecraft:creeper,1.3,0.1,0.1,0.15",
                                "minecraft:enderman,2.2,0.25,0.1,0.2"
                        ),
                        () -> "minecraft:entity,1.0,0.2,0.125",
                        obj -> obj instanceof String
                );
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        loadIntoDefaults();
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        loadIntoDefaults(); // 用户修改配置后重新加载
    }

    public static void loadIntoDefaults() {
        Map<EntityType<?>, HeadEyeOffset> map = new HashMap<>();
        // 解析实体列表
        for (String entry : INSTANCE.entityEntries.get()) {
            String[] parts = entry.split(",");
            if (parts.length == 5) {
                String key = parts[0].trim();
                EntityType.byString(key).ifPresent(type -> {
                    try {
                        float head = Float.parseFloat(parts[1].trim());
                        float eyeH = Float.parseFloat(parts[2].trim());
                        float eyeSurFaceOffset = Float.parseFloat(parts[3].trim());
                        float gap = Float.parseFloat(parts[4].trim());
                        map.put(type, new HeadEyeOffset(head, eyeH,eyeSurFaceOffset, gap));
                    } catch (NumberFormatException ignored) {
                    }
                });
            }
        }
        HeadEyeOffset.updateDefaults(map);
    }


}