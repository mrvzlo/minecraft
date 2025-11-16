package com.ave.smartminer.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.partblock.PartBlockEntity;

public class SmartMinerBlock extends Block implements EntityBlock {

    public SmartMinerBlock(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SmartMinerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> {
            if (be instanceof SmartMinerBlockEntity miner)
                miner.tick();
        };
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        SmartMinerBlockEntity blockEntity = (SmartMinerBlockEntity) level.getBlockEntity(pos);
        player.openMenu(new SimpleMenuProvider(blockEntity, Component.literal("SmartMiner")), pos);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        if (level.isClientSide)
            return;

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(dx, 0, dz);
                if (p.equals(pos) || level.getBlockState(p).canBeReplaced())
                    continue;
                SmartMiner.LOGGER.info("Cannot place parts, space occupied " + dx + "," + dz);
                level.destroyBlock(pos, true);
                return;
            }

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(dx, 0, dz);

                if (p.equals(pos))
                    continue;

                level.setBlock(p, SmartMiner.SMART_PART_BLOCK.get().defaultBlockState(), 3);

                PartBlockEntity be = (PartBlockEntity) level.getBlockEntity(p);
                be.setControllerPos(pos);
            }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        super.onRemove(state, level, pos, newState, moving);

        if (level.isClientSide)
            return;

        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = pos.offset(dx, 0, dz);

                if (p.equals(pos))
                    continue;

                BlockEntity be = level.getBlockEntity(p);
                if (be instanceof PartBlockEntity)
                    level.destroyBlock(p, false);
            }
    }
}
