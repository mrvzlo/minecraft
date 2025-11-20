package com.ave.simplestationsminer;

import org.slf4j.Logger;

import com.ave.simplestationsminer.blockentity.ModBlockEntities;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.partblock.PartBlock;
import com.ave.simplestationsminer.blockentity.partblock.PartBlockEntity;
import com.ave.simplestationsminer.screen.ModMenuTypes;
import com.ave.simplestationsminer.sound.ModSounds;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimpleStationsMiner.MODID)
public class SimpleStationsMiner {
        public static final String MODID = "simplestationsminer";
        public static final Logger LOGGER = LogUtils.getLogger();
        public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, MODID);

        public static final DeferredBlock<Block> MINER_BLOCK = BLOCKS.register("miner",
                        () -> new MinerBlock(BlockBehaviour.Properties.of()
                                        .strength(0.1F).lightLevel((state) -> 12).noOcclusion()));

        public static final DeferredBlock<Block> MINER_PART = BLOCKS.register("miner_part",
                        () -> new PartBlock(BlockBehaviour.Properties.of()
                                        .strength(0.1F).noOcclusion()));

        public static final DeferredItem<BlockItem> MINER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
                        "miner",
                        MINER_BLOCK);

        public static final DeferredItem<Item> MINER_DRILL = ITEMS.registerItem("miner_drill", Item::new,
                        new Item.Properties());

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
                        .register("example_tab", () -> CreativeModeTab.builder()
                                        .title(Component.translatable("itemGroup.simplestationsminer")) // The language
                                                                                                        // key for
                                        // the title of your
                                        // CreativeModeTab
                                        .withTabsBefore(CreativeModeTabs.COMBAT)
                                        .icon(() -> MINER_BLOCK_ITEM.get().getDefaultInstance())
                                        .displayItems((parameters, output) -> {
                                                output.accept(MINER_BLOCK_ITEM.get());
                                                output.accept(MINER_DRILL.get());
                                        }).build());

        public SimpleStationsMiner(IEventBus modEventBus, ModContainer modContainer) {
                BLOCKS.register(modEventBus);
                ITEMS.register(modEventBus);
                CREATIVE_MODE_TABS.register(modEventBus);
                ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
                ModMenuTypes.register(modEventBus);
                ModSounds.SOUND_EVENTS.register(modEventBus);

                modEventBus.addListener(this::addCreative);
                modEventBus.addListener(this::registerCapabilities);
                modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        }

        // Add the example block item to the building blocks tab
        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
                        event.accept(MINER_BLOCK_ITEM);
        }

        private void registerCapabilities(RegisterCapabilitiesEvent event) {
                event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                                (level, pos, state, be, side) -> ((MinerBlockEntity) be).fuel,
                                MINER_BLOCK.get());
                event.registerBlock(Capabilities.EnergyStorage.BLOCK,
                                (level, pos, state, be, side) -> ((PartBlockEntity) be)
                                                .getEnergyStorage((PartBlockEntity) be),
                                MINER_PART.get());
        }
}