package net.gudenau.minecraft.tooltips.tooltip;

import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Matrix4f;

public final class JukeboxTooltipComponent extends InventoryTooltipComponent {
    public JukeboxTooltipComponent(ItemStack stack) {
        super(DefaultedList.copyOf(ItemStack.EMPTY, getRecord(stack)), 1, 1);
    }
    
    private static ItemStack getRecord(ItemStack stack) {
        var tag = BlockItem.getBlockEntityNbt(stack);
        if(tag != null && tag.contains("RecordItem", NbtElement.COMPOUND_TYPE)){
            return ItemStack.fromNbt(tag.getCompound("RecordItem"));
        }else{
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public int getHeight() {
        return super.getHeight() + 10;
    }
    
    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        var stack = inventory.get(0);
        MutableText title;
        if(stack.getItem() instanceof MusicDiscItem musicDisc) {
            title = musicDisc.getDescription();
        }else{
            title = new LiteralText("Unknown");
        }
        textRenderer.draw(title.formatted(Formatting.GRAY).asOrderedText(), x, y + 21, 0xFFFFFFFF, true, matrix, vertexConsumers, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
    }
}
