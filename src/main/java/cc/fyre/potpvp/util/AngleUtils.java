/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.block.BlockFace
 */
package cc.fyre.potpvp.util;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.block.BlockFace;

public final class AngleUtils {
    private static final Map<BlockFace, Integer> NOTCHES = new EnumMap<BlockFace, Integer>(BlockFace.class);

    public static int faceToYaw(BlockFace face) {
        return AngleUtils.wrapAngle(45 * NOTCHES.getOrDefault(face, 0));
    }

    private static int wrapAngle(int angle) {
        int wrappedAngle;
        for (wrappedAngle = angle; wrappedAngle <= -180; wrappedAngle += 360) {
        }
        while (wrappedAngle > 180) {
            wrappedAngle -= 360;
        }
        return wrappedAngle;
    }

    private AngleUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        BlockFace[] radials = new BlockFace[]{BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST};
        for (int i = 0; i < radials.length; ++i) {
            NOTCHES.put(radials[i], i);
        }
    }
}

