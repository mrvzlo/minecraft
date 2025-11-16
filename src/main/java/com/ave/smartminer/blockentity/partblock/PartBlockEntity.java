package com.ave.smartminer.blockentity.partblock;

import com.ave.smartminer.blockentity.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PartBlockEntity extends BlockEntity {

    private BlockPos controllerPos;

    public PartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PART_BLOCK_ENTITY.get(), pos, state);
    }

    public void setControllerPos(BlockPos pos) {
        controllerPos = pos;
        setChanged();
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (controllerPos == null)
            return;
        tag.putLong("Controller", controllerPos.asLong());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        controllerPos = BlockPos.of(tag.getLong("Controller"));
    }
}
