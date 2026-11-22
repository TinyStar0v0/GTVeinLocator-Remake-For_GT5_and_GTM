package com.gtveinlocator;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.ModList;

/** Creates ordinary map waypoints when VisualVein is not installed. */
public final class FallbackWaypoints {

    private FallbackWaypoints() {
    }

    public static void show(CompoundTag data) {
        if (ModList.get().isLoaded("xaerominimap")) {
            XaeroFallback.show(data);
        } else if (ModList.get().isLoaded("journeymap")) {
            JMFallback.show(data);
        }
    }
}
