package net.gudenau.minecraft.tooltips.mixin;

import net.gudenau.minecraft.tooltips.duck.ScreenDuck;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends ScreenMixin implements ScreenDuck {
    @Inject(
        method = "renderTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;"
        )
    )
    private void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo ci){
        gud_tooltips$lastStack = stack;
    }
}
