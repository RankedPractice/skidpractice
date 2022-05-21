/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.rematch.listener;

import cc.fyre.potpvp.PotPvP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RematchUnloadListener
implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PotPvP.getInstance().getRematchHandler().unloadRematchData(event.getPlayer());
    }
}

