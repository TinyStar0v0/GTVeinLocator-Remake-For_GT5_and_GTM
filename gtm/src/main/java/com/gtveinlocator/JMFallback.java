package com.gtveinlocator;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.common.waypoint.Waypoint;
import journeymap.api.v2.common.waypoint.WaypointFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

final class JMFallback {

    private JMFallback() {
    }

    static void show(CompoundTag data) {
        IClientAPI api = GTVLJourneyMapPlugin.api;
        Minecraft minecraft = Minecraft.getInstance();
        if (api == null || minecraft.player == null || minecraft.level == null) {
            return;
        }
        Level level = minecraft.level;
        ListTag ores = data.getList("ores", Tag.TAG_COMPOUND);
        for (int i = 0; i < ores.size(); i++) {
            CompoundTag ore = ores.getCompound(i);
            int x = ore.getInt("x");
            int z = ore.getInt("z");
            int y = surface(level, x, z, ore.getInt("y"));
            String name = "GT: " + pretty(ore.getString("id"));
            add(api, level, new BlockPos(x, y, z), name, 0xFFAA00);
        }
        if (data.contains("fluid")) {
            CompoundTag fluid = data.getCompound("fluid");
            int x = fluid.getInt("rx") * 8 * 16 + 64;
            int z = fluid.getInt("rz") * 8 * 16 + 64;
            int y = surface(level, x, z, 64);
            add(api, level, new BlockPos(x, y, z), "GT: " + fluid.getString("name"), 0x33CCFF);
        }
        minecraft.player.displayClientMessage(
                net.minecraft.network.chat.Component.translatable("msg.gtveinlocator.waypoints"), true);
    }

    private static void add(IClientAPI api, Level level, BlockPos pos, String name, int color) {
        try {
            Waypoint waypoint = WaypointFactory.createWaypoint(GTVeinLocator.MODID, pos, name,
                    level.dimension(), true);
            waypoint.setColor(color);
            waypoint.setShowBeacon(true);
            api.addWaypoint(GTVeinLocator.MODID, waypoint);
        } catch (Throwable ignored) {
        }
    }

    private static int surface(Level level, int x, int z, int fallback) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        return y > level.getMinBuildHeight() ? y : fallback;
    }

    private static String pretty(String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        String path = location == null ? id : location.getPath();
        path = path.replace('_', ' ');
        return path.substring(0, 1).toUpperCase() + path.substring(1);
    }
}
