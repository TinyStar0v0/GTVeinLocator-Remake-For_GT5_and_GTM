package com.gtveinlocator;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GTVeinLocator.MODID)
public class GTVeinLocator {

    public static final String MODID = "gtveinlocator";

    public GTVeinLocator() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(bus);
        ModCreativeTabs.register(bus);
        com.gtveinlocator.network.VPNetwork.register();
    }
}
