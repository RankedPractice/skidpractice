/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package cc.fyre.potpvp.util;

import org.bukkit.Location;

public final class LocationUtils {
    public static String locToStr(Location loc) {
        return "(" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")";
    }

    private LocationUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

