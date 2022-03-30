package net.gudenau.minecraft.tooltips.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.ItemTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.Widgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public final class FurnaceTooltipComponent implements ItemTooltipComponent {
    private static final int INPUT = 0;
    private static final int FUEL = 1;
    private static final int OUTPUT = 2;
    
    private final DefaultedList<ItemStack> inventory;
    private final int burnTime;
    private final int cookTime;
    private final int cookTimeTotal;
    private final int fuelTime;
    
    public FurnaceTooltipComponent(ItemStack stack) {
        inventory = InventoryTooltipComponent.getBlockEntityInventory(stack, 3);
        
        var nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt != null){
            burnTime = nbt.getShort("BurnTime");
            cookTime = nbt.getShort("CookTime");
            cookTimeTotal = nbt.getShort("CookTimeTotal");
        }else{
            burnTime = 0;
            cookTime = 0;
            cookTimeTotal = 0;
        }
        // This is vanilla, don't yell at me.
        var fuelTime = FuelRegistry.INSTANCE.get(inventory.get(FUEL).getItem());
        if(fuelTime == 0){
            fuelTime = 200;
        }
        this.fuelTime = fuelTime;
    }
    
    @Override
    public int getHeight() {
        return 59;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 82;
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, Widgets.TEXTURE);
        
        Widgets.SLOT.draw(matrices, x, y, z);
        Widgets.SLOT.draw(matrices, x, y + 36, z);
        Widgets.OUTPUT_SLOT.draw(matrices, x + 56, y + 14, z);
        
        Widgets.FIRE_BACKGROUND.draw(matrices, x + 2, y + 20, z);
        if(burnTime != 0){
            int progress = burnTime * 13 / fuelTime;
            Widgets.FIRE.drawInvertedHeight(matrices, x + 2, y, z, progress + 1);
        }
        
        Widgets.FURNACE_PROGRESS_BACKGROUND.draw(matrices, x + 25, y + 18, z);
        if(cookTimeTotal != 0 && cookTime != 0){
            Widgets.FURNACE_PROGRESS.drawWidth(matrices, x + 25, y + 18, z, cookTime * 24 / cookTimeTotal + 1);
        }
        
        drawStack(textRenderer, itemRenderer, inventory.get(INPUT), x, y, INPUT);
        drawStack(textRenderer, itemRenderer, inventory.get(OUTPUT), x + 60, y + 18, OUTPUT);
        drawStack(textRenderer, itemRenderer, inventory.get(FUEL), x, y + 36, FUEL);
    }
    
    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty) && burnTime == 0 && cookTime == 0 && cookTimeTotal == 0;
    }
}
