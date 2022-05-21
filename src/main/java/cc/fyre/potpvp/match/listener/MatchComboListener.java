/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MatchComboListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onStart(MatchStartEvent event) {
        Match match = event.getMatch();
        int noDamageTicks = match.getKitType().getId().contains("COMBO") ? 3 : 20;
        match.getTeams().forEach(team -> team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> p.setMaximumNoDamageTicks(noDamageTicks)));
    }
}

