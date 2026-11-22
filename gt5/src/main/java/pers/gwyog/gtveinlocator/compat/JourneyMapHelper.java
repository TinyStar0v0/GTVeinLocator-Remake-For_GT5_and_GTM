package pers.gwyog.gtveinlocator.compat;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import cpw.mods.fml.common.Loader;

/** Reflection keeps JourneyMap optional for GT5u and avoids a hard compile dependency. */
public final class JourneyMapHelper {

    private static boolean failed;

    private JourneyMapHelper() {}

    public static boolean addWaypoint(String name, int x, int y, int z) {
        if (!Loader.isModLoaded("journeymap")) {
            return false;
        }
        try {
            Class<?> waypointClass = Class.forName("journeymap.client.model.Waypoint");
            Class<?> typeClass = Class.forName("journeymap.client.model.Waypoint$Type");
            Class<?> storeClass = Class.forName("journeymap.client.waypoint.WaypointStore");
            Constructor<?> constructor = waypointClass
                .getConstructor(String.class, int.class, int.class, int.class, Color.class, typeClass, Integer.class);
            Object waypoint = constructor
                .newInstance(name, x, y, z, Color.WHITE, typeClass.getEnumConstants()[0], Integer.valueOf(0));
            Object store = storeClass.getMethod("instance")
                .invoke(null);
            Method save = storeClass.getMethod("save", waypointClass);
            save.invoke(store, waypoint);
            return true;
        } catch (Throwable throwable) {
            failed = true;
            return false;
        }
    }

    public static boolean failed() {
        return failed;
    }
}
