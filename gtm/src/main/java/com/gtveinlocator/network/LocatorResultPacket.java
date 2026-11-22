package com.gtveinlocator.network;

import com.gtveinlocator.client.ClientLocatorData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record LocatorResultPacket(CompoundTag data) {

    public static void encode(LocatorResultPacket packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.data);
    }

    public static LocatorResultPacket decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        return new LocatorResultPacket(tag == null ? new CompoundTag() : tag);
    }

    public static void handle(LocatorResultPacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> ClientLocatorData.accept(packet.data));
        ctx.setPacketHandled(true);
    }
}
