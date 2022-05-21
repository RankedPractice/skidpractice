/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.lobby.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.event.PartyCreateEvent;
import cc.fyre.potpvp.party.event.PartyMemberJoinEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class LobbySpecModeListener
implements Listener {
    @EventHandler
    public void onPartyMemberJoin(PartyMemberJoinEvent event) {
        PotPvP.getInstance().getLobbyHandler().setSpectatorMode(event.getMember(), false);
    }

    @EventHandler
    public void onPartyCreate(PartyCreateEvent event) {
        Player leader = Bukkit.getPlayer((UUID)event.getParty().getLeader());
        PotPvP.getInstance().getLobbyHandler().setSpectatorMode(leader, false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PotPvP.getInstance().getLobbyHandler().setSpectatorMode(event.getPlayer(), false);
    }
}

