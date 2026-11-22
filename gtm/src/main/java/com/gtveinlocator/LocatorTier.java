package com.gtveinlocator;

public enum LocatorTier {
    LV(3, Integer.MAX_VALUE, false),
    MV(5, Integer.MAX_VALUE, false),
    HV(7, Integer.MAX_VALUE, true);

    public final int radiusChunks;
    public final int maxVeins;
    public final boolean fluid;

    LocatorTier(int radiusChunks, int maxVeins, boolean fluid) {
        this.radiusChunks = radiusChunks;
        this.maxVeins = maxVeins;
        this.fluid = fluid;
    }
}
