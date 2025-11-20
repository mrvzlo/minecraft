package com.ave.smartminer.blockentity.handlers;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerContainer;
import com.ave.smartminer.datagen.ModTags;

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
        if (slot == SmartMinerContainer.OUTPUT_SLOT)
            return false;
        if (slot == SmartMinerBlockEntity.FUEL_SLOT)
            return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL
                    || stack.getItem() == Items.COAL_BLOCK;
        if (slot == SmartMinerBlockEntity.COOLANT_SLOT)
            return stack.getItem() == Items.LAPIS_BLOCK || stack.getItem() == Items.LAPIS_LAZULI;
        if (slot == SmartMinerBlockEntity.REDSTONE_SLOT)
            return stack.getItem() == Items.REDSTONE_BLOCK || stack.getItem() == Items.REDSTONE;

        if (slot == SmartMinerBlockEntity.TYPE_SLOT)
            return stack.is(ModTags.Items.MINEABLE_TAG);

        return super.isItemValid(slot, stack);
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (slot == SmartMinerBlockEntity.TYPE_SLOT)
            return 1;
        return super.getStackLimit(slot, stack);
    }

    public NonNullList<ItemStack> getAsList() {
        return stacks;
    }
}