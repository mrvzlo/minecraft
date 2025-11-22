package com.ave.simplestationsminer.registrations;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.partblock.PartBlock;
import com.ave.simplestationsminer.blockentity.partblock.PartBlockEntity;
import com.ave.simplestationsminer.screen.MinerScreen;
import com.ave.simplestationsminer.screen.MinerScreenHandler;
import com.google.common.base.Function;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Registrations {
        public static final Item DRILL_ITEM = register(new Item(new Item.Settings()), "miner_drill");

        public static final Block MINER_BLOCK = register(p -> new MinerBlock(p.luminance(x -> 12)),
                        "miner", true);
        public static final Block MINER_PART = register(p -> new PartBlock(p.nonOpaque()), "miner_part", false);

        public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey
                        .of(Registries.ITEM_GROUP.getKey(), Identifier.of(SimpleStationsMiner.MOD_ID, "item_group"));
        public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
                        .icon(() -> new ItemStack(MINER_BLOCK))
                        .displayName(Text.translatable("itemGroup.simplestationsminer"))
                        .build();

        public static final BlockEntityType<MinerBlockEntity> MINER_BLOCK_ENTITY = register("miner", MinerBlockEntity::new, MINER_BLOCK);

        public static final BlockEntityType<PartBlockEntity> PART_BLOCK_ENTITY = register("miner_part", PartBlockEntity::new, MINER_PART);

        public static final SoundEvent WORK_SOUND = registerSound("work_sound");

        public static final TagKey<Item> MINEABLE_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of(SimpleStationsMiner.MOD_ID, "mineable"));

        public static final ScreenHandlerType<MinerScreenHandler> MINER_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
                        Identifier.of(SimpleStationsMiner.MOD_ID, "miner_menu"),
                        new ExtendedScreenHandlerType<>(MinerScreenHandler::new, BlockPos.PACKET_CODEC));

        public static void initialize() {
                Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
                ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
                        itemGroup.add(MINER_BLOCK);
                        itemGroup.add(DRILL_ITEM);
                });
                HandledScreens.register(MINER_SCREEN_HANDLER, MinerScreen::new);
        }

        private static Item register(Item item, String id) {
                Identifier itemID = Identifier.of(SimpleStationsMiner.MOD_ID, id);
                Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
                return registeredItem;
        }

        private static Block register(Function<AbstractBlock.Settings, Block> function, String name, boolean shouldRegisterItem) {
                Identifier id = Identifier.of(SimpleStationsMiner.MOD_ID, name);
                Block toRegister = function.apply(AbstractBlock.Settings.create());

                if (shouldRegisterItem) {
                        BlockItem blockItem = new BlockItem(toRegister, new Item.Settings());
                        Registry.register(Registries.ITEM, id, blockItem);
                }
                return Registry.register(Registries.BLOCK, id, toRegister);
        }

        private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntityFactory<? extends T> entityFactory, Block... blocks) {
                Identifier id = Identifier.of(SimpleStationsMiner.MOD_ID, name);
                return Registry.register(Registries.BLOCK_ENTITY_TYPE, id,
                                BlockEntityType.Builder.<T>create(entityFactory, blocks).build());
        }

        private static SoundEvent registerSound(String id) {
                Identifier identifier = Identifier.of(SimpleStationsMiner.MOD_ID, id);
                return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
        }
}