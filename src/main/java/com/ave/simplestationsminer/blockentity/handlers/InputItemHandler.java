package com.ave.simplestationsminer.blockentity.handlers;

import net.neoforged.neoforge.items.IItemHandler;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;

import net.minecraft.world.item.ItemStack;

public class InputItemHandler implements IItemHandler {
    private final SidedItemHandler parent;

    public InputItemHandler(SidedItemHandler parent) {
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
        // Нельзя класть в выход
        if (slot == MinerBlockEntity.OUTPUT_SLOT)
            return stack;
        return parent.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // input-хендлер ничего не отдаёт
        return ItemStack.EMPTY;
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
