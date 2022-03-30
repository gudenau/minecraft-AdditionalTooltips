package net.gudenau.minecraft.tooltips.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.ItemTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.Widgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;

import java.util.stream.IntStream;

public final class CampfireTooltipComponent implements ItemTooltipComponent {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[4];
    private final int[] cookingTotalTimes = new int[4];
    
    public CampfireTooltipComponent(ItemStack stack) {
        var nbt = BlockItem.getBlockEntityNbt(stack);
    
        if(nbt != null){
            Inventories.readNbt(nbt, inventory);
            if (nbt.contains("CookingTimes", NbtElement.INT_ARRAY_TYPE)) {
                System.arraycopy(nbt.getIntArray("CookingTimes"), 0, cookingTimes, 0, 4);
            }
            if (nbt.contains("CookingTotalTimes", NbtElement.INT_ARRAY_TYPE)) {
                System.arraycopy(nbt.getIntArray("CookingTotalTimes"), 0, cookingTotalTimes, 0, 4);
            }
        }
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, Widgets.TEXTURE);
        
        for (int i = 0; i < 4; i++) {
            Widgets.FIRE_BACKGROUND.draw(matrices, x + i * 18 + 2, y, z);
    
            int cookingTime = cookingTimes[i];
            int cookingTotalTime = cookingTotalTimes[i];
            if(cookingTime != 0){
                int progress = cookingTime * 13 / cookingTotalTime;
                Widgets.FIRE.drawInvertedHeight(matrices, x + i * 18 + 2, y, z, progress + 1);
            }
            
            Widgets.SLOT.draw(matrices, x + i * 18, y + 16, z);
        }
        
        for (int i = 0; i < 4; i++) {
            drawStack(textRenderer, itemRenderer, inventory.get(i), x + i * 18, y + 16, i);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty) && IntStream.of(cookingTimes).allMatch((i)->i == 0) && IntStream.of(cookingTotalTimes).allMatch((i)->i == 0);
    }
    
    @Override
    public int getHeight() {
        return 18 + 16 + 5;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 18 * 4;
    }
}
