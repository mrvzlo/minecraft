package com.ave.simplestationsminer.screen;

import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.uihelpers.UIBlocks;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class MinerScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    public final MinerBlockEntity miner;

    public MinerScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getEntityWorld().getBlockEntity(pos), new ArrayPropertyDelegate(5));
    }

    public MinerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(Registrations.MINER_SCREEN_HANDLER, syncId);
        this.miner = (MinerBlockEntity) blockEntity;
        this.inventory = (Inventory) blockEntity;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addSlot(new Slot(inventory, MinerBlockEntity.TYPE_SLOT, UIBlocks.FILTER_SLOT.left, UIBlocks.FILTER_SLOT.top) {
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem().equals(Items.STONE);
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.OUTPUT_SLOT, UIBlocks.OUT_SLOT.left, UIBlocks.OUT_SLOT.top) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        addSlot(new Slot(inventory, MinerBlockEntity.FUEL_SLOT, UIBlocks.FUEL_SLOT.left, UIBlocks.FUEL_SLOT.top));
        addSlot(new Slot(inventory, MinerBlockEntity.COOLANT_SLOT, UIBlocks.COOL_SLOT.left, UIBlocks.COOL_SLOT.top));
        addSlot(new Slot(inventory, MinerBlockEntity.REDSTONE_SLOT, UIBlocks.CATA_SLOT.left, UIBlocks.CATA_SLOT.top));

        addProperties(arrayPropertyDelegate);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
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
