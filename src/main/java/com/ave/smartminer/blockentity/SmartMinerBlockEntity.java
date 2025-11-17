package com.ave.smartminer.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    public static final int FUEL_CAPACITY = 1200;
    public static final int MAX_PROGRESS = 400;
    private static final int INCREMENT = 1;
    public SmartMinerType type = SmartMinerType.Unknown;
    public int progress = 0;
    public boolean working = false;
    public int fuel = 0;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    public void tick() {
        if (level.isClientSide)
            return;

        if (performAllChecks())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    private boolean performAllChecks() {
        boolean updateBlock = checkNewType() || checkFuel();
        if (type == null || type == SmartMinerType.Unknown)
            return updateBlock;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = fuel > 0 && slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type.minedItem);
        if (!working)
            return updateBlock;

        progress++;
        fuel--;
        updateBlock = updateBlock || progress % 25 == 0;

        if (progress < MAX_PROGRESS)
            return updateBlock;

        progress = 0;
        ItemStack toAdd = new ItemStack(type.minedItem);
        toAdd.setCount(slot.getCount() + INCREMENT);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);
        setChanged();
        return updateBlock;
    }

    private boolean checkFuel() {
        ItemStack fuelStack = inventory.getStackInSlot(FUEL_SLOT);
        int fuelPerCoal = 300;
        if (fuelStack.isEmpty() || fuel + fuelPerCoal > FUEL_CAPACITY)
            return false;

        fuelStack.shrink(1);
        inventory.setStackInSlot(FUEL_SLOT, fuelStack);
        fuel += fuelPerCoal;
        return true;
    }

    private boolean checkNewType() {
        Item slot = inventory.getStackInSlot(TYPE_SLOT).getItem();
        SmartMinerType newType = SmartMinerType.findMatch(slot);
        if (type == newType)
            return false;

        type = newType;
        progress = 0;
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("type", type.getSerializedName());
        tag.putInt("fuel", fuel);
        tag.putInt("progress", progress);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        String typeStr = tag.getString("type");
        type = SmartMinerType.findMatch(typeStr);
        fuel = tag.getInt("fuel");
        progress = tag.getInt("progress");
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        tag.putString("type", type.getSerializedName());
        tag.putInt("progress", progress);
        tag.putInt("fuel", fuel);
    }
}
