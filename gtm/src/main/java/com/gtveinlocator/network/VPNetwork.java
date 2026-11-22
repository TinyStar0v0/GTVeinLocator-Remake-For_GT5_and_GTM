package com.gtveinlocator.network;

import com.gtveinlocator.GTVeinLocator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class VPNetwork {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(GTVeinLocator.MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    private VPNetwork() {
    }

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, LocatorResultPacket.class,
                LocatorResultPacket::encode, LocatorResultPacket::decode, LocatorResultPacket::handle);
    }

    public static void sendTo(ServerPlayer player, LocatorResultPacket packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
