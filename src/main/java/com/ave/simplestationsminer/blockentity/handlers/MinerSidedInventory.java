package com.ave.simplestationsminer.blockentity.handlers;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.registrations.Registrations;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public interface MinerSidedInventory extends SidedInventory {
    public DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty())
            markDirty();
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        SimpleStationsMiner.LOGGER.info("Put " + stack + " in " + slot + " " + stack.isIn(Registrations.MINEABLE_TAG));

        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack())
            stack.setCount(getMaxCountPerStack());
    }

    @Override
    default void markDirty() {
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default void clear() {
        getItems().clear();
    }

    @Override
    default int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++)
            result[i] = i;
        return result;
    }

    @Override
    default boolean canInsert(int slot, ItemStack stack, Direction dir) {
        if (slot == MinerBlockEntity.FUEL_SLOT)
            return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL || stack.getItem() == Items.COAL_BLOCK;
        if (slot == MinerBlockEntity.COOLANT_SLOT)
            return stack.getItem() == Items.LAPIS_BLOCK || stack.getItem() == Items.LAPIS_LAZULI;
        if (slot == MinerBlockEntity.REDSTONE_SLOT)
            return stack.getItem() == Items.REDSTONE_BLOCK || stack.getItem() == Items.REDSTONE;
        if (slot == MinerBlockEntity.TYPE_SLOT)
            return stack.isIn(Registrations.MINEABLE_TAG);
        return false;
    }

    @Override
    default boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == MinerBlockEntity.OUTPUT_SLOT;
    }
}