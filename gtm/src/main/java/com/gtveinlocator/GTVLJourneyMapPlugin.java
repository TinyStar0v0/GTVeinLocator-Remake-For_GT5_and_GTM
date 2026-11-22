package com.gtveinlocator;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JmApiVersion;
import journeymap.api.v2.common.JourneyMapPlugin;

@JourneyMapPlugin(apiVersion = JmApiVersion.VERSION)
public final class GTVLJourneyMapPlugin implements IClientPlugin {

    static IClientAPI api;

    @Override
    public void initialize(IClientAPI api) {
        GTVLJourneyMapPlugin.api = api;
    }

    @Override
    public String getModId() {
        return GTVeinLocator.MODID;
    }
}
