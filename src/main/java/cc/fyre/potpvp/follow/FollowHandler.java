/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.nametag.FrozenNametagHandler
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.follow;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.listener.FollowGeneralListener;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.VisibilityUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.nametag.FrozenNametagHandler;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class FollowHandler {
    private final Map<UUID, UUID> followingData = new ConcurrentHashMap<UUID, UUID>();

    public FollowHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new FollowGeneralListener(this), (Plugin)PotPvP.getInstance());
    }

    public Optional<UUID> getFollowing(Player player) {
        return Optional.ofNullable(this.followingData.get(player.getUniqueId()));
    }

    public void startFollowing(Player player, Player target) {
        this.followingData.put(player.getUniqueId(), target.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Now following " + ChatColor.RESET + target.getDisplayName() + ChatColor.GOLD + ", exit with /unfollow.");
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match targetMatch = matchHandler.getMatchPlayingOrSpectating(target);
        if (targetMatch != null && targetMatch.getState() != MatchState.ENDING) {
            targetMatch.addSpectator(player, target);
        } else {
            InventoryUtils.resetInventoryDelayed(player);
            VisibilityUtils.updateVisibility(player);
            FrozenNametagHandler.reloadOthersFor((Player)player);
            player.teleport((Entity)target);
        }
    }

    public void stopFollowing(Player player) {
        UUID prevTarget = this.followingData.remove(player.getUniqueId());
        if (prevTarget != null) {
            Player online = Bukkit.getPlayer((UUID)prevTarget);
            player.sendMessage(ChatColor.GOLD + "Stopped following " + ChatColor.RESET + (online == null ? FrozenUUIDCache.name((UUID)prevTarget) : online.getDisplayName()) + ChatColor.GOLD + ".");
            InventoryUtils.resetInventoryDelayed(player);
            VisibilityUtils.updateVisibility(player);
            FrozenNametagHandler.reloadOthersFor((Player)player);
        }
    }

    public Set<UUID> getFollowers(Player player) {
        HashSet<UUID> followers = new HashSet<UUID>();
        this.followingData.forEach((follower, followed) -> {
            if (followed == player.getUniqueId()) {
                followers.add((UUID)follower);
            }
        });
        return followers;
    }
}

