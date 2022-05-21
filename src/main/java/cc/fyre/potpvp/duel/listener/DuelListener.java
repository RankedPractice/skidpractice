/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package cc.fyre.potpvp.duel.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.duel.DuelHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorJoinEvent;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.event.PartyDisbandEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class DuelListener
implements Listener {
    @EventHandler
    public void onMatchSpectatorJoin(MatchSpectatorJoinEvent event) {
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        Player player = event.getSpectator();
        duelHandler.removeInvitesTo(player);
        duelHandler.removeInvitesFrom(player);
    }

    @EventHandler
    public void onPartyDisband(PartyDisbandEvent event) {
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        Party party = event.getParty();
        duelHandler.removeInvitesTo(party);
        duelHandler.removeInvitesFrom(party);
    }

    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        for (MatchTeam team : event.getMatch().getTeams()) {
            for (UUID member : team.getAllMembers()) {
                Player memberPlayer = Bukkit.getPlayer((UUID)member);
                duelHandler.removeInvitesTo(memberPlayer);
                duelHandler.removeInvitesFrom(memberPlayer);
            }
        }
    }
}

