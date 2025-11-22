package com.ave.simplestationsminer.blockentity.partblock;

import org.jetbrains.annotations.Nullable;

import com.ave.simplestationsminer.blockentity.MinerBlock;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PartBlock extends BlockWithEntity {
    public static final MapCodec<PartBlock> CODEC = PartBlock.createCodec(PartBlock::new);

    public PartBlock(Settings props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PartBlockEntity(pos, state);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof PartBlockEntity part))
            return state;
        super.onBreak(world, pos, state, player);

        BlockPos ctrlPos = part.getControllerPos();
        if (ctrlPos == null)
            return state;
        BlockState controllerState = world.getBlockState(ctrlPos);
        if (controllerState.getBlock() instanceof MinerBlock mb) {
            mb.onBreak(world, ctrlPos, controllerState, player);
            world.breakBlock(ctrlPos, true);
        }
        return state;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof PartBlockEntity be))
            return ActionResult.SUCCESS;
        BlockPos ctrlPos = be.getControllerPos();
        if (ctrlPos == null)
            return ActionResult.SUCCESS;

        MinerBlockEntity controller = (MinerBlockEntity) world.getBlockEntity(ctrlPos);
        player.openHandledScreen(controller);
        return ActionResult.SUCCESS;

    }
}
