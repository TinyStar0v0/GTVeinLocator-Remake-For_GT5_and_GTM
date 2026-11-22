package com.gtveinlocator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@GTAddon
public final class GTVLAddon implements IGTAddon {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(GTVeinLocator.MODID);
    private static final TagKey<Item> ANY_QUARTZ = TagKey.create(Registries.ITEM,
            new ResourceLocation(GTVeinLocator.MODID, "any_quartz"));
    private static final TagKey<Item> LV_CIRCUIT = circuit("lv");
    private static final TagKey<Item> MV_CIRCUIT = circuit("mv");
    private static final TagKey<Item> HV_CIRCUIT = circuit("hv");

    @Override
    public GTRegistrate getRegistrate() {
        return REGISTRATE;
    }

    @Override
    public void initializeAddon() {
    }

    @Override
    public String addonModId() {
        return GTVeinLocator.MODID;
    }

    private static TagKey<Item> circuit(String tier) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("gtceu", "circuits/" + tier));
    }

    @Override
    public void addRecipes(@NotNull Consumer<FinishedRecipe> provider) {
        assemblerCore(provider, "detection_core_lv", ModItems.DETECTION_CORE_LV.get(),
                GTItems.SENSOR_LV.get(), GTItems.EMITTER_LV.get(), LV_CIRCUIT, 30);
        assemblerCore(provider, "detection_core_mv", ModItems.DETECTION_CORE_MV.get(),
                GTItems.SENSOR_MV.get(), GTItems.EMITTER_MV.get(), MV_CIRCUIT, 120);
        assemblerCore(provider, "detection_core_hv", ModItems.DETECTION_CORE_HV.get(),
                GTItems.SENSOR_HV.get(), GTItems.EMITTER_HV.get(), HV_CIRCUIT, 480);

        locator(provider, "vein_locator", ModItems.VEIN_LOCATOR.get(), ModItems.DETECTION_CORE_LV.get(),
                null, TagPrefix.plate, GTMaterials.Steel, TagPrefix.cableGtSingle, GTMaterials.Tin,
                LV_CIRCUIT, 30, 200);
        locator(provider, "advanced_vein_locator", ModItems.ADVANCED_VEIN_LOCATOR.get(),
                ModItems.DETECTION_CORE_MV.get(), null, TagPrefix.plate, GTMaterials.Aluminium,
                TagPrefix.cableGtDouble, GTMaterials.Copper, MV_CIRCUIT, 120, 300);
        locator(provider, "elite_vein_locator", ModItems.ELITE_VEIN_LOCATOR.get(),
                ModItems.DETECTION_CORE_HV.get(), GTItems.SENSOR_HV.get(), TagPrefix.plate,
                GTMaterials.StainlessSteel, TagPrefix.cableGtQuadruple, GTMaterials.Silver,
                HV_CIRCUIT, 480, 400);
    }

    private void assemblerCore(Consumer<FinishedRecipe> output, String name, Item core, Item sensor,
                               Item emitter, TagKey<Item> circuit, long eut) {
        GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(name)
                .inputItems(new ItemStack(sensor, 2))
                .inputItems(new ItemStack(emitter, 2))
                .inputItems(circuit, 1)
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 3)
                .inputItems(ANY_QUARTZ, 1)
                .outputItems(new ItemStack(core))
                .EUt(eut)
                .duration(200)
                .save(output);
    }

    private void locator(Consumer<FinishedRecipe> output, String name, Item locator, Item core,
                         Item extraSensor, TagPrefix platePrefix, Material plate,
                         TagPrefix cablePrefix, Material cable, TagKey<Item> circuit, long eut,
                         int duration) {
        var builder = GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(name)
                .inputItems(platePrefix, plate, 4)
                .inputItems(cablePrefix, cable, 3)
                .inputItems(circuit, 1)
                .inputItems(new ItemStack(core, 1));
        if (extraSensor != null) {
            builder.inputItems(new ItemStack(extraSensor, 1));
        }
        builder.inputFluids(GTMaterials.Tin.getFluid(144))
                .outputItems(new ItemStack(locator))
                .EUt(eut)
                .duration(duration)
                .save(output);
    }
}
