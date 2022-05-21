/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.party.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.event.MatchSpectatorLeaveEvent;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.PartyInvite;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PartyLeaveListener
implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        for (Party party : PotPvP.getInstance().getPartyHandler().getParties()) {
            PartyInvite invite;
            if (party.isMember(playerUuid)) {
                party.leave(player);
            }
            if ((invite = party.getInvite(playerUuid)) == null) continue;
            party.revokeInvite(invite);
        }
    }

    @EventHandler
    public void onMatchSpectatorLeave(MatchSpectatorLeaveEvent event) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(event.getSpectator());
        if (party != null) {
            party.leave(event.getSpectator());
        }
    }
}

