package net.gudenau.minecraft.tooltips.api.v0;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.gudenau.minecraft.tooltips.ItemComponentSupport;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.Optional;
import java.util.function.Function;

public interface CcaComponentRegistry {
    //<C extends ItemComponent> void register(Predicate<Item> test, ComponentKey<? super C> type, ComponentFactory<ItemStack, C> factory);
    static <T extends ItemComponent> void register(ComponentKey<? super T> type, Factory<T> factory){
        ItemComponentSupport.register(type, factory);
    }
    
    @FunctionalInterface
    interface Factory<T extends ItemComponent>{
        Optional<TooltipComponent> createComponent(T component);
    }
}
