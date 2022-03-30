package net.gudenau.minecraft.tooltips.api.v0.tooltip;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public enum Widgets {
    SLOT(0, 0, 18, 18),
    OUTPUT_SLOT(18, 0, 26, 26),
    BLAZE_SLOT(0, 18, 18, 18),
    POTION_SLOT(0, 36, 18, 18),
    FIRE_BACKGROUND(44, 0, 14, 14),
    FIRE(58, 0, 14, 14),
    FURNACE_PROGRESS_BACKGROUND(44, 14, 22, 16),
    FURNACE_PROGRESS(66, 14, 22, 16),
    BLAZE_BACKGROUND(18, 26, 20, 6),
    BLAZE(18, 32, 20, 6),
    POTION_PROGRESS_BACKGROUND(38, 30, 7, 27),
    POTION_PROGRESS(45, 30, 7, 27),
    POTION_BUBBLES_BACKGROUND(52, 30, 11, 28),
    POTION_BUBBLES(63, 30, 11, 28),
    ;
    
    public static final Identifier TEXTURE = new Identifier("gud_tooltips", "textures/gui/tooltip_widgets.png");
    
    public final int u;
    public final int v;
    public final int w;
    public final int h;
    
    Widgets(int u, int v, int w, int h) {
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
    }
    
    public void draw(MatrixStack matrices, int x, int y, int z) {
        DrawableHelper.drawTexture(matrices, x, y, z, u, v, w, h, 256, 256);
    }
    
    public void drawWidth(MatrixStack matrices, int x, int y, int z, int width) {
        DrawableHelper.drawTexture(matrices, x, y, z, u, v, width, h, 256, 256);
    }
    
    public void drawInvertedWidth(MatrixStack matrices, int x, int y, int z, int width) {
        DrawableHelper.drawTexture(matrices, x + w - width, y, z, u + w - width, v, width, h, 256, 256);
    }
    
    public void drawHeight(MatrixStack matrices, int x, int y, int z, int height) {
        DrawableHelper.drawTexture(matrices, x, y, z, u, v, w, height, 256, 256);
    }
    
    public void drawInvertedHeight(MatrixStack matrices, int x, int y, int z, int height) {
        DrawableHelper.drawTexture(matrices, x, y + h - height, z, u, v + h - height, w, height, 256, 256);
    }
}
