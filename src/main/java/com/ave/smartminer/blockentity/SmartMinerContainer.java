package com.ave.smartminer.blockentity;

import com.ave.smartminer.blockentity.handlers.InputItemHandler;
import com.ave.smartminer.blockentity.handlers.OutputItemHandler;
import com.ave.smartminer.blockentity.handlers.SidedItemHandler;
import com.ave.smartminer.screen.SmartMinerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

public class SmartMinerContainer extends BlockEntity implements MenuProvider {
    public final SidedItemHandler inventory;
    public static final int OUTPUT_SLOT = 0;

    public SmartMinerContainer(BlockEntityType<SmartMinerBlockEntity> entity, BlockPos pos, BlockState state,
            int size) {
        super(entity, pos, state);
        inventory = new SidedItemHandler(size) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MINER_BLOCK_ENTITY.get(),
                (be, direction) -> be.getItemHandler(direction));
    }

    public IItemHandler getItemHandler(Direction side) {
        if (side == Direction.DOWN)
            return new OutputItemHandler(inventory);
        return new InputItemHandler(inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.smartminer.smart_miner");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SmartMinerMenu(containerId, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        // todo inventory.getStackInSlot(2).limitSize(1);
    }
}
