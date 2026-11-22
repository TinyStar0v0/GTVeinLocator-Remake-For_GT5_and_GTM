package pers.gwyog.gtveinlocator.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import pers.gwyog.gtveinlocator.GTVeinLocator;

public final class GTVLNetwork {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE
        .newSimpleChannel(GTVeinLocator.MODID.toLowerCase());

    private GTVLNetwork() {}

    public static void init() {
        INSTANCE.registerMessage(LocatorMessageHandler.class, LocatorMessage.class, 0, Side.CLIENT);
    }
}
