package net.gudenau.minecraft.tooltips.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DyeColor;

import java.util.List;

import static net.minecraft.client.render.TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE;

public final class BannerTooltipComponent implements TooltipComponent {
    private final List<Pair<BannerPattern, DyeColor>> patterns;
    
    public BannerTooltipComponent(ItemStack stack) {
        DyeColor base = null;
        var item = stack.getItem();
        if (item instanceof BannerItem banner) {
            base = banner.getColor();
        } else if (item instanceof ShieldItem) {
            base = ShieldItem.getColor(stack);
        }
        if (base == null) {
            base = DyeColor.WHITE;
        }
        
        patterns = BannerBlockEntity.getPatternsFromNbt(base, BannerBlockEntity.getPatternListNbt(stack));
    }
    
    @Override
    public int getHeight() {
        return patterns.isEmpty() ? 0 : 100;
    }
    
    @Override
    public int getWidth(TextRenderer textRenderer) {
        return patterns.size() * 100;
    }
    
    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderTexture(0, BANNER_PATTERNS_ATLAS_TEXTURE);
        
        DiffuseLighting.disableGuiDepthLighting();
        matrices.push();
        matrices.translate(x, y, z);
        matrices.push();
        matrices.translate(0.5, 16, 0);
        matrices.scale(6, -6, 1);
        matrices.scale(2, -2, 2);
        
        var client = MinecraftClient.getInstance();
        var immediate = client.getBufferBuilders().getEntityVertexConsumers();
        var flag = client.getEntityModelLoader().getModelPart(EntityModelLayers.BANNER).getChild("flag");
        flag.pitch = 0;
        flag.pivotY = -32;
        BannerBlockEntityRenderer.renderCanvas(matrices, immediate, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, flag, ModelLoader.BANNER_BASE, true, patterns);
        matrices.pop();
        immediate.draw();
        matrices.pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
