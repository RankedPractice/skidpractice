/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.profile;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.profile.Profile;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ProfileManager
implements Listener {
    private final Map<UUID, Profile> profiles = new ConcurrentHashMap<UUID, Profile>();

    public ProfileManager() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)PotPvP.getInstance());
    }

    public Profile getProfile(UUID uniqueId) {
        for (Profile profile : this.profiles.values()) {
            if (profile.getUniqueId() != uniqueId) continue;
            return profile;
        }
        return new Profile(uniqueId);
    }

    public Profile getProfile(Player player) {
        for (Profile profile : this.profiles.values()) {
            if (profile.getUniqueId() != player.getUniqueId()) continue;
            return profile;
        }
        return new Profile(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        Profile profile = new Profile(event.getUniqueId());
        PotPvP.getInstance().getProfileManager().getProfiles().put(event.getUniqueId(), profile);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CompletableFuture.runAsync(() -> this.getProfile(event.getPlayer().getUniqueId()).save());
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }
}

