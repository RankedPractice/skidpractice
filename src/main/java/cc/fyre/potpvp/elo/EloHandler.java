/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.util.UUIDUtils
 */
package cc.fyre.potpvp.elo;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloCalculator;
import cc.fyre.potpvp.elo.listener.EloLoadListener;
import cc.fyre.potpvp.elo.listener.EloUpdateListener;
import cc.fyre.potpvp.elo.repository.EloRepository;
import cc.fyre.potpvp.elo.repository.MongoEloRepository;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.util.UUIDUtils;

public final class EloHandler {
    public static final int DEFAULT_ELO = 1000;
    private final Map<Set<UUID>, Map<KitType, Integer>> premiumEloData = new ConcurrentHashMap<Set<UUID>, Map<KitType, Integer>>();
    private final Map<Set<UUID>, Map<KitType, Integer>> eloData = new ConcurrentHashMap<Set<UUID>, Map<KitType, Integer>>();
    private final EloRepository eloRepository;

    public EloHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new EloLoadListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new EloUpdateListener(this, new EloCalculator(35.0, 7, 25, 7, 25)), (Plugin)PotPvP.getInstance());
        this.eloRepository = new MongoEloRepository();
    }

    public int getElo(Player player, KitType kitType) {
        return this.getElo((Set<UUID>)ImmutableSet.of((Object)player.getUniqueId()), kitType);
    }

    public int getPremiumElo(Player player, KitType kitType) {
        return this.getPremiumElo((Set<UUID>)ImmutableSet.of((Object)player.getUniqueId()), kitType);
    }

    public void setElo(Player player, KitType kitType, int newElo) {
        this.setElo((Set<UUID>)ImmutableSet.of((Object)player.getUniqueId()), kitType, newElo);
    }

    public void setPremiumElo(Player player, KitType kitType, int newElo) {
        this.setPremiumElo((Set<UUID>)ImmutableSet.of((Object)player.getUniqueId()), kitType, newElo);
    }

    public int getPremiumElo(Set<UUID> playerUuids, KitType kitType) {
        Map<KitType, Integer> partyElo = this.premiumEloData.getOrDefault(playerUuids, (Map<KitType, Integer>)ImmutableMap.of());
        return partyElo.getOrDefault(kitType, 1000);
    }

    public int getElo(Set<UUID> playerUuids, KitType kitType) {
        Map<KitType, Integer> partyElo = this.eloData.getOrDefault(playerUuids, (Map<KitType, Integer>)ImmutableMap.of());
        return partyElo.getOrDefault(kitType, 1000);
    }

    public int getGlobalElo(UUID uuid) {
        Map<KitType, Integer> eloValues = this.eloData.getOrDefault(ImmutableSet.of((Object)uuid), (Map<KitType, Integer>)ImmutableMap.of());
        if (eloValues.isEmpty()) {
            return 1000;
        }
        int[] wrapper = new int[2];
        KitType.getAllTypes().stream().filter(KitType::isSupportsRanked).forEach(kitType -> {
            wrapper[0] = wrapper[0] + 1;
            wrapper[1] = wrapper[1] + eloValues.getOrDefault(kitType, 1000);
        });
        return wrapper[1] / wrapper[0];
    }

    public int getGlobalPremiumElo(UUID uuid) {
        Map<KitType, Integer> eloValues = this.premiumEloData.getOrDefault(ImmutableSet.of((Object)uuid), (Map<KitType, Integer>)ImmutableMap.of());
        if (eloValues.isEmpty()) {
            return 1000;
        }
        int[] wrapper = new int[2];
        KitType.getAllTypes().stream().filter(KitType::isSupportsPremium).forEach(kitType -> {
            wrapper[0] = wrapper[0] + 1;
            wrapper[1] = wrapper[1] + eloValues.getOrDefault(kitType, 1000);
        });
        return wrapper[1] / wrapper[0];
    }

    public void setPremiumElo(Set<UUID> playerUuids, KitType kitType, int newElo) {
        Map partyElo = this.premiumEloData.computeIfAbsent(playerUuids, i -> new ConcurrentHashMap());
        partyElo.put(kitType, newElo);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            try {
                this.eloRepository.savePremiumElo(playerUuids, partyElo);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void setElo(Set<UUID> playerUuids, KitType kitType, int newElo) {
        Map partyElo = this.eloData.computeIfAbsent(playerUuids, i -> new ConcurrentHashMap());
        partyElo.put(kitType, newElo);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            try {
                this.eloRepository.saveElo(playerUuids, partyElo);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void loadElo(Set<UUID> playerUuids) {
        ConcurrentHashMap<KitType, Integer> partyElo;
        try {
            partyElo = new ConcurrentHashMap<KitType, Integer>(this.eloRepository.loadElo(playerUuids));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            partyElo = new ConcurrentHashMap();
        }
        this.eloData.put(playerUuids, partyElo);
    }

    public void loadPremiumElo(Set<UUID> playerUuids) {
        ConcurrentHashMap<KitType, Integer> partyElo;
        try {
            partyElo = new ConcurrentHashMap<KitType, Integer>(this.eloRepository.loadPremiumElo(playerUuids));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            partyElo = new ConcurrentHashMap();
        }
        this.premiumEloData.put(playerUuids, partyElo);
    }

    public void unloadPremiumElo(Set<UUID> playerUuids) {
        this.premiumEloData.remove(playerUuids);
    }

    public void unloadElo(Set<UUID> playerUuids) {
        this.eloData.remove(playerUuids);
    }

    public Map<String, Integer> topPremiumElo(KitType type2) {
        ImmutableMap topElo;
        try {
            topElo = this.eloRepository.topPremiumElo(type2);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            topElo = ImmutableMap.of();
        }
        return topElo;
    }

    public Map<String, Integer> topElo(KitType type2) {
        ImmutableMap topElo;
        try {
            topElo = this.eloRepository.topElo(type2);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            topElo = ImmutableMap.of();
        }
        return topElo;
    }

    public void resetElo(UUID player) {
        Bukkit.getLogger().info("Resetting elo of " + UUIDUtils.name((UUID)player) + ".");
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            this.unloadElo((Set<UUID>)ImmutableSet.of((Object)player));
            try {
                this.eloRepository.saveElo((Set<UUID>)ImmutableSet.of((Object)player), (Map<KitType, Integer>)ImmutableMap.of());
                this.eloRepository.savePremiumElo((Set<UUID>)ImmutableSet.of((Object)player), (Map<KitType, Integer>)ImmutableMap.of());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public EloRepository getEloRepository() {
        return this.eloRepository;
    }
}

