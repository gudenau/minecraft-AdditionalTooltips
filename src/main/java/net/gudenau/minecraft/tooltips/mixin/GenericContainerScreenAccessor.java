package net.gudenau.minecraft.tooltips.mixin;

import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GenericContainerScreen.class)
public interface GenericContainerScreenAccessor {
    // Yes this is overkill
    @Accessor("TEXTURE") static Identifier getTexture(){ return new Identifier(""); }
}
