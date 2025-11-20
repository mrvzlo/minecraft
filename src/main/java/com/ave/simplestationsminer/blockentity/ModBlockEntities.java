package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.partblock.PartBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
                        .create(Registries.BLOCK_ENTITY_TYPE, SimpleStationsMiner.MODID);

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MinerBlockEntity>> MINER_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("miner",
                                        () -> BlockEntityType.Builder
                                                        .of(MinerBlockEntity::new,
                                                                        SimpleStationsMiner.MINER_BLOCK.get())
                                                        .build(null));

        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PartBlockEntity>> PART_BLOCK_ENTITY = BLOCK_ENTITIES
                        .register("miner_part",
                                        () -> BlockEntityType.Builder
                                                        .of(PartBlockEntity::new,
                                                                        SimpleStationsMiner.MINER_PART.get())
                                                        .build(null));
}