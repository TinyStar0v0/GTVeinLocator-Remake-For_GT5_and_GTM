package com.gtveinlocator;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.thirdparty.ThirdPartyWaypoints;

final class XaeroFallback {

    private XaeroFallback() {
    }

    static void show(CompoundTag data) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }
        try {
            XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
            if (session == null) {
                return;
            }
            WaypointsManager manager = session.getWaypointsManager();
            WaypointWorld world = manager == null ? null : manager.getCurrentWorld();
            if (world == null) {
                return;
            }
            ThirdPartyWaypoints set = world.getContainer().getThirdPartyWaypointManager()
                    .get(new ResourceLocation(GTVeinLocator.MODID, "locator"));
            set.setEnabledStateGetter(() -> true);

            Level level = minecraft.level;
            ListTag ores = data.getList("ores", Tag.TAG_COMPOUND);
            for (int i = 0; i < ores.size(); i++) {
                CompoundTag ore = ores.getCompound(i);
                int x = ore.getInt("x");
                int z = ore.getInt("z");
                int y = surface(level, x, z, ore.getInt("y"));
                String name = "GT: " + pretty(ore.getString("id"));
                String id = "ore:" + ore.getInt("ox") + ":" + ore.getInt("oz");
                if (set.get(id) == null) {
                    set.add(id, new Waypoint(x, y, z, name, initials(name),
                            WaypointColor.GOLD, WaypointPurpose.NORMAL));
                }
            }
            if (data.contains("fluid")) {
                CompoundTag fluid = data.getCompound("fluid");
                int x = fluid.getInt("rx") * 8 * 16 + 64;
                int z = fluid.getInt("rz") * 8 * 16 + 64;
                int y = surface(level, x, z, 64);
                String name = "GT: " + fluid.getString("name");
                String id = "fluid:" + fluid.getInt("rx") + ":" + fluid.getInt("rz");
                if (set.get(id) == null) {
                    set.add(id, new Waypoint(x, y, z, name, initials(name),
                            WaypointColor.AQUA, WaypointPurpose.NORMAL));
                }
            }
            minecraft.player.displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("msg.gtveinlocator.waypoints"), true);
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

    private static String initials(String name) {
        String[] parts = name.replaceAll("[^\\p{L}\\p{N}]", " ").trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty() && result.length() < 2) {
                result.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return result.isEmpty() ? "GT" : result.toString();
    }
}
