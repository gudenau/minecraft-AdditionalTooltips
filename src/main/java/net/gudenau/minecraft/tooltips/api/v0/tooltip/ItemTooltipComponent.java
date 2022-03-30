package net.gudenau.minecraft.tooltips.api.v0.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

public interface ItemTooltipComponent extends TooltipComponent {
    default void drawStack(TextRenderer textRenderer, ItemRenderer renderer, ItemStack stack, int x, int y, int slot) {
        if(!stack.isEmpty()){
            renderer.renderInGuiWithOverrides(stack, x + 1, y + 1, slot);
            renderer.renderGuiItemOverlay(textRenderer, stack, x + 1, y + 1);
        }
    }
    
    boolean isEmpty();
}
