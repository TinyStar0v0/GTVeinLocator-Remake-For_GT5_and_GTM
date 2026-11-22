package com.gtveinlocator;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;
import java.util.List;

/** Marks the located origins in VisualVein's persistent vein data. */
final class VisualVeinMarking {

    private VisualVeinMarking() {
    }

    static void markFluid(ServerLevel level, ServerPlayer player) {
        if (!ModList.get().isLoaded("visualvein")) {
            return;
        }
        try {
            Class<?> sync = Class.forName("com.visualvein.server.FluidVeinSyncManager");
            Method discoverAround = sync.getMethod("discoverAround", ServerLevel.class,
                    ServerPlayer.class, int.class);
            // Radius 0 targets exactly the current 8x8 GT fluid region.
            discoverAround.invoke(null, level, player, 0);
        } catch (Throwable ignored) {
        }
    }

    static void mark(ServerLevel level, List<ChunkPos> origins) {
        if (!ModList.get().isLoaded("visualvein")) {
            return;
        }
        try {
            Class<?> scanner = Class.forName("com.visualvein.server.VeinScanner");
            Method scanRegion = scanner.getMethod("scanRegion", ServerLevel.class,
                    ChunkPos.class, int.class, String.class);
            for (ChunkPos origin : origins) {
                scanRegion.invoke(null, level, origin, 0, "locator");
            }
        } catch (Throwable ignored) {
        }
    }
}
