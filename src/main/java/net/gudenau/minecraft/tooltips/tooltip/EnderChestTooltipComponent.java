package net.gudenau.minecraft.tooltips.tooltip;

import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public final class EnderChestTooltipComponent extends InventoryTooltipComponent {
    public EnderChestTooltipComponent() {
        super(copy(MinecraftClient.getInstance().player.getEnderChestInventory()), 9, 3);
    }
    
    private static DefaultedList<ItemStack> copy(EnderChestInventory enderChestInventory) {
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        for(int i = 0; i < 27; i++){
            inventory.set(i, enderChestInventory.getStack(i));
        }
        return inventory;
    }
}
