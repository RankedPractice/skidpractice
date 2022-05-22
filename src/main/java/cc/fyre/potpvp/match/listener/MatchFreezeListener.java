/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MatchFreezeListener
implements Listener {
    @EventHandler
    public void onCountdownEnd(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        if (match == null || !match.getKitType().getId().equals("Sumo") || match.getState() != MatchState.COUNTDOWN) {
            return;
        }
        event.getPlayer().teleport(from);
    }
}

