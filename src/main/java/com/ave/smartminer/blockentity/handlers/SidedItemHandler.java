package com.ave.smartminer.blockentity.handlers;

import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerContainer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SidedItemHandler extends ItemStackHandler {

    public SidedItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == SmartMinerContainer.OUTPUT_SLOT)
            return false;
        if (slot == SmartMinerBlockEntity.FUEL_SLOT && stack.getItem() != Items.COAL)
            return false;
        return super.isItemValid(slot, stack);
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        return super.getStackLimit(slot, stack);
    }
}