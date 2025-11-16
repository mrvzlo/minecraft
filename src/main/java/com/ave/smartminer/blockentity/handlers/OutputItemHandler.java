package com.ave.smartminer.blockentity.handlers;

import com.ave.smartminer.blockentity.SmartMinerBlockEntity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class OutputItemHandler implements IItemHandler {
    private final SidedItemHandler parent;

    public OutputItemHandler(SidedItemHandler parent) {
        this.parent = parent;
    }

    @Override
    public int getSlots() {
        return parent.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return parent.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        // Output не принимает предметы вообще
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // Разрешено только вытаскивать slot 1
        if (slot != SmartMinerBlockEntity.OUTPUT_SLOT)
            return ItemStack.EMPTY;
        return parent.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return parent.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return parent.isItemValid(slot, stack);
    }
}
