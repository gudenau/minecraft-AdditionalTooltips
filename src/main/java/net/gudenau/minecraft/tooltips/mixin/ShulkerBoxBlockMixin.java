package net.gudenau.minecraft.tooltips.mixin;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin {
    @Inject(
        method = "appendTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BlockItem;getBlockEntityNbt(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/NbtCompound;"
        ),
        cancellable = true
    )
    private void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, CallbackInfo ci){
        ci.cancel();
    }
}
