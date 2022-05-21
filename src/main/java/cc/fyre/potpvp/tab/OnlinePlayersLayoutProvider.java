/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.bridge.BridgeGlobal
 *  rip.bridge.bridge.global.profile.Profile
 *  rip.bridge.qlib.tab.TabLayout
 *  rip.bridge.qlib.util.PlayerUtils
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.tab;

import cc.fyre.potpvp.PotPvP;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.util.PlayerUtils;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class OnlinePlayersLayoutProvider
implements Listener,
BiConsumer<Player, TabLayout> {
    private Map<UUID, String> playersMap = this.generateNewTreeMap();

    public OnlinePlayersLayoutProvider() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)PotPvP.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), this::rebuildCache, 0L, 1200L);
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {
    }

    @EventHandler(ignoreCancelled=true)
    public void onJoin(PlayerJoinEvent event) {
        this.playersMap.put(event.getPlayer().getUniqueId(), this.getName(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.playersMap.remove(event.getPlayer().getUniqueId());
    }

    private void rebuildCache() {
        TreeMap<UUID, String> newTreeMap = this.generateNewTreeMap();
        Bukkit.getOnlinePlayers().forEach(player -> newTreeMap.put(player.getUniqueId(), this.getName(player.getUniqueId())));
        this.playersMap = newTreeMap;
    }

    private String getName(UUID uuid) {
        return BridgeGlobal.getProfileHandler().getProfileByUUID(uuid).getColor() + FrozenUUIDCache.name((UUID)uuid);
    }

    public int getPing(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        return player == null ? -1 : Math.max((PlayerUtils.getPing((Player)player) + 5) / 10 * 10, 1);
    }

    private TreeMap<UUID, String> generateNewTreeMap() {
        return new TreeMap<UUID, String>(new Comparator<UUID>(){

            @Override
            public int compare(UUID first, UUID second) {
                Profile firstProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(first);
                Profile secondProfile = BridgeGlobal.getProfileHandler().getProfileByUUID(second);
                if (firstProfile != null && secondProfile != null) {
                    int compare = Integer.compare(secondProfile.getCurrentGrant().getRank().getPriority(), firstProfile.getCurrentGrant().getRank().getPriority());
                    if (compare == 0) {
                        return OnlinePlayersLayoutProvider.this.tieBreaker(first, second);
                    }
                    return compare;
                }
                if (firstProfile != null && secondProfile == null) {
                    return -1;
                }
                if (firstProfile == null && secondProfile != null) {
                    return 1;
                }
                return OnlinePlayersLayoutProvider.this.tieBreaker(first, second);
            }
        });
    }

    private int tieBreaker(UUID first, UUID second) {
        String firstName = FrozenUUIDCache.name((UUID)first);
        String secondName = FrozenUUIDCache.name((UUID)second);
        return firstName.compareTo(secondName);
    }
}

