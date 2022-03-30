package net.gudenau.minecraft.tooltips;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.gudenau.minecraft.tooltips.api.v0.TooltipRenderEvent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.InventoryTooltipComponent;
import net.gudenau.minecraft.tooltips.api.v0.tooltip.ItemTooltipComponent;
import net.gudenau.minecraft.tooltips.tooltip.*;
import net.minecraft.block.*;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

// Just in case
@Environment(EnvType.CLIENT)
public final class AdditionalTooltips implements ModInitializer {
    @SuppressWarnings({"TrivialFunctionalExpressionUsage", "Convert2MethodRef"})
    @Override
    public void onInitialize() {
        TooltipRenderEvent.EVENT.register((stack, context)->{
            List<TooltipComponent> components = new ArrayList<>();
            var item = stack.getItem();
            if(context.isAdvanced()){
                getAdvancedTooltip(stack, item, components, context);
            }else{
                if(stack.getItem() instanceof BlockItem blockItem){
                    getBlockTooltip(stack, blockItem.getBlock(), components, context);
                }
                getItemTooltip(stack, item, components, context);
            }
            return components;
        });
        
        if(FabricLoader.getInstance().isModLoaded("cardinal-components-item")){
            // This makes it harder for the JVM to accidentally load the CCA class
            ((Runnable)()->ItemComponentSupport.init()).run();
        }
    }
    
    private void getBlockTooltip(ItemStack stack, Block block, List<TooltipComponent> components, TooltipContext context) {
        if(block instanceof EnderChestBlock){
            var component = new EnderChestTooltipComponent();
            if(!component.isEmpty()){
                components.add(component);
            }
            return;
        }
        
        if(BlockItem.getBlockEntityNbt(stack) == null){
            return;
        }
        
        ItemTooltipComponent component = null;
        
        if(block instanceof ShulkerBoxBlock){
            component = new ShulkerBoxTooltipComponent(stack);
        }else if(block instanceof BarrelBlock || block instanceof AbstractChestBlock){
            component = new InventoryTooltipComponent(stack, 9, 3);
        }else if(block instanceof AbstractFurnaceBlock){
            component = new FurnaceTooltipComponent(stack);
        }else if(block instanceof BrewingStandBlock){
            component = new BrewingStandTooltipComponent(stack);
        }else if(block instanceof CampfireBlock){
            component = new CampfireTooltipComponent(stack);
        }else if(block instanceof DispenserBlock){
            component = new InventoryTooltipComponent(stack, 3, 3);
        }else if(block instanceof HopperBlock){
            component = new InventoryTooltipComponent(stack, 5, 1);
        }else if(block instanceof JukeboxBlock){
            component = new JukeboxTooltipComponent(stack);
        }else if(block instanceof LecternBlock){
            component = new LecternTooltipComponent(stack);
        }
        
        if(component != null && !component.isEmpty()){
            components.add(component);
        }
    }
    
    private void getItemTooltip(ItemStack stack, Item item, List<TooltipComponent> components, TooltipContext context) {
        if(item instanceof BannerItem || item instanceof ShieldItem){
            components.add(new BannerTooltipComponent(stack));
        }else if(item instanceof FilledMapItem){
            components.add(new MapTooltipComponent(stack));
        }
    }
    
    private void getAdvancedTooltip(ItemStack stack, Item item, List<TooltipComponent> components, TooltipContext context) {
        var id = Registry.ITEM.getId(item);
        var namespace = id.getNamespace();
        components.add(TooltipComponent.of(FabricLoader.getInstance().getModContainer(namespace)
            .map((container)->new LiteralText(container.getMetadata().getName()))
            .orElseGet(()->new LiteralText("Unknown Mod"))
            .formatted(Formatting.DARK_GRAY).asOrderedText())
        );
    }
}
