package com.ave.simplestationsminer.blockentity.handlers;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.blockentity.ModContainer;
import com.ave.simplestationsminer.datagen.ModTags;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;

public class SidedItemHandler extends ItemStackHandler {
    public SidedItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == ModContainer.OUTPUT_SLOT)
            return false;
        if (slot == MinerBlockEntity.FUEL_SLOT)
            return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL
                    || stack.getItem() == Items.COAL_BLOCK;
        if (slot == MinerBlockEntity.COOLANT_SLOT)
            return stack.getItem() == Items.LAPIS_BLOCK || stack.getItem() == Items.LAPIS_LAZULI;
        if (slot == MinerBlockEntity.REDSTONE_SLOT)
            return stack.getItem() == Items.REDSTONE_BLOCK || stack.getItem() == Items.REDSTONE;

        if (slot == MinerBlockEntity.TYPE_SLOT)
            return stack.is(ModTags.Items.MINEABLE_TAG);

        return super.isItemValid(slot, stack);
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (slot == MinerBlockEntity.TYPE_SLOT)
            return 1;
        return super.getStackLimit(slot, stack);
    }

    public NonNullList<ItemStack> getAsList() {
        return stacks;
    }
}