/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.util.TimeUtils
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchEndReason;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.util.TimeUtils;

public final class MatchDurationLimitListener
implements Listener {
    private static final int DURATION_LIMIT_SECONDS = (int)TimeUnit.MINUTES.toSeconds(20L);
    private static final String TIME_WARNING_MESSAGE = ChatColor.RED + "The match will forcefully end in %s.";
    private static final String TIME_EXCEEDED_MESSAGE = ChatColor.RED.toString() + ChatColor.BOLD + "Match time exceeded %s. Ending match...";

    @EventHandler
    public void onMatchCountdownEnd(MatchStartEvent event) {
        final Match match = event.getMatch();
        new BukkitRunnable(){
            int secondsRemaining = MatchDurationLimitListener.DURATION_LIMIT_SECONDS;

            public void run() {
                if (match.getState() != MatchState.IN_PROGRESS) {
                    this.cancel();
                    return;
                }
                if (match.getKitType().getId().equals("SUMO") || match.getKitType().getId().equals("Boxing")) {
                    match.getTeams().forEach(t -> t.getAllMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> {
                        p.setHealth(20.0);
                        p.setFoodLevel(20);
                        p.setSaturation(20.0f);
                    }));
                }
                switch (this.secondsRemaining) {
                    case 5: 
                    case 10: 
                    case 15: 
                    case 30: 
                    case 60: 
                    case 120: {
                        match.messageAll(String.format(TIME_WARNING_MESSAGE, TimeUtils.formatIntoDetailedString((int)this.secondsRemaining)));
                        break;
                    }
                    case 0: {
                        match.messageAll(String.format(TIME_EXCEEDED_MESSAGE, TimeUtils.formatIntoDetailedString((int)DURATION_LIMIT_SECONDS)));
                        match.endMatch(MatchEndReason.DURATION_LIMIT_EXCEEDED);
                        break;
                    }
                }
                --this.secondsRemaining;
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 20L, 20L);
    }
}

