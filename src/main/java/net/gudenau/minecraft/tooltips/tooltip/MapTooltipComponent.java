package net.gudenau.minecraft.tooltips.tooltip;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

public final class MapTooltipComponent implements TooltipComponent {
    private final Integer mapId;
    private final MapState mapState;
    
    public MapTooltipComponent(ItemStack stack) {
        mapId = FilledMapItem.getMapId(stack);
        mapState = FilledMapItem.getMapState(FilledMapItem.getMapId(stack), MinecraftClient.getInstance().world);
    }
    
    @Override
    public int getHeight() {
        return mapState == null ? 0 : 128;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return mapState == null ? 0 : 128;
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        if(mapState == null){
            return;
        }
        
        //this.client.gameRenderer.getMapRenderer().draw(matrices, immediate, mapId, mapState, true, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        matrices.push();
        matrices.translate(x, y, z);
        var immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        MinecraftClient.getInstance().gameRenderer.getMapRenderer().draw(matrices, immediate, mapId, mapState, true, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        immediate.draw();
        matrices.pop();
    }
}
