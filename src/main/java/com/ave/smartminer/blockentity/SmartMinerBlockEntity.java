package com.ave.smartminer.blockentity;

import com.ave.smartminer.SmartMiner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SmartMinerBlockEntity extends SmartMinerContainer {
    private int progress = 0;
    private static final int MAX_PROGRESS = 10;
    private static final int INCREMENT = 1;
    public SmartMinerType type = SmartMinerType.Unknown;
    public boolean working = false;

    public SmartMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINER_BLOCK_ENTITY.get(), pos, state, 3);
    }

    public void tick() {
        checkNewType();
        if (level.isClientSide)
            return;

        if (type == null || type == SmartMinerType.Unknown)
            return;

        ItemStack slot = inventory.getStackInSlot(OUTPUT_SLOT);
        working = slot.getCount() < slot.getMaxStackSize()
                && (slot.getCount() == 0 || slot.getItem() == type.minedItem);
        if (!working)
            return;

        progress++;
        if (progress < MAX_PROGRESS)
            return;

        progress = 0;
        ItemStack toAdd = new ItemStack(type.minedItem);
        toAdd.setCount(slot.getCount() + INCREMENT);
        inventory.setStackInSlot(OUTPUT_SLOT, toAdd);

        setChanged();
        SmartMiner.LOGGER.info("Added");
    }

    private void checkNewType() {
        Item slot = inventory.getStackInSlot(2).getItem();
        SmartMinerType newType = getType(slot);
        if (type == newType)
            return;

        type = newType;
        progress = 0;
    }

    private SmartMinerType getType(Item item) {
        for (SmartMinerType t : SmartMinerType.values())
            if (t.minedItem == item)
                return t;
        return SmartMinerType.Unknown;
    }
}
