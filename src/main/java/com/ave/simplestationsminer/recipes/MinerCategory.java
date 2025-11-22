package com.ave.simplestationsminer.recipes;

import java.util.LinkedList;
import java.util.List;

import com.ave.simplestationsminer.SimpleStationsMiner;
import com.ave.simplestationsminer.registrations.Registrations;
import com.ave.simplestationsminer.uihelpers.UIBlocks;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MinerCategory implements DisplayCategory<BasicDisplay> {
        private static final Identifier TEXTURE = Identifier.of(SimpleStationsMiner.MOD_ID, "textures/gui/jei.png");

        public static CategoryIdentifier<MinerDisplay> REGULAR = CategoryIdentifier.of(SimpleStationsMiner.MOD_ID, "miner");

        @Override
        public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
                return REGULAR;
        }

        @Override
        public Text getTitle() {
                return Text.translatable("screen.simplestationsminer.recipes");
        }

        @Override
        public Renderer getIcon() {
                return EntryStacks.of(Registrations.MINER_BLOCK.asItem().getDefaultStack());
        }

        @Override
        public int getDisplayHeight() {
                return 80;
        }

        @Override
        public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
                Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
                List<Widget> widgets = new LinkedList<>();

                widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 176, 80)));
                widgets.add(Widgets.createSlot(new Point(startPoint.x + UIBlocks.FILTER_SLOT.left, startPoint.y + UIBlocks.FILTER_SLOT.top))
                                .entries(display.getInputEntries().get(0)).markInput());
                widgets.add(Widgets.createSlot(new Point(startPoint.x + UIBlocks.OUT_SLOT.left, startPoint.y + UIBlocks.OUT_SLOT.top))
                                .entries(display.getOutputEntries().get(0)).markOutput());
                return widgets;
        }
}
