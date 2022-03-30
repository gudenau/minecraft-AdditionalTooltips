package net.gudenau.minecraft.tooltips.mixin;

import net.gudenau.minecraft.tooltips.api.v0.TooltipRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement {
    @Shadow @Nullable protected MinecraftClient client;
    
    @Shadow public abstract void init(MinecraftClient client, int width, int height);
    
    @Unique protected ItemStack gud_tooltips$lastStack;
    
    @Inject(
        method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V",
        at = @At("HEAD")
    )
    private void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci){
        gud_tooltips$lastStack = stack;
    }
    
    @Inject(
        method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;Ljava/util/Optional;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void renderTooltip(MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, int x, int y, CallbackInfo ci, List<TooltipComponent> components){
        if(gud_tooltips$lastStack != null) {
            var additionalComponents = TooltipRenderEvent.EVENT.invoker().onTooltipRender(gud_tooltips$lastStack, TooltipContext.Default.NORMAL);
            if(!additionalComponents.isEmpty()){
                // selectedTab == ItemGroup.SEARCH.getIndex()
                if(((Object)this) instanceof CreativeInventoryScreen creativeScreen) {
                    components.addAll(creativeScreen.getSelectedTab() == ItemGroup.SEARCH.getIndex() ? 2 : 1, additionalComponents);
                }else{
                    components.addAll(1, additionalComponents);
                }
            }
            if(!client.options.advancedItemTooltips){
                return;
            }
            additionalComponents = TooltipRenderEvent.EVENT.invoker().onTooltipRender(gud_tooltips$lastStack, TooltipContext.Default.ADVANCED);
            if(!additionalComponents.isEmpty()){
                components.addAll(additionalComponents);
            }
        }
    }
}
