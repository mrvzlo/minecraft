package com.ave.smartminer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AutoMinerBlockEntity extends BaseContainerBlockEntity {
    private int progress = 0;
    private static final int MAX_PROGRESS = 100; // каждые 5 секунд при 20 TPS
    public static final int SIZE = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public AutoMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    // The getter for our item stack list.
    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    // The setter for our item stack list.
    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    // The display name of the menu. Don't forget to add a translation!
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.smartminer.iron_miner");
    }

    // The menu to create from this container. See below for what to return here.
    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    public void tick() {
        if (level.isClientSide)
            return;

        ItemStack slot = this.getItem(0);
        if (slot.getCount() >= slot.getMaxStackSize()) {
            SmartMiner.LOGGER.info("Slot is full");
            return;
        }

        SmartMiner.LOGGER.info(this.progress + "");
        progress++;
        if (progress < MAX_PROGRESS)
            return;

        progress = 0;
        this.setItem(slot.getCount() + 1, new ItemStack(Items.RAW_IRON));

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        setChanged();
        SmartMiner.LOGGER.info("Added");
    }
}
