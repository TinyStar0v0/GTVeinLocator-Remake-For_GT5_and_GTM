package com.gtveinlocator;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GTVeinLocator.MODID);

    public static final RegistryObject<Item> VEIN_LOCATOR =
            ITEMS.register("vein_locator", () -> new ItemLocator(LocatorTier.LV));
    public static final RegistryObject<Item> ADVANCED_VEIN_LOCATOR =
            ITEMS.register("advanced_vein_locator", () -> new ItemLocator(LocatorTier.MV));
    public static final RegistryObject<Item> ELITE_VEIN_LOCATOR =
            ITEMS.register("elite_vein_locator", () -> new ItemLocator(LocatorTier.HV));

    public static final RegistryObject<Item> DETECTION_CORE_LV =
            ITEMS.register("detection_core_lv", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DETECTION_CORE_MV =
            ITEMS.register("detection_core_mv", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DETECTION_CORE_HV =
            ITEMS.register("detection_core_hv", () -> new Item(new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
