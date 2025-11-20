package com.ave.simplestationsminer.blockentity.partblock;

import com.ave.simplestationsminer.blockentity.ModBlockEntities;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.handlers.InputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.OutputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.SidedItemHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class PartBlockEntity extends BlockEntity {

    private BlockPos controllerPos;

    public PartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PART_BLOCK_ENTITY.get(), pos, state);
    }

    public void setControllerPos(BlockPos pos) {
        controllerPos = pos;
        setChanged();
        if (level == null || level.isClientSide)
            return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.PART_BLOCK_ENTITY.get(),
                (be, direction) -> be.getItemHandler(direction, be));
    }

    public IItemHandler getItemHandler(Direction side, PartBlockEntity be) {
        SidedItemHandler inventory = ((MinerBlockEntity) be.getLevel().getBlockEntity(be.controllerPos)).inventory;
        if (side == Direction.DOWN)
            return new OutputItemHandler(inventory);
        return new InputItemHandler(inventory);
    }

    public EnergyStorage getEnergyStorage(PartBlockEntity be) {
        return ((MinerBlockEntity) be.getLevel().getBlockEntity(be.controllerPos)).fuel;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (controllerPos == null)
            return;
        tag.putLong("Controller", controllerPos.asLong());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        controllerPos = BlockPos.of(tag.getLong("Controller"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
