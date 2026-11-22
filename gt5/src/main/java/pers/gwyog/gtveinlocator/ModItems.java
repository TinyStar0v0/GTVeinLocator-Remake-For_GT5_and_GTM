package pers.gwyog.gtveinlocator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ModItems {

    public static Item veinLocator;
    public static Item advancedVeinLocator;
    public static Item eliteVeinLocator;
    public static Item detectionCoreLV;
    public static Item detectionCoreMV;
    public static Item detectionCoreHV;

    private ModItems() {}

    public static void init() {
        veinLocator = locator("veinLocator", 0, 48);
        advancedVeinLocator = locator("advancedVeinLocator", 1, 80);
        eliteVeinLocator = locator("eliteVeinLocator", 2, 112);

        detectionCoreLV = core("detectionCoreLV");
        detectionCoreMV = core("detectionCoreMV");
        detectionCoreHV = core("detectionCoreHV");
    }

    private static Item locator(String name, int tier, int radius) {
        Item item = new ItemLocator(name, tier, radius);
        item.setCreativeTab(CreativeTabs.tabTools);
        GameRegistry.registerItem(item, name);
        return item;
    }

    private static Item core(String name) {
        Item item = new Item();
        item.setUnlocalizedName(GTVeinLocator.MODID + "." + name);
        item.setTextureName(GTVeinLocator.MODID + ":" + name);
        item.setCreativeTab(CreativeTabs.tabMaterials);
        GameRegistry.registerItem(item, name);
        return item;
    }
}
