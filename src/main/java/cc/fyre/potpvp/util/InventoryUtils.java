/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.lobby.LobbyUtils;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class InventoryUtils {
    public static final long RESET_DELAY_TICKS = 2L;

    public static void resetInventoryDelayed(Player player) {
        Runnable task = () -> InventoryUtils.resetInventoryNow(player);
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), task, 2L);
    }

    public static void resetInventoryNow(Player player) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isPlayingOrSpectatingMatch(player)) {
            MatchUtils.resetInventory(player);
        } else {
            LobbyUtils.resetInventory(player);
        }
    }

    private InventoryUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

