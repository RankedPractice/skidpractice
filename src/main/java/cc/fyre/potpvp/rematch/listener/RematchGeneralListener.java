/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package cc.fyre.potpvp.rematch.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RematchGeneralListener
implements Listener {
    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        PotPvP.getInstance().getRematchHandler().registerRematches(event.getMatch());
    }
}

