package com.ave.smartminer.screen;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerContainer;
import com.ave.smartminer.uihelpers.UIBlocks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SmartMinerMenu extends AbstractContainerMenu {
    public final Level level;
    public final SmartMinerContainer blockEntity;

    public SmartMinerMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory,
                (SmartMinerContainer) inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public SmartMinerMenu(int containerId, Inventory inventory, SmartMinerContainer be) {
        super(ModMenuTypes.SMART_MINER_MENU.get(), containerId);
        level = inventory.player.level();
        blockEntity = be;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        addSlot(new SlotItemHandler(blockEntity.inventory, SmartMinerContainer.OUTPUT_SLOT, UIBlocks.OUT_SLOT.left,
                UIBlocks.OUT_SLOT.top));
        addSlot(new SlotItemHandler(blockEntity.inventory, SmartMinerContainer.FUEL_SLOT, UIBlocks.FUEL_SLOT.left,
                UIBlocks.FUEL_SLOT.top));
        addSlot(new SlotItemHandler(blockEntity.inventory, SmartMinerContainer.TYPE_SLOT, UIBlocks.FILTER_SLOT.left,
                UIBlocks.FILTER_SLOT.top));
        addSlot(new SlotItemHandler(blockEntity.inventory, SmartMinerContainer.COOLANT_SLOT, UIBlocks.COOL_SLOT.left,
                UIBlocks.COOL_SLOT.top));
        addSlot(new SlotItemHandler(blockEntity.inventory, SmartMinerContainer.REDSTONE_SLOT, UIBlocks.CATA_SLOT.left,
                UIBlocks.CATA_SLOT.top));

        if (blockEntity instanceof SmartMinerBlockEntity miner)
            addDataSlots(miner);
    }

    private void addDataSlots(SmartMinerBlockEntity miner) {
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return miner.coolant;
            }

            @Override
            public void set(int value) {
                miner.coolant = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return miner.redstone;
            }

            @Override
            public void set(int value) {
                miner.redstone = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (int) miner.progress;
            }

            @Override
            public void set(int value) {
                miner.progress = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return miner.working ? 1 : 0;
            }

            @Override
            public void set(int value) {
                miner.working = value != 0;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return miner.fuel.getEnergyStored();
            }

            @Override
            public void set(int value) {
                miner.fuel = new EnergyStorage(SmartMinerBlockEntity.FUEL_CAPACITY, 0, 0, value);
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        int HOTBAR_SLOT_COUNT = 9;
        int PLAYER_INVENTORY_ROW_COUNT = 3;
        int PLAYER_INVENTORY_COLUMN_COUNT = 9;
        int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
        int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
        int VANILLA_FIRST_SLOT_INDEX = 0;
        int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
        int TE_INVENTORY_SLOT_COUNT = 5;

        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY; // EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY; // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
                    false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player,
                SmartMiner.SMART_MINER_BLOCK.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int k = 0; k < 3; ++k)
            for (int j = 0; j < 9; ++j)
                addSlot(new Slot(inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));

    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int j = 0; j < 9; ++j)
            addSlot(new Slot(inventory, j, 8 + j * 18, 142));
    }
}
