package net.gudenau.minecraft.tooltips.api.v0;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface TooltipRenderEvent {
    Event<TooltipRenderEvent> EVENT = EventFactory.createArrayBacked(TooltipRenderEvent.class, (listeners)->(stack, context)->{
        List<TooltipComponent> components = new ArrayList<>();
        for (TooltipRenderEvent listener : listeners) {
            components.addAll(listener.onTooltipRender(stack, context));
        }
        return components;
    });
    
    List<TooltipComponent> onTooltipRender(ItemStack stack, TooltipContext context);
}
