/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.util;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class DisguiseUtil {
    public static String getDisguisedName(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        return player == null ? FrozenUUIDCache.name((UUID)uuid) : player.getDisguisedName();
    }
}

