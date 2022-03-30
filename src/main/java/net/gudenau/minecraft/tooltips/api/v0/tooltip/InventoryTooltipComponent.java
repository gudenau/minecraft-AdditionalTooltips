package net.gudenau.minecraft.tooltips.api.v0.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class InventoryTooltipComponent implements ItemTooltipComponent {
    protected final DefaultedList<ItemStack> inventory;
    private final int width;
    private final int height;
    
    public InventoryTooltipComponent(ItemStack stack, int width, int height) {
        this(getBlockEntityInventory(stack, width * height), width, height);
    }
    
    public InventoryTooltipComponent(DefaultedList<ItemStack> inventory, int width, int height) {
        this.inventory = inventory;
        this.width = width;
        this.height = height;
    }
    
    public static DefaultedList<ItemStack> getBlockEntityInventory(ItemStack stack, int size) {
        var inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
        var nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt != null){
            Inventories.readNbt(nbt, inventory);
        }
        return inventory;
    }
    
    @Override
    public int getHeight() {
        return height * 18 + 7;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return width * 18 + 2;
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        drawBackground(matrices, x, y, z);
        
        for(int slotY = 0; slotY < height; slotY++) {
            int indexOffset = slotY * width;
            int renderY = y + slotY * 18;
            for(int slotX = 0; slotX < width; slotX++) {
                int slot = indexOffset + slotX;
                var stack = inventory.get(slot);
                drawStack(textRenderer, itemRenderer, stack, x + slotX * 18, renderY, slot);
            }
        }
    }
    
    protected void drawBackground(MatrixStack matrices, int x, int y, int z) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, Widgets.TEXTURE);
        for(int slotY = 0; slotY < height; slotY++){
            int renderY = y + slotY * 18;
            for(int slotX = 0; slotX < width; slotX++){
                Widgets.SLOT.draw(matrices, x + slotX * 18, renderY, z);
            }
        }
    }
    
    @Override
    public final boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }
}
