package pers.gwyog.gtveinlocator;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = GTVeinLocator.MODID,
    version = Tags.VERSION,
    name = "GT Vein Locator",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:gregtech;after:visualprospecting")
public class GTVeinLocator {

    public static final String MODID = "gtveinlocator";

    @SidedProxy(
        clientSide = "pers.gwyog.gtveinlocator.ClientProxy",
        serverSide = "pers.gwyog.gtveinlocator.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.init();
        pers.gwyog.gtveinlocator.network.GTVLNetwork.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModRecipes.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            gtneioreplugin.util.GT5OreLayerHelper.init();
        } catch (Throwable ignored) {}
        ModRecipes.init();
    }
}
