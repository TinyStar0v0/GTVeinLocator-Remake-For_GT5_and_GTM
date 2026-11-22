package com.gtveinlocator;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OreGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class VeinQuery {

    private static final OreGenerator GENERATOR = new OreGenerator();

    private VeinQuery() {
    }

    public static CompoundTag build(ServerLevel level, ServerPlayer player, LocatorTier tier) {
        ChunkPos playerChunk = player.chunkPosition();
        double maxDistance = tier.radiusChunks * 16.0;
        Map<Long, List<GeneratedVeinMetadata>> grouped = new LinkedHashMap<>();

        int minX = Math.floorDiv(playerChunk.x - tier.radiusChunks, 3) * 3;
        int maxX = Math.floorDiv(playerChunk.x + tier.radiusChunks, 3) * 3;
        int minZ = Math.floorDiv(playerChunk.z - tier.radiusChunks, 3) * 3;
        int maxZ = Math.floorDiv(playerChunk.z + tier.radiusChunks, 3) * 3;

        for (int cx = minX; cx <= maxX; cx += 3) {
            for (int cz = minZ; cz <= maxZ; cz += 3) {
                try {
                    List<GeneratedVeinMetadata> found = GENERATOR.generateMetadata(
                            level, level.getChunkSource().getGenerator(), new ChunkPos(cx, cz));
                    for (GeneratedVeinMetadata meta : found) {
                        grouped.computeIfAbsent(meta.originChunk().toLong(), ignored -> new ArrayList<>())
                                .add(meta);
                    }
                } catch (Throwable ignored) {
                }
            }
        }

        List<OriginVeins> veins = new ArrayList<>();
        for (List<GeneratedVeinMetadata> layers : grouped.values()) {
            GeneratedVeinMetadata primary = layers.get(0);
            double dx = primary.center().getX() - player.getX();
            double dz = primary.center().getZ() - player.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance <= maxDistance) {
                veins.add(new OriginVeins(primary.originChunk(), layers, primary.center(), distance));
            }
        }
        veins.sort(Comparator.comparingDouble(OriginVeins::distance));
        VisualVeinMarking.mark(level, veins.stream().map(OriginVeins::origin).toList());

        CompoundTag result = new CompoundTag();
        ListTag oreList = new ListTag();
        int shown = Math.min(tier.maxVeins, veins.size());
        for (int i = 0; i < shown; i++) {
            OriginVeins vein = veins.get(i);
            CompoundTag entry = new CompoundTag();
            entry.putString("id", vein.layers().get(0).id().toString());
            entry.putInt("ox", vein.origin().x);
            entry.putInt("oz", vein.origin().z);
            entry.putInt("x", vein.center().getX());
            entry.putInt("y", vein.center().getY());
            entry.putInt("z", vein.center().getZ());
            ListTag layers = new ListTag();
            for (GeneratedVeinMetadata layer : vein.layers()) {
                layers.add(StringTag.valueOf(layer.definition().layer().getSerializedName()));
            }
            entry.put("layers", layers);
            oreList.add(entry);
        }
        result.put("ores", oreList);
        result.putInt("max", tier.maxVeins);
        result.putInt("radius", tier.radiusChunks);

        if (tier.fluid) {
            VisualVeinMarking.markFluid(level, player);
            CompoundTag fluid = buildFluid(level, player);
            if (fluid != null) {
                result.put("fluid", fluid);
            }
        }
        return result;
    }

    public static void print(ServerPlayer player, CompoundTag result) {
        ListTag ores = result.getList("ores", Tag.TAG_COMPOUND);
        player.sendSystemMessage(Component.translatable("msg.gtveinlocator.header",
                ores.size(), result.getInt("radius")).withStyle(ChatFormatting.GOLD));
        for (int i = 0; i < ores.size(); i++) {
            CompoundTag ore = ores.getCompound(i);
            String id = ore.getString("id");
            String name = pretty(id);
            StringBuilder layers = new StringBuilder();
            ListTag layerList = ore.getList("layers", Tag.TAG_STRING);
            for (int j = 0; j < layerList.size(); j++) {
                if (j > 0) {
                    layers.append(", ");
                }
                layers.append(layerList.getString(j));
            }
            player.sendSystemMessage(Component.literal((i + 1) + ". " + name)
                    .withStyle(ChatFormatting.WHITE)
                    .append(Component.literal("  [" + layers + "]").withStyle(ChatFormatting.GRAY)));
            player.sendSystemMessage(Component.literal("   center: " + ore.getInt("x") + ", "
                            + ore.getInt("y") + ", " + ore.getInt("z"))
                    .withStyle(ChatFormatting.DARK_AQUA)
                    .append(Component.literal("   origin chunk: "
                            + ore.getInt("ox") + ", " + ore.getInt("oz")).withStyle(ChatFormatting.DARK_GRAY)));
        }
        if (ores.isEmpty()) {
            player.sendSystemMessage(Component.translatable("msg.gtveinlocator.none")
                    .withStyle(ChatFormatting.GRAY));
        }
        if (result.contains("fluid")) {
            CompoundTag fluid = result.getCompound("fluid");
            player.sendSystemMessage(Component.translatable("msg.gtveinlocator.fluid_header",
                    fluid.getString("name"), fluid.getInt("rx"), fluid.getInt("rz"))
                    .withStyle(ChatFormatting.AQUA));
            player.sendSystemMessage(Component.translatable("msg.gtveinlocator.fluid_stats",
                    fluid.getInt("chunks"), fluid.getInt("yield"), fluid.getInt("operations"),
                    fluid.getInt("depleted")).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    private static CompoundTag buildFluid(ServerLevel level, ServerPlayer player) {
        ChunkPos chunk = player.chunkPosition();
        int regionX = Math.floorDiv(chunk.x, 8);
        int regionZ = Math.floorDiv(chunk.z, 8);
        BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(level);
        Fluid fluid = null;
        int totalYield = 0;
        int totalOperations = 0;
        int chunks = 0;
        int depleted = 0;
        for (int dx = 0; dx < 8; dx++) {
            for (int dz = 0; dz < 8; dz++) {
                FluidVeinWorldEntry entry = data.getFluidVeinWorldEntry(
                        regionX * 8 + dx, regionZ * 8 + dz);
                if (entry.getDefinition() == null) {
                    continue;
                }
                chunks++;
                totalYield += entry.getFluidYield();
                totalOperations += entry.getOperationsRemaining();
                if (entry.getOperationsRemaining() <= 0) {
                    depleted++;
                }
                if (fluid == null) {
                    fluid = entry.getDefinition().getStoredFluid().get();
                }
            }
        }
        if (fluid == null) {
            return null;
        }
        CompoundTag tag = new CompoundTag();
        tag.putInt("rx", regionX);
        tag.putInt("rz", regionZ);
        tag.putString("name", new FluidStack(fluid, 1).getDisplayName().getString());
        tag.putInt("yield", totalYield);
        tag.putInt("operations", totalOperations);
        tag.putInt("chunks", chunks);
        tag.putInt("depleted", depleted);
        return tag;
    }

    private static String pretty(String id) {
        String path = ResourceLocation.tryParse(id) == null
                ? id : ResourceLocation.tryParse(id).getPath();
        path = path.replace('_', ' ');
        return path.substring(0, 1).toUpperCase(Locale.ROOT) + path.substring(1);
    }

    private record OriginVeins(ChunkPos origin, List<GeneratedVeinMetadata> layers,
                               net.minecraft.core.BlockPos center, double distance) {
    }
}
