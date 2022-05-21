/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.util.com.google.common.base.Objects
 *  net.minecraft.util.com.google.common.collect.ImmutableMap
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import cc.fyre.potpvp.util.MongoUtils;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.util.com.google.common.base.Objects;
import net.minecraft.util.com.google.common.collect.ImmutableMap;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class StatisticsHandler
implements Listener {
    private static MongoCollection<Document> COLLECTION;
    private Map<UUID, Map<String, Map<Statistic, Double>>> statisticsMap;

    public StatisticsHandler() {
        COLLECTION = MongoUtils.getCollection("playerStatistics");
        this.statisticsMap = Maps.newConcurrentMap();
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            long start = System.currentTimeMillis();
            this.statisticsMap.keySet().forEach(this::saveStatistics);
            Bukkit.getLogger().info("Saved " + this.statisticsMap.size() + " statistics in " + (System.currentTimeMillis() - start) + "ms.");
        }, 600L, 600L);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> this.loadStatistics(event.getPlayer().getUniqueId()));
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            this.saveStatistics(event.getPlayer().getUniqueId());
            this.unloadStatistics(event.getPlayer().getUniqueId());
        });
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onMatchEnd(MatchTerminateEvent event) {
        Match match = event.getMatch();
        if (match.getKitType().equals(KitType.teamFight)) {
            return;
        }
        match.getWinningPlayers().forEach(uuid -> this.incrementStat((UUID)uuid, Statistic.WINS, match.getKitType()));
        match.getLosingPlayers().forEach(uuid -> this.incrementStat((UUID)uuid, Statistic.LOSSES, match.getKitType()));
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player died = event.getEntity();
        Player killer = died.getKiller();
        Match diedMatch = PotPvP.getInstance().getMatchHandler().getMatchPlayingOrSpectating(died);
        if (diedMatch == null) {
            return;
        }
        if (diedMatch.getKitType().equals(KitType.teamFight)) {
            return;
        }
        this.incrementStat(died.getUniqueId(), Statistic.DEATHS, diedMatch.getKitType());
        if (killer != null) {
            this.incrementStat(killer.getUniqueId(), Statistic.KILLS, diedMatch.getKitType());
        }
    }

    public void loadStatistics(UUID uuid) {
        Document document = (Document)COLLECTION.find(new Document("_id", uuid.toString())).first();
        if (document == null) {
            document = new Document();
        }
        document.put("lastUsername", FrozenUUIDCache.name((UUID)uuid));
        Document finalDocument = document;
        HashMap subStatisticsMap = Maps.newHashMap();
        KitType.getAllTypes().forEach(kitType -> {
            Document subStatisticsDocument = finalDocument.containsKey(kitType.getId()) ? (Document)(finalDocument.get(kitType.getId(), Document.class)) : new Document();
            HashMap statsMap = Maps.newHashMap();
            for (Statistic statistic : Statistic.values()) {
                Double value = (Double)Objects.firstNonNull(((Double)(subStatisticsDocument.get(statistic.name(), Double.class))), 0.0);
                statsMap.put(statistic, value);
            }
            subStatisticsMap.put(kitType.getId(), statsMap);
        });
        if (finalDocument.containsKey("GLOBAL")) {
            Document subStatisticsDocument = finalDocument.containsKey("GLOBAL") ? (Document)(finalDocument.get("GLOBAL", Document.class)) : new Document();
            HashMap statsMap = Maps.newHashMap();
            for (Statistic statistic : Statistic.values()) {
                Double value = (Double)Objects.firstNonNull(((Double)(subStatisticsDocument.get(statistic.name(), Double.class))), 0.0);
                statsMap.put(statistic, value);
            }
            subStatisticsMap.put("GLOBAL", statsMap);
        } else {
            subStatisticsMap.put("GLOBAL", Maps.newHashMap());
        }
        this.statisticsMap.put(uuid, subStatisticsMap);
    }

    public void saveStatistics(UUID uuid) {
        Map<String, Map<Statistic, Double>> subMap = this.statisticsMap.get(uuid);
        if (subMap == null) {
            return;
        }
        Document toInsert = new Document();
        subMap.entrySet().forEach(entry -> {
            Document typeStats = new Document();
            (entry.getValue()).entrySet().forEach(subEntry -> typeStats.put(((Statistic)(((subEntry.getKey())))).name(), subEntry.getValue()));
            toInsert.put((String)entry.getKey(), typeStats);
        });
        toInsert.put("lastUsername", FrozenUUIDCache.name((UUID)uuid));
        COLLECTION.updateOne(new Document("_id", uuid.toString()), (Bson)new Document("$set", toInsert), MongoUtils.UPSERT_OPTIONS);
    }

    public void unloadStatistics(UUID uuid) {
        this.statisticsMap.remove(uuid);
    }

    public void incrementStat(UUID uuid, Statistic statistic, KitType kitType) {
        boolean shouldUpdateKDR;
        boolean shouldUpdateWLR = statistic == Statistic.WINS || statistic == Statistic.LOSSES;
        boolean bl = shouldUpdateKDR = statistic == Statistic.KILLS || statistic == Statistic.DEATHS;
        if (!this.statisticsMap.containsKey(uuid)) {
            return;
        }
        this.incrementEntry(uuid, kitType.getId(), statistic);
        this.incrementEntry(uuid, "GLOBAL", statistic);
        if (shouldUpdateWLR) {
            this.recalculateWLR(uuid, kitType);
        } else if (shouldUpdateKDR) {
            this.recalculateKDR(uuid, kitType);
        }
    }

    private void recalculateWLR(UUID uuid, KitType kitType) {
        double totalWins = this.getStat(uuid, Statistic.WINS, kitType.getId());
        double totalLosses = this.getStat(uuid, Statistic.LOSSES, kitType.getId());
        double ratio = totalWins / Math.max(totalLosses, 1.0);
        this.statisticsMap.get(uuid).get(kitType.getId()).put(Statistic.WLR, ratio);
        totalWins = this.getStat(uuid, Statistic.WINS, "GLOBAL");
        totalLosses = this.getStat(uuid, Statistic.LOSSES, "GLOBAL");
        ratio = totalWins / Math.max(totalLosses, 1.0);
        this.statisticsMap.get(uuid).get("GLOBAL").put(Statistic.WLR, ratio);
    }

    private void recalculateKDR(UUID uuid, KitType kitType) {
        double totalKills = this.getStat(uuid, Statistic.KILLS, kitType.getId());
        double totalDeaths = this.getStat(uuid, Statistic.DEATHS, kitType.getId());
        double ratio = totalKills / Math.max(totalDeaths, 1.0);
        this.statisticsMap.get(uuid).get(kitType.getId()).put(Statistic.KDR, ratio);
        totalKills = this.getStat(uuid, Statistic.KILLS, "GLOBAL");
        totalDeaths = this.getStat(uuid, Statistic.DEATHS, "GLOBAL");
        ratio = totalKills / Math.max(totalDeaths, 1.0);
        this.statisticsMap.get(uuid).get("GLOBAL").put(Statistic.KDR, ratio);
    }

    private void incrementEntry(UUID uuid, String primaryKey, Statistic statistic) {
        Map<Statistic, Double> subMap = this.statisticsMap.get(uuid).get(primaryKey);
        subMap.put(statistic, subMap.getOrDefault(statistic, 0.0) + 1.0);
    }

    public double getStat(UUID uuid, Statistic statistic, String kitType) {
        return (Double)Objects.firstNonNull(this.statisticsMap.getOrDefault(uuid, ImmutableMap.of()).getOrDefault(kitType, ImmutableMap.of()).get(statistic), 0.0);
    }

    private static enum Statistic {
        WINS,
        LOSSES,
        WLR,
        KILLS,
        DEATHS,
        KDR;

    }
}

