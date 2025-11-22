package com.ave.simplestationsminer.screen;

import java.util.Arrays;
import java.util.List;

import com.ave.simplestationsminer.Config;
import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.blockentity.MinerBlockEntity;
import com.ave.simplestationsminer.uihelpers.NumToString;
import com.ave.simplestationsminer.uihelpers.UIBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MinerScreen extends HandledScreen<MinerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(SimpleStationsMiner.MOD_ID,
            "textures/gui/base_miner_gui.png");

    private final MinerBlockEntity be;

    public MinerScreen(MinerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        be = handler.miner;
    }

    @Override
    public void render(DrawContext dc, int mouseX, int mouseY, float delta) {
        super.render(dc, mouseX, mouseY, delta);

        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;

        if (UIBlocks.FUEL_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String fuelPart = NumToString.parse(be.fuel, "RF / ")
                    + NumToString.parse(Config.FUEL_CAPACITY, "RF");
            List<Text> fuelText = Arrays.asList(Text.translatable("screen.simplestationsminer.fuel"),
                    Text.literal(fuelPart));
            dc.drawTooltip(textRenderer, fuelText, startX, startY);
        }

        if (UIBlocks.COOL_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String coolantPart = be.coolant + " / " + Config.MAX_COOLANT;
            List<Text> coolantText = Arrays.asList(Text.translatable("screen.simplestationsminer.coolant"),
                    Text.literal(coolantPart));
            dc.drawTooltip(textRenderer, coolantText, mouseX, mouseY);
        }

        if (UIBlocks.CATA_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            String redstonePart = be.redstone + " / " + Config.MAX_CATALYST;
            List<Text> redstoneText = Arrays.asList(Text.translatable("screen.simplestationsminer.catalysis"),
                    Text.literal(redstonePart));
            dc.drawTooltip(textRenderer, redstoneText, mouseX, mouseY);
        }

        if (be.progress > 0 && UIBlocks.PROGRESS_BAR.isHovered(mouseX - startX, mouseY - startY)) {
            int progressPart = (int) Math.ceil(100 * be.progress / Config.MAX_PROGRESS);
            dc.drawTooltip(textRenderer, Text.literal(progressPart + "%"), mouseX, mouseY);
        }

        if (be.type == null && UIBlocks.FILTER_SLOT.isHovered(mouseX - startX, mouseY - startY)) {
            dc.drawTooltip(textRenderer, Text.translatable("screen.simplestationsminer.filter"), mouseX, mouseY);
        }

        if (be.invalidDepth && UIBlocks.ERROR.isHovered(mouseX - startX, mouseY - startY)) {
            dc.drawTooltip(textRenderer, Text.translatable("screen.simplestationsminer.depthError"), mouseX, mouseY);
        }
    }

    @Override
    public void drawBackground(DrawContext dc, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        dc.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);

        int tickAlpha = 96 + (int) (63 * Math.sin(System.currentTimeMillis() / 400.0));
        int borderColor = (tickAlpha << 24) | 0xFF0000;
        float progressPart = be.progress / Config.MAX_PROGRESS;
        UIBlocks.PROGRESS_BAR.drawProgressToRight(dc, x, y, progressPart, 0xFFCCFEDD);

        if (be.invalidDepth) {
            UIBlocks.ERROR.drawText(dc, textRenderer, x, y, borderColor, "Y > 20");
            return;
        }

        float fuelPart = (float) be.fuel / Config.FUEL_CAPACITY;
        UIBlocks.FUEL_BAR.drawProgressToTop(dc, x, y, fuelPart, 0xAA225522);

        float coolantPart = (float) be.coolant / Config.MAX_COOLANT;
        UIBlocks.COOL_BAR.drawProgressToTop(dc, x, y, coolantPart, 0xAA3333AA);

        float redstonePart = (float) be.redstone / Config.MAX_CATALYST;
        UIBlocks.CATA_BAR.drawProgressToTop(dc, x, y, redstonePart, 0xAABB2211);

        if (fuelPart == 0)
            UIBlocks.FUEL_SLOT.drawBorder(dc, x, y, borderColor);
        if (coolantPart == 0)
            UIBlocks.COOL_SLOT.drawBorder(dc, x, y, borderColor);
        if (redstonePart == 0)
            UIBlocks.CATA_SLOT.drawBorder(dc, x, y, borderColor);
    }
}
