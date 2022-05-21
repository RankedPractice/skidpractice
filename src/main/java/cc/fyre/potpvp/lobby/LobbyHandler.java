/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.nametag.FrozenNametagHandler
 */
package cc.fyre.potpvp.lobby;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.follow.command.UnfollowCommand;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.lobby.listener.LobbyGeneralListener;
import cc.fyre.potpvp.lobby.listener.LobbyItemListener;
import cc.fyre.potpvp.lobby.listener.LobbyParkourListener;
import cc.fyre.potpvp.lobby.listener.LobbySpecModeListener;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import cc.fyre.potpvp.util.VisibilityUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.nametag.FrozenNametagHandler;

public final class LobbyHandler {
    private final Set<UUID> spectatorMode = new HashSet<UUID>();
    private final Map<UUID, Long> returnedToLobby = new HashMap<UUID, Long>();

    public LobbyHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new LobbyGeneralListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new LobbyItemListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new LobbySpecModeListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new LobbyParkourListener(), (Plugin)PotPvP.getInstance());
    }

    public void returnToLobby(Player player) {
        this.returnToLobbySkipItemSlot(player);
        player.getInventory().setHeldItemSlot(0);
    }

    private void returnToLobbySkipItemSlot(Player player) {
        player.teleport(this.getLobbyLocation());
        FrozenNametagHandler.reloadPlayer((Player)player);
        FrozenNametagHandler.reloadOthersFor((Player)player);
        VisibilityUtils.updateVisibility(player);
        PatchedPlayerUtils.resetInventory(player, GameMode.CREATIVE, KitType.teamFight, true);
        InventoryUtils.resetInventoryDelayed(player);
        player.setGameMode(GameMode.SURVIVAL);
        this.returnedToLobby.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public long getLastLobbyTime(Player player) {
        return this.returnedToLobby.getOrDefault(player.getUniqueId(), 0L);
    }

    public boolean isInLobby(Player player) {
        /*if (PotPvP.getInstance().getGameHandler().getOngoingGame() != null && PotPvP.getInstance().getGameHandler().getOngoingGame().getPlayers().contains(player)) {
            return false;
        }*/
        return !PotPvP.getInstance().getMatchHandler().isPlayingOrSpectatingMatch(player);
    }

    public boolean isInSpectatorMode(Player player) {
        return this.spectatorMode.contains(player.getUniqueId());
    }

    public void setSpectatorMode(Player player, boolean mode) {
        boolean changed;
        if (mode) {
            changed = this.spectatorMode.add(player.getUniqueId());
        } else {
            FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
            followHandler.getFollowing(player).ifPresent(i -> UnfollowCommand.unfollow(player));
            changed = this.spectatorMode.remove(player.getUniqueId());
        }
        if (changed) {
            InventoryUtils.resetInventoryNow(player);
            if (!mode) {
                this.returnToLobbySkipItemSlot(player);
            }
        }
    }

    public Location getLobbyLocation() {
        Location spawn = ((World)Bukkit.getWorlds().get(0)).getSpawnLocation();
        spawn.add(0.5, 0.0, 0.5);
        return spawn;
    }
}

