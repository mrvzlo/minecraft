package com.ave.smartminer.renderer;

import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.ave.smartminer.blockentity.SmartMinerType;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SmartMinerRenderer implements BlockEntityRenderer<SmartMinerBlockEntity> {

    public SmartMinerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SmartMinerBlockEntity be, float pt, PoseStack pose, MultiBufferSource buf, int light,
            int overlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        drawMineCart(pose, itemRenderer, new ItemStack(Items.MINECART), be, buf);
        if (be.type == null || be.type == SmartMinerType.Unknown)
            return;
        ItemStack stack = new ItemStack(be.type.minedItem);
        drawBlock(pose, itemRenderer, stack, be, buf);
    }

    private void drawBlock(PoseStack pose, ItemRenderer itemRenderer, ItemStack stack,
            SmartMinerBlockEntity be, MultiBufferSource buf) {
        pose.pushPose();
        pose.translate(0.5f, 1f, 0.5f);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 0xA000A0, OverlayTexture.NO_OVERLAY, pose, buf,
                be.getLevel(), 1);
        pose.popPose();
    }

    private void drawMineCart(PoseStack pose, ItemRenderer itemRenderer, ItemStack stack,
            SmartMinerBlockEntity be, MultiBufferSource buf) {
        pose.pushPose();
        pose.translate(0.5f, 0.5f, 0.5f);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 0xA000A0, OverlayTexture.NO_OVERLAY, pose, buf,
                be.getLevel(), 1);
        pose.popPose();
    }
}
