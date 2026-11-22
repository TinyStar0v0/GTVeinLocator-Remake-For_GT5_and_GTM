package com.gtveinlocator.client;

import com.gtveinlocator.FallbackWaypoints;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fml.ModList;

public final class ClientLocatorData {

    private static CompoundTag current;

    private ClientLocatorData() {
    }

    public static void accept(CompoundTag data) {
        current = data;
        // With VisualVein installed the server already wrote the discovered veins into its data.
        // Only use ordinary waypoints when neither VP edition is present.
        if (!ModList.get().isLoaded("visualvein")) {
            FallbackWaypoints.show(data);
        }
    }

    public static CompoundTag current() {
        return current;
    }
}
