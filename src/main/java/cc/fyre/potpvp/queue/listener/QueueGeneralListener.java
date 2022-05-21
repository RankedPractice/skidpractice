/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.queue.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorJoinEvent;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.event.PartyCreateEvent;
import cc.fyre.potpvp.party.event.PartyDisbandEvent;
import cc.fyre.potpvp.party.event.PartyMemberJoinEvent;
import cc.fyre.potpvp.party.event.PartyMemberKickEvent;
import cc.fyre.potpvp.party.event.PartyMemberLeaveEvent;
import cc.fyre.potpvp.queue.QueueHandler;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class QueueGeneralListener
implements Listener {
    private final QueueHandler queueHandler;

    public QueueGeneralListener(QueueHandler queueHandler) {
        this.queueHandler = queueHandler;
    }

    @EventHandler
    public void onPartyDisband(PartyDisbandEvent event) {
        this.queueHandler.leaveQueue(event.getParty(), true);
    }

    @EventHandler
    public void onPartyCreate(PartyCreateEvent event) {
        UUID leaderUuid = event.getParty().getLeader();
        Player leaderPlayer = Bukkit.getPlayer((UUID)leaderUuid);
        this.queueHandler.leaveQueue(leaderPlayer, true, false);
    }

    @EventHandler
    public void onPartyMemberJoin(PartyMemberJoinEvent event) {
        this.queueHandler.leaveQueue(event.getMember(), true, false);
        this.leaveQueue(event.getParty(), event.getMember(), "joined");
    }

    @EventHandler
    public void onPartyMemberKick(PartyMemberKickEvent event) {
        this.leaveQueue(event.getParty(), event.getMember(), "was kicked");
    }

    @EventHandler
    public void onPartyMemberLeave(PartyMemberLeaveEvent event) {
        this.leaveQueue(event.getParty(), event.getMember(), "left");
    }

    private void leaveQueue(Party party, Player member, String action) {
        if (this.queueHandler.leaveQueue(party, true)) {
            party.message(ChatColor.YELLOW + "Your party has been removed from the queue because " + ChatColor.AQUA + member.getName() + ChatColor.YELLOW + " " + action + ".");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.queueHandler.leaveQueue(event.getPlayer(), true, true);
    }

    @EventHandler
    public void onMatchSpectatorJoin(MatchSpectatorJoinEvent event) {
        this.queueHandler.leaveQueue(event.getSpectator(), true, true);
    }

    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        for (MatchTeam team : event.getMatch().getTeams()) {
            for (UUID member : team.getAllMembers()) {
                Player memberBukkit = Bukkit.getPlayer((UUID)member);
                Party memberParty = partyHandler.getParty(memberBukkit);
                this.queueHandler.leaveQueue(memberBukkit, false, true);
                if (memberParty == null || !memberParty.isLeader(member)) continue;
                this.queueHandler.leaveQueue(memberParty, true);
            }
        }
    }
}

