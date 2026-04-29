package com.fanxing.jackie_craft_talismans;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = JackieCraftTalismans.MOD_ID)
public class Config {
    public static boolean isLoadCurio = ModList.get().isLoaded("curios");
    public static final String CURIO_TALISMAN_SLOT = "talisman";

    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        public Common(ModConfigSpec.Builder builder) {
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    public static final Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        public Server(ModConfigSpec.Builder builder) {
        }
    }
}