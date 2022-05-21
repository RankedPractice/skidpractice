/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.elo.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.party.event.PartyCreateEvent;
import cc.fyre.potpvp.party.event.PartyMemberJoinEvent;
import cc.fyre.potpvp.party.event.PartyMemberKickEvent;
import cc.fyre.potpvp.party.event.PartyMemberLeaveEvent;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public final class EloLoadListener
implements Listener {
    private final EloHandler eloHandler;

    public EloLoadListener(EloHandler eloHandler) {
        this.eloHandler = eloHandler;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        ImmutableSet playerSet = ImmutableSet.of((Object)event.getUniqueId());
        this.eloHandler.loadElo((Set<UUID>)playerSet);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        ImmutableSet playerSet = ImmutableSet.of((Object)event.getPlayer().getUniqueId());
        this.eloHandler.unloadElo((Set<UUID>)playerSet);
    }

    @EventHandler
    public void onPartyCreate(PartyCreateEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.eloHandler.loadElo(event.getParty().getMembers()));
    }

    @EventHandler
    public void onPartyMemberJoin(PartyMemberJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.eloHandler.loadElo(event.getParty().getMembers()));
    }

    @EventHandler
    public void onPartyMemberKick(PartyMemberKickEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.eloHandler.loadElo(event.getParty().getMembers()));
    }

    @EventHandler
    public void onPartyMemberLeave(PartyMemberLeaveEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.eloHandler.loadElo(event.getParty().getMembers()));
    }
}

