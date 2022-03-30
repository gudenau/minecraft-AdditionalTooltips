package net.gudenau.minecraft.tooltips.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.ItemTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.Widgets;
import net.gudenau.minecraft.tooltips.mixin.BrewingStandScreenAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

public final class BrewingStandTooltipComponent implements ItemTooltipComponent {
    private static final int INPUT = 3;
    private static final int BLAZE = 4;
    
    private final DefaultedList<ItemStack> inventory;
    private final int progress;
    private final int fuel;
    
    public BrewingStandTooltipComponent(ItemStack stack) {
        inventory = InventoryTooltipComponent.getBlockEntityInventory(stack, 5);
        
        var nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt != null){
            progress = nbt.getShort("BrewTime");
            fuel = nbt.getByte("Fuel");
        }else{
            progress = 0;
            fuel = 0;
        }
    }
    
    @Override
    public int getHeight() {
        return 61 + 5;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 79;
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, Widgets.TEXTURE);
    
        Widgets.POTION_SLOT.draw(matrices, x + 15, y + 36, z);
        Widgets.POTION_SLOT.draw(matrices, x + 38, y + 43, z);
        Widgets.POTION_SLOT.draw(matrices, x + 61, y + 36, z);
        Widgets.BLAZE_SLOT.draw(matrices, x, y + 2, z);
        Widgets.SLOT.draw(matrices, x + 38, y + 2, z);
        
        Widgets.BLAZE_BACKGROUND.draw(matrices, x + 19, y + 29, z);
        if(fuel != 0){
            Widgets.BLAZE.drawWidth(matrices, x + 19, y + 29, z, MathHelper.clamp((18 * fuel + 20 - 1) / 20, 0, 18));
        }
        
        Widgets.POTION_PROGRESS_BACKGROUND.draw(matrices, x + 58, y + 2, z);
        Widgets.POTION_BUBBLES_BACKGROUND.draw(matrices, x + 23, y, z);
        if(progress != 0){
            int size = (int) (28 * (1 - progress / 400F));
            if(size > 0){
                Widgets.POTION_PROGRESS.drawHeight(matrices, x + 58, y + 2, z, size);
            }
            size = BrewingStandScreenAccessor.getBubbleProgress()[progress / 2 % 7];
            if(size > 0){
                Widgets.POTION_BUBBLES.drawInvertedHeight(matrices, x + 23, y, z, size);
            }
        }
        
        /*
        if ((m = ((BrewingStandScreenHandler)this.handler).getBrewTime()) > 0) {
            int n = (int)(28.0f * (1.0f - (float)m / 400.0f));
            if (n > 0) {
                this.drawTexture(matrices, i + 97, j + 16, 176, 0, 9, n);
            }
            if ((n = BUBBLE_PROGRESS[m / 2 % 7]) > 0) {
                this.drawTexture(matrices, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
            }
        }
         */
        
        drawStack(textRenderer, itemRenderer, inventory.get(0), x + 15, y + 36, 0);
        drawStack(textRenderer, itemRenderer, inventory.get(1), x + 38, y + 43, 0);
        drawStack(textRenderer, itemRenderer, inventory.get(2), x + 61, y + 36, 0);
        drawStack(textRenderer, itemRenderer, inventory.get(BLAZE), x, y + 2, 0);
        drawStack(textRenderer, itemRenderer, inventory.get(INPUT), x + 38, y + 2, 0);
    }
    
    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty) && progress == 0 && fuel == 0;
    }
}
