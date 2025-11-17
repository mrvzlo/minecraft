package com.ave.smartminer.blockentity.partblock;

import com.ave.smartminer.blockentity.SmartMinerBlock;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PartBlock extends Block implements EntityBlock {
    public static final MapCodec<BlockPos> CONTROLLER_POS_CODEC = BlockPos.CODEC.fieldOf("controller");

    public PartBlock(Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PartBlockEntity(pos, state);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return ItemInteractionResult.SUCCESS;

        BlockPos ctrlPos = part.getControllerPos();
        SmartMinerBlock ctrl = (SmartMinerBlock) level.getBlockState(ctrlPos).getBlock();
        ctrl.useItemOn(stack, state, level, ctrlPos, player, hand, hit);

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return;

        if (state.getBlock() != newState.getBlock()) {
            BlockPos controllerPos = part.getControllerPos();
            BlockState controllerState = level.getBlockState(controllerPos);
            if (controllerState.getBlock() instanceof SmartMinerBlock)
                level.destroyBlock(controllerPos, true);
        }
        super.onRemove(state, level, pos, newState, moved);
    }
}
