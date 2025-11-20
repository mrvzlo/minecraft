package com.ave.simplestationsminer.blockentity.partblock;

import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return InteractionResult.SUCCESS;

        BlockPos ctrlPos = part.getControllerPos();
        MinerBlock ctrl = (MinerBlock) level.getBlockState(ctrlPos).getBlock();
        ctrl.use(state, level, ctrlPos, player, hand, hit);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return;
        super.onRemove(state, level, pos, newState, moved);

        if (state.getBlock() == newState.getBlock())
            return;
        BlockPos controllerPos = part.getControllerPos();
        BlockState controllerState = level.getBlockState(controllerPos);
        if (controllerState.getBlock() instanceof MinerBlock)
            level.destroyBlock(controllerPos, true);

    }
}
