package com.ave.smartminer.screen;

import com.ave.smartminer.SmartMiner;
import com.ave.smartminer.blockentity.SmartMinerBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmartMinerScreen extends AbstractContainerScreen<SmartMinerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SmartMiner.MODID,
            "textures/gui/base_miner_gui.png");

    public SmartMinerScreen(SmartMinerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float tick, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

        if (menu.blockEntity instanceof SmartMinerBlockEntity miner) {
            int fuelPart = miner.fuel * 35 / SmartMinerBlockEntity.FUEL_CAPACITY;
            graphics.fill(x + 8, y + 50 - fuelPart, x + 24, y + 50, 0xAA225522);
            int coolantPart = miner.coolant * 35 / SmartMinerBlockEntity.MAX_COOLANT;
            graphics.fill(x + 26, y + 50 - coolantPart, x + 42, y + 50, 0xAA3333AA);
            int redstonePart = miner.redstone * 35 / SmartMinerBlockEntity.MAX_REDSTONE;
            graphics.fill(x + 44, y + 50 - redstonePart, x + 60, y + 50, 0xAABB2211);

            int progressPart = miner.progress * 16 / SmartMinerBlockEntity.MAX_PROGRESS;
            graphics.fill(x + 80, y + 55, x + 80 + progressPart, y + 58, 0xFFCCFEDD);

            int tickAlpha = 128 + (int) (127 * Math.sin(System.currentTimeMillis() / 500.0));
            int borderColor = (tickAlpha << 24) | 0xBB3300;
            if (miner.fuel == 0)
                drawBorder(graphics, x + 7, y + 51, borderColor);
            if (miner.coolant == 0)
                drawBorder(graphics, x + 25, y + 51, borderColor);
            if (miner.redstone == 0)
                drawBorder(graphics, x + 43, y + 51, borderColor);

        }
    }

    private void drawBorder(GuiGraphics g, int x, int y, int color) {
        g.fill(x, y, x + 18, y + 1, color);
        g.fill(x, y + 17, x + 18, y + 18, color);
        g.fill(x, y, x + 1, y + 18, color);
        g.fill(x + 17, y, x + 18, y + 18, color);

    }
}
