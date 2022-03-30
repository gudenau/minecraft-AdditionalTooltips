package net.gudenau.minecraft.tooltips;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.gudenau.minecraft.tooltips.api.v0.CcaComponentRegistry;
import net.gudenau.minecraft.tooltips.api.v0.TooltipRenderEvent;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemComponentSupport {
    static {
        if(!FabricLoader.getInstance().isModLoaded("cardinal-components-item")){
            throw new AssertionError("Can not load Cardinal Components support classes without the Cardinal Components Item module present");
        }
    }
    
    private static final Map<ComponentKey<?>, Set<CcaComponentRegistry.Factory<?>>> REGISTRY = new ConcurrentHashMap<>();
    
    public static void init() {
        TooltipRenderEvent.EVENT.register((stack, context)->{
            List<TooltipComponent> components = new ArrayList<>();
            
            for (var entry : REGISTRY.entrySet()) {
                var component = entry.getKey().getNullable(stack);
                if(!(component instanceof ItemComponent itemComponent)){
                    continue;
                }
    
                for (var factory : entry.getValue()) {
                    //noinspection unchecked
                    ((CcaComponentRegistry.Factory<ItemComponent>)factory).createComponent(itemComponent)
                        .ifPresent(components::add);
                }
            }
            
            return components;
        });
    }
    
    public static <T extends ItemComponent> void register(ComponentKey<? super T> type, CcaComponentRegistry.Factory<T> factory) {
        Objects.requireNonNull(type, "type can not be null");
        Objects.requireNonNull(factory, "factory can not be null");
        
        REGISTRY.computeIfAbsent(type, (key)-> Collections.synchronizedSet(new HashSet<>())).add(factory);
    }
}
