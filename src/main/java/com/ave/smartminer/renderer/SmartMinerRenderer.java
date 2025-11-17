package com.ave.smartminer.renderer;

import com.ave.smartminer.blockentity.SmartMinerBlock;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerType;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class SmartMinerRenderer implements BlockEntityRenderer<SmartMinerBlockEntity> {

    public SmartMinerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SmartMinerBlockEntity be, float pt, PoseStack pose, MultiBufferSource buf, int light,
            int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        if (be.type == null || be.type == SmartMinerType.Unknown || be.fuel == 0)
            return;

        ItemStack stack = new ItemStack(be.type.minedItem);
        Direction direction = be.getBlockState().getValue(SmartMinerBlock.FACING);
        long gameTime = be.getLevel().getGameTime();

        drawBlock(pose, itemRenderer, stack, be, buf, getZShift(gameTime, 0), 0.5f, direction);
        drawBlock(pose, itemRenderer, stack, be, buf, getZShift(gameTime, 250), 0.4f, direction);
        drawBlock(pose, itemRenderer, stack, be, buf, getZShift(gameTime, 500), 0.5f, direction);
        drawBlock(pose, itemRenderer, stack, be, buf, getZShift(gameTime, 750), 0.6f, direction);
    }

    private float getZShift(long gameTime, int delay) {
        float shift = ((gameTime + delay) % 1000) / 200f - 0.5f;
        return Math.clamp(shift, -0.5f, 1.5f);
    }

    private void drawBlock(PoseStack pose, ItemRenderer itemRenderer, ItemStack stack,
            SmartMinerBlockEntity be, MultiBufferSource buf, float sx, float sz, Direction direction) {
        if (sx >= 1.5f || sx <= -0.5f)
            return;
        if (direction == Direction.WEST || direction == Direction.SOUTH)
            sx = 1 - sx;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            float temp = sx;
            sx = sz;
            sz = temp;
        }

        pose.pushPose();
        pose.translate(sx, 0.5f, sz);
        pose.scale(0.7f, 0.7f, 0.7f);

        int light = getLightLevel(be.getLevel(), be.getBlockPos());
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, pose, buf,
                be.getLevel(), 1);
        pose.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
