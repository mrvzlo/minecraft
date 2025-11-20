package com.ave.simplestationsminer.screen;

import java.util.Arrays;
import java.util.List;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.uihelpers.NumToString;
import com.ave.simplestationsminer.uihelpers.UIBlocks;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MinerScreen extends AbstractContainerScreen<MinerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleStationsMiner.MODID,
            "textures/gui/base_miner_gui.png");

    public MinerScreen(MinerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        super.render(gfx, mouseX, mouseY, partialTicks);
        this.renderTooltip(gfx, mouseX, mouseY);

        if (!(menu.blockEntity instanceof MinerBlockEntity miner))
            return;

        int startX = (width - imageWidth) / 2;
        int startY = (height - imageHeight) / 2;

        if (UIBlocks.FUEL_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String fuelPart = NumToString.parse(miner.fuel.getEnergyStored(), "RF / ")
                    + NumToString.parse(Config.FUEL_CAPACITY.get(), "RF");
            List<Component> fuelText = Arrays.asList(Component.translatable("screen.simplestationsminer.fuel"),
                    Component.literal(fuelPart));
            gfx.renderComponentTooltip(font, fuelText, mouseX, mouseY);
        }

        if (UIBlocks.COOL_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String coolantPart = miner.coolant + " / " + Config.MAX_COOLANT.get();
            List<Component> coolantText = Arrays.asList(Component.translatable("screen.simplestationsminer.coolant"),
                    Component.literal(coolantPart));
            gfx.renderComponentTooltip(font, coolantText, mouseX, mouseY);
        }

        if (UIBlocks.CATA_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String redstonePart = miner.redstone + " / " + Config.MAX_CATALYST.get();
            List<Component> redstoneText = Arrays.asList(Component.translatable("screen.simplestationsminer.catalysis"),
                    Component.literal(redstonePart));
            gfx.renderComponentTooltip(font, redstoneText, mouseX, mouseY);
        }

        if (miner.progress > 0 && UIBlocks.PROGRESS_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            int progressPart = (int) Math.ceil(100 * miner.progress / Config.MAX_PROGRESS.get());
            gfx.renderTooltip(font, Component.literal(progressPart + "%"), mouseX, mouseY);
        }

        if (miner.type == null && UIBlocks.FILTER_SLOT.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.filter"), mouseX, mouseY);
        }

        if (miner.invalidDepth && UIBlocks.ERROR.isHovered(mouseX - startX, mouseY - startY)) {
            gfx.renderTooltip(font, Component.translatable("screen.simplestationsminer.depthError"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float tick, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

        if (!(menu.blockEntity instanceof MinerBlockEntity miner))
            return;

        int tickAlpha = 96 + (int) (63 * Math.sin(System.currentTimeMillis() / 400.0));
        int borderColor = (tickAlpha << 24) | 0xFF0000;
        float progressPart = miner.progress / Config.MAX_PROGRESS.get();
        UIBlocks.PROGRESS_BAR.drawProgressToRight(graphics, x, y, progressPart, 0xFFCCFEDD);

        if (miner.invalidDepth) {
            UIBlocks.ERROR.drawText(graphics, x, y, font, borderColor, "Y > 20");
            return;
        }

        float fuelPart = (float) miner.fuel.getEnergyStored() / Config.FUEL_CAPACITY.get();
        UIBlocks.FUEL_BAR.drawProgressToTop(graphics, x, y, fuelPart, 0xAA225522);

        float coolantPart = (float) miner.coolant / Config.MAX_COOLANT.get();
        UIBlocks.COOL_BAR.drawProgressToTop(graphics, x, y, coolantPart, 0xAA3333AA);

        float redstonePart = (float) miner.redstone / Config.MAX_CATALYST.get();
        UIBlocks.CATA_BAR.drawProgressToTop(graphics, x, y, redstonePart, 0xAABB2211);

        if (fuelPart == 0)
            UIBlocks.FUEL_SLOT.drawBorder(graphics, x, y, borderColor);
        if (coolantPart == 0)
            UIBlocks.COOL_SLOT.drawBorder(graphics, x, y, borderColor);
        if (redstonePart == 0)
            UIBlocks.CATA_SLOT.drawBorder(graphics, x, y, borderColor);
    }
}
