/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.postmatchinv.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import cc.fyre.potpvp.postmatchinv.PostMatchInvHandler;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PostMatchInvGeneralListener
implements Listener {
    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        PostMatchInvHandler postMatchInvHandler = PotPvP.getInstance().getPostMatchInvHandler();
        postMatchInvHandler.recordMatch(event.getMatch());
    }

    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        PostMatchInvHandler postMatchInvHandler = PotPvP.getInstance().getPostMatchInvHandler();
        for (MatchTeam team : event.getMatch().getTeams()) {
            for (UUID member : team.getAllMembers()) {
                postMatchInvHandler.removePostMatchData(member);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PostMatchInvHandler postMatchInvHandler = PotPvP.getInstance().getPostMatchInvHandler();
        UUID playerUuid = event.getPlayer().getUniqueId();
        postMatchInvHandler.removePostMatchData(playerUuid);
    }
}

