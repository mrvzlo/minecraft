package com.ave.simplestationsminer.uihelpers;

import net.minecraft.client.font.Font;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class Square {
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;
    public final int width;
    public final int height;

    public Square(int x, int y, int w, int h) {
        left = x;
        top = y;
        width = w;
        height = h;
        right = w + x;
        bottom = h + y;
    }

    public boolean isHovered(int x, int y) {
        return x < right && x >= left && y >= top && y < bottom;
    }

    public boolean isHovered(double x, double y) {
        return x < right && x >= left && y >= top && y < bottom;
    }

    public void drawBorder(DrawContext g, int x, int y, int color) {
        x += left;
        y += top;
        g.fill(x, y, x + width, y + 1, color);
        g.fill(x, y + height - 1, x + width, y + height, color);
        g.fill(x, y, x + 1, y + height, color);
        g.fill(x + width - 1, y, x + width, y + height, color);
    }

    public void drawProgressToRight(DrawContext g, int x, int y, float percent, int color) {
        int end = left + (int) Math.round(percent * width);
        g.fill(x + left, y + top, x + end, y + bottom, color);
    }

    public void drawProgressToTop(DrawContext g, int x, int y, float percent, int color) {
        int start = bottom - (int) Math.round(percent * height);
        g.fill(x + left, y + start, x + right, y + bottom, color);
    }

    public void drawText(DrawContext dc, TextRenderer textRenderer, int x, int y, int color, String text) {
        dc.drawText(textRenderer, text, x + left, y + top, color, false);
    }
}
