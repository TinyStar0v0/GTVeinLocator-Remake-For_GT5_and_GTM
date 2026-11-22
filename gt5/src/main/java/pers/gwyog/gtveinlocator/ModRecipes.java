package pers.gwyog.gtveinlocator;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;

public final class ModRecipes {

    private static boolean initialized;

    private ModRecipes() {}

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            System.out.println("[GTVL] ModRecipes.init start");
            core(ModItems.detectionCoreLV, ItemList.Sensor_LV, ItemList.Emitter_LV, ItemList.Circuit_Basic, 30);
            core(ModItems.detectionCoreMV, ItemList.Sensor_MV, ItemList.Emitter_MV, ItemList.Circuit_Good, 120);
            core(ModItems.detectionCoreHV, ItemList.Sensor_HV, ItemList.Emitter_HV, ItemList.Circuit_Advanced, 480);

            locator(
                ModItems.veinLocator,
                ModItems.detectionCoreLV,
                Materials.Steel,
                Materials.Tin,
                OrePrefixes.cableGt01,
                ItemList.Circuit_Basic,
                30);
            locator(
                ModItems.advancedVeinLocator,
                ModItems.detectionCoreMV,
                Materials.Aluminium,
                Materials.Copper,
                OrePrefixes.cableGt02,
                ItemList.Circuit_Good,
                120);
            locator(
                ModItems.eliteVeinLocator,
                ModItems.detectionCoreHV,
                Materials.StainlessSteel,
                Materials.Silver,
                OrePrefixes.cableGt04,
                ItemList.Circuit_Advanced,
                480);
        } catch (Throwable throwable) {
            System.out.println("[GTVL] recipe init failed: " + throwable);
            throwable.printStackTrace();
        }
    }

    private static void core(Item core, ItemList sensor, ItemList emitter, ItemList circuit, int eut) {
        ItemStack quartz = GTOreDictUnificator.get(OrePrefixes.gem, Materials.Quartz, 1L);
        if (quartz == null) {
            quartz = new ItemStack(Items.quartz);
        }
        System.out.println("[GTVL] core " + core.getUnlocalizedName() + " quartz=" + quartz);
        GTRecipeBuilder builder = GTRecipeBuilder.builder()
            .itemInputs(
                sensor.get(2L),
                emitter.get(2L),
                circuit.get(1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 3L),
                quartz)
            .itemOutputs(new ItemStack(core))
            .duration(200)
            .eut(eut);
        System.out.println(
            "[GTVL] core recipes: " + builder.addTo(RecipeMaps.assemblerRecipes)
                .size());
    }

    private static void locator(Item locator, Item core, Materials plate, Materials cable, OrePrefixes cablePrefix,
        ItemList circuit, int eut) {
        FluidStack tin = FluidRegistry.getFluidStack("molten.tin", 144);
        if (tin == null) {
            tin = FluidRegistry.getFluidStack("tin", 144);
        }
        System.out.println("[GTVL] locator " + locator.getUnlocalizedName() + " tin=" + tin);
        GTRecipeBuilder builder = GTRecipeBuilder.builder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, plate, 4L),
                GTOreDictUnificator.get(cablePrefix, cable, 3L),
                circuit.get(1L),
                new ItemStack(core))
            .fluidInputs(tin)
            .itemOutputs(new ItemStack(locator))
            .duration(200)
            .eut(eut);
        System.out.println(
            "[GTVL] locator recipes: " + builder.addTo(RecipeMaps.assemblerRecipes)
                .size());
    }
}
