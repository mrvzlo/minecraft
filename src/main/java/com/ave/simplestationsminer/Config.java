package com.ave.simplestationsminer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = SimpleStationsMiner.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        static ModConfigSpec SPEC;

        public static ModConfigSpec.IntValue ENERGY_PER_TICK;
        public static ModConfigSpec.IntValue MAX_PROGRESS;
        public static ModConfigSpec.IntValue MAX_COOLANT;
        public static ModConfigSpec.IntValue MAX_CATALYST;

        public static ModConfigSpec.IntValue FUEL_PER_COAL;
        public static ModConfigSpec.IntValue FUEL_CAPACITY;

        static {
                setupGenerationConfig();
                SPEC = BUILDER.build();
        }

        private static void setupGenerationConfig() {
                ENERGY_PER_TICK = BUILDER
                                .comment("How much RF to consume per tick\n Default: 16")
                                .defineInRange("consume", 16, 1, 1000);
                MAX_PROGRESS = BUILDER
                                .comment("Default working time in ticks\n Default: 1000")
                                .defineInRange("work_time", 1000, 1, 100000);
                MAX_COOLANT = BUILDER
                                .comment("Max coolant to store\n Default: 20")
                                .defineInRange("max_coolant", 20, 1, 10000);
                MAX_CATALYST = BUILDER
                                .comment("Max catalyst to store\n Default: 20")
                                .defineInRange("max_catalyst", 20, 1, 10000);
                FUEL_PER_COAL = BUILDER
                                .comment("Base RF amount received from 1 coal\n Default: 48000")
                                .defineInRange("fuel_rf", 48000, 1, 1000000);
                FUEL_CAPACITY = BUILDER
                                .comment("How much RF can be stored\n Default: 480000")
                                .defineInRange("fuel_max", 480000, 1, 2_000_000_000);
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {

        }

}
