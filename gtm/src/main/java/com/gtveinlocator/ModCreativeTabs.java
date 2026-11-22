package com.gtveinlocator;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GTVeinLocator.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.gtveinlocator"))
            .icon(() -> new ItemStack(ModItems.DETECTION_CORE_LV.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.VEIN_LOCATOR.get());
                output.accept(ModItems.ADVANCED_VEIN_LOCATOR.get());
                output.accept(ModItems.ELITE_VEIN_LOCATOR.get());
                output.accept(ModItems.DETECTION_CORE_LV.get());
                output.accept(ModItems.DETECTION_CORE_MV.get());
                output.accept(ModItems.DETECTION_CORE_HV.get());
            })
            .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
