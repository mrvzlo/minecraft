package com.ave.simplestationsminer.blockentity;

import com.ave.simplestationsminer.blockentity.handlers.InputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.OutputItemHandler;
import com.ave.simplestationsminer.blockentity.handlers.SidedItemHandler;
import com.ave.simplestationsminer.screen.MinerMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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

public class ModContainer extends BlockEntity implements MenuProvider {
    public final SidedItemHandler inventory;
    public static final int OUTPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int TYPE_SLOT = 2;
    public static final int COOLANT_SLOT = 3;
    public static final int REDSTONE_SLOT = 4;

    public ModContainer(BlockEntityType<MinerBlockEntity> entity, BlockPos pos, BlockState state,
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
        return Component.translatable("container.simplestationsminer.miner");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new MinerMenu(containerId, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
