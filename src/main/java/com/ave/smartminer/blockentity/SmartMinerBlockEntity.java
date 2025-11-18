package com.ave.smartminer.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    public static final int MAX_PROGRESS = 400;
    public static final int MAX_COOLANT = 20;
    public static final int MAX_REDSTONE = 20;
    public static final int FUEL_CAPACITY = MAX_PROGRESS * 50;
    private static final int INCREMENT = 1;
    private static final int FUEL_PER_COAL = MAX_PROGRESS * 5;
    public Item type = null;
    public int progress = 0;
    public boolean working = false;
    public int fuel = 0;
    public int coolant = 0;
    public int redstone = 0;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 5);
    }

    public void tick() {
        if (level.isClientSide)
            return;

        if (performAllChecks())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    private boolean performAllChecks() {
        boolean updateBlock = checkNewType() | checkFuel() | checkRedstone() | checkCoolant();
        if (type == null)
            return updateBlock;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = fuel > 0 && coolant > 0 && redstone > 0 && slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type);
        if (!working)
            return updateBlock;

        progress++;
        fuel--;
        updateBlock = updateBlock || progress % 25 == 0;

        if (progress < MAX_PROGRESS)
            return updateBlock;

        progress = 0;
        coolant--;
        redstone--;
        ItemStack toAdd = new ItemStack(type);
        toAdd.setCount(slot.getCount() + INCREMENT);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);
        setChanged();
        return true;
    }

    private boolean checkFuel() {
        return checkResource(FUEL_SLOT, Items.COAL_BLOCK, FUEL_PER_COAL, FUEL_CAPACITY, ResourceType.FUEL);
    }

    private boolean checkCoolant() {
        return checkResource(COOLANT_SLOT, Items.LAPIS_BLOCK, 1, MAX_COOLANT, ResourceType.COOLANT);
    }

    private boolean checkRedstone() {
        return checkResource(REDSTONE_SLOT, Items.REDSTONE_BLOCK, 1, MAX_REDSTONE, ResourceType.REDSTONE);
    }

    private boolean checkResource(int slot, Item blockItem, int singleValue, int maxCapacity, ResourceType type) {
        ItemStack stack = inventory.getStackInSlot(slot);
        int increment = stack.getItem().equals(blockItem) ? singleValue * 9 : singleValue;

        if (stack.isEmpty() || getResourceValue(type) + increment > maxCapacity)
            return false;

        stack.shrink(1);
        inventory.setStackInSlot(slot, stack);
        addResource(type, increment);
        return true;
    }

    private void addResource(ResourceType type, int amount) {
        switch (type) {
            case FUEL -> fuel += amount;
            case COOLANT -> coolant += amount;
            case REDSTONE -> redstone += amount;
        }
    }

    private int getResourceValue(ResourceType type) {
        return switch (type) {
            case FUEL -> fuel;
            case COOLANT -> coolant;
            case REDSTONE -> redstone;
        };
    }

    private boolean checkNewType() {
        Item newType = getCurrentFilter();
        if (type == null && newType == null || type != null && type.equals(newType))
            return false;

        type = newType;
        progress = 0;
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("fuel", fuel);
        tag.putInt("progress", progress);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        type = getCurrentFilter();
        fuel = tag.getInt("fuel");
        progress = tag.getInt("progress");
        coolant = tag.getInt("coolant");
        redstone = tag.getInt("redstone");
    }

    private Item getCurrentFilter() {
        ItemStack stack = inventory.getStackInSlot(TYPE_SLOT);
        return stack.isEmpty() ? null : stack.getItem();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        tag.putInt("progress", progress);
        tag.putInt("fuel", fuel);
        tag.putInt("coolant", coolant);
        tag.putInt("redstone", redstone);
    }

    private enum ResourceType {
        FUEL, COOLANT, REDSTONE
    }
}
