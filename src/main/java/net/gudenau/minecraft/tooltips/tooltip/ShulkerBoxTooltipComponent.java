package net.gudenau.minecraft.tooltips.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.gudenau.minecraft.tooltips.mixin.GenericContainerScreenAccessor;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

public final class ShulkerBoxTooltipComponent extends InventoryTooltipComponent implements TooltipComponent {
    private final DyeColor color;
    
    public ShulkerBoxTooltipComponent(ItemStack stack) {
        super(stack, 9, 3);
        this.color = ((ShulkerBoxBlock)((BlockItem)stack.getItem()).getBlock()).getColor();
    }
    
    @Override
    protected void drawBackground(MatrixStack matrices, int x, int y, int z) {
        var texture = GenericContainerScreenAccessor.getTexture();
        if(color == null) {
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }else{
            var components = color.getColorComponents();
            RenderSystem.setShaderColor(components[0], components[1], components[2], 1F);
        }
        RenderSystem.setShaderTexture(0, texture);
        DrawableHelper.drawTexture(matrices, x, y, z, 7, 17, 162, 54, 256, 256);
    }
}
