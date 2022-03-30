package net.gudenau.minecraft.tooltips.tooltip;

import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Matrix4f;

import static net.minecraft.item.WrittenBookItem.AUTHOR_KEY;
import static net.minecraft.item.WrittenBookItem.GENERATION_KEY;

public final class LecternTooltipComponent extends InventoryTooltipComponent {
    public LecternTooltipComponent(ItemStack stack) {
        super(DefaultedList.copyOf(ItemStack.EMPTY, getBook(stack)), 1, 1);
    }
    
    private static ItemStack getBook(ItemStack stack) {
        var tag = BlockItem.getBlockEntityNbt(stack);
        if(tag != null && tag.contains("Book", NbtElement.COMPOUND_TYPE)){
            return ItemStack.fromNbt(tag.getCompound("Book"));
        }else{
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public int getHeight() {
        return super.getHeight() + 30;
    }
    
    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        var stack = inventory.get(0);
        
        MutableText name;
        MutableText author;
        MutableText generation;
        
        if(stack.getItem() instanceof WrittenBookItem book) {
            var nbt = stack.getNbt();
            
            if(nbt != null) {
                name = book.getName(stack).copy();
                var authorString = nbt.getString(AUTHOR_KEY);
                if(!authorString.isBlank()) {
                    author = new TranslatableText("book.byAuthor", authorString);
                }else{
                    author = new LiteralText("Anonymous").setStyle(Style.EMPTY.withItalic(true));
                }
                generation = new TranslatableText("book.generation." + nbt.getInt(GENERATION_KEY));
            }else{
                name = new LiteralText("Unknown Title").setStyle(Style.EMPTY.withItalic(true));
                author = new LiteralText("Unknown Author").setStyle(Style.EMPTY.withItalic(true));
                generation = new LiteralText("Unknown Generation").setStyle(Style.EMPTY.withItalic(true));
            }
        }else{
            name = new LiteralText("Unknown Title").setStyle(Style.EMPTY.withItalic(true));
            author = new LiteralText("Unknown Author").setStyle(Style.EMPTY.withItalic(true));
            generation = new LiteralText("Unknown Generation").setStyle(Style.EMPTY.withItalic(true));
        }
        
        textRenderer.draw(name.formatted(Formatting.GRAY).asOrderedText(), x, y + 21, 0xFFFFFFFF, true, matrix, vertexConsumers, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        textRenderer.draw(author.formatted(Formatting.GRAY).asOrderedText(), x, y + 31, 0xFFFFFFFF, true, matrix, vertexConsumers, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        textRenderer.draw(generation.formatted(Formatting.GRAY).asOrderedText(), x, y + 41, 0xFFFFFFFF, true, matrix, vertexConsumers, false, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
    }
}
