/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 *  net.minecraft.util.com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.util.UUIDUtils
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.elo.repository;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.repository.EloRepository;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.util.MongoUtils;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.util.com.google.common.collect.ImmutableSet;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.util.UUIDUtils;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class MongoEloRepository
implements EloRepository {
    private static final String MONGO_COLLECTION_NAME = "elo";
    private static final String MONGO_COLLECTION_PREMIUM_NAME = "premium_elo";
    private static Map<String, Map<String, Integer>> cachedFormattedElo = Maps.newHashMap();
    private static Map<String, Map<String, Integer>> cachedFormattedPremiumElo = Maps.newHashMap();
    private static MongoEloRepository instance;

    public MongoEloRepository() {
        instance = this;
        MongoUtils.getCollection(MONGO_COLLECTION_NAME).createIndex(new Document("players", 1));
        MongoUtils.getCollection(MONGO_COLLECTION_PREMIUM_NAME).createIndex(new Document("players", 1));
        Bukkit.getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), () -> this.refreshFormattedElo(), 150L, 150L);
    }

    @Override
    public Map<KitType, Integer> loadElo(Set<UUID> playerUuids) throws IOException {
        MongoCollection<Document> partyEloCollection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Set playerUuidStrings = playerUuids.stream().map(u -> u.toString()).collect(Collectors.toSet());
        try {
            Document eloDocument = (Document)partyEloCollection.find(new Document("players", playerUuidStrings)).first();
            if (eloDocument == null) {
                return ImmutableMap.of();
            }
            HashMap parsedElo = new HashMap();
            Document finalEloDocument = eloDocument;
            KitType.getAllTypes().forEach(kitType -> {
                Integer elo = finalEloDocument.getInteger(kitType.getId());
                if (elo != null) {
                    parsedElo.put(kitType, elo);
                } else {
                    parsedElo.put(kitType, 1000);
                }
            });
            return ImmutableMap.copyOf(parsedElo);
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public Map<KitType, Integer> loadPremiumElo(Set<UUID> playerUuids) throws IOException {
        MongoCollection<Document> partyEloCollection = MongoUtils.getCollection(MONGO_COLLECTION_PREMIUM_NAME);
        Set playerUuidStrings = playerUuids.stream().map(UUID::toString).collect(Collectors.toSet());
        try {
            Document eloDocument = (Document)partyEloCollection.find(new Document("players", playerUuidStrings)).first();
            if (eloDocument == null) {
                return ImmutableMap.of();
            }
            HashMap parsedElo = new HashMap();
            Document finalEloDocument = eloDocument;
            KitType.getAllTypes().forEach(kitType -> {
                Integer elo = finalEloDocument.getInteger(kitType.getId());
                if (elo != null) {
                    parsedElo.put(kitType, elo);
                } else {
                    parsedElo.put(kitType, 1000);
                }
            });
            return ImmutableMap.copyOf(parsedElo);
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void savePremiumElo(Set<UUID> playerUuids, Map<KitType, Integer> elo) throws IOException {
        Document document = new Document();
        elo.forEach((kit, value) -> document.put(kit.getId(), value));
        int[] wrapper = new int[2];
        KitType.getAllTypes().forEach(kitType -> document.putIfAbsent(kitType.getId(), 1000));
        KitType.getAllTypes().stream().filter(KitType::isSupportsPremium).forEach(kitType -> {
            wrapper[0] = wrapper[0] + 1;
            wrapper[1] = wrapper[1] + elo.getOrDefault(kitType, 1000);
        });
        document.put("GLOBAL", (Object)(wrapper[1] / wrapper[0]));
        if (playerUuids.size() == 1) {
            document.put("lastUsername", (Object)UUIDUtils.name((UUID)playerUuids.iterator().next()));
        }
        try {
            MongoUtils.getCollection(MONGO_COLLECTION_PREMIUM_NAME).updateOne(new Document("players", playerUuids.stream().map(UUID::toString).collect(Collectors.toSet())), (Bson)new Document("$set", document), MongoUtils.UPSERT_OPTIONS);
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void saveElo(Set<UUID> playerUuids, Map<KitType, Integer> elo) throws IOException {
        Document document = new Document();
        elo.forEach((kit, value) -> document.put(kit.getId(), value));
        int[] wrapper = new int[2];
        KitType.getAllTypes().forEach(kitType -> document.putIfAbsent(kitType.getId(), 1000));
        KitType.getAllTypes().stream().filter(KitType::isSupportsRanked).forEach(kitType -> {
            wrapper[0] = wrapper[0] + 1;
            wrapper[1] = wrapper[1] + elo.getOrDefault(kitType, 1000);
        });
        document.put("GLOBAL", (Object)(wrapper[1] / wrapper[0]));
        if (playerUuids.size() == 1) {
            document.put("lastUsername", (Object)FrozenUUIDCache.name((UUID)playerUuids.iterator().next()));
        }
        try {
            MongoUtils.getCollection(MONGO_COLLECTION_NAME).updateOne(new Document("players", playerUuids.stream().map(UUID::toString).collect(Collectors.toSet())), (Bson)new Document("$set", document), MongoUtils.UPSERT_OPTIONS);
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    @Command(names={"recalcGlobalElo"}, permission="op")
    public static void recalcGlobalElo(Player sender) {
        List documents = MongoUtils.getCollection(MONGO_COLLECTION_NAME).find().into(new ArrayList());
        sender.sendMessage(ChatColor.GREEN + "Recalculating " + documents.size() + " players global elo...");
        int[] wrapper = new int[2];
        documents.forEach(document -> {
            try {
                UUID uuid = UUID.fromString((String)((ArrayList)((Object)document.get((Object)"players", ArrayList.class))).get(0));
                instance.saveElo((Set<UUID>)ImmutableSet.of((Object)uuid), instance.loadElo((Set<UUID>)ImmutableSet.of((Object)uuid)));
                wrapper[0] = wrapper[0] + 1;
                if (wrapper[0] % 100 == 0) {
                    sender.sendMessage(ChatColor.GREEN + "Finished " + wrapper[0] + " out of " + documents.size() + " players...");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                wrapper[1] = wrapper[1] + 1;
            }
        });
    }

    @Override
    public Map<String, Integer> topElo(KitType type2) throws IOException {
        return cachedFormattedElo.getOrDefault(type2 == null ? "GLOBAL" : type2.getId(), (Map<String, Integer>)ImmutableMap.of());
    }

    private void refreshFormattedElo() {
        KitType.getAllTypes().stream().filter(KitType::isSupportsRanked).forEach(type2 -> {
            LinkedHashMap topElo = Maps.newLinkedHashMap();
            this.mapTop10(type2.getId(), topElo);
            this.mapPremiumTop10(type2.getId(), topElo);
            cachedFormattedElo.put(type2.getId(), topElo);
            cachedFormattedPremiumElo.put(type2.getId(), topElo);
        });
        LinkedHashMap topGlobal = Maps.newLinkedHashMap();
        this.mapTop10("GLOBAL", topGlobal);
        this.mapPremiumTop10("GLOBAL", topGlobal);
        cachedFormattedElo.put("GLOBAL", topGlobal);
        cachedFormattedPremiumElo.put("GLOBAL", topGlobal);
    }

    @Override
    public Map<String, Integer> topPremiumElo(KitType type2) {
        return cachedFormattedPremiumElo.getOrDefault(type2 == null ? "GLOBAL" : type2.getId(), (Map<String, Integer>)ImmutableMap.of());
    }

    public void mapTop10(final String kitTypeName, final Map<String, Integer> toInsert) {
        try {
            MongoUtils.getCollection(MONGO_COLLECTION_NAME).find().sort(Sorts.descending(kitTypeName)).limit(10).forEach(new Consumer<Document>(){

                @Override
                public void accept(Document document) {
                    Object eloNumber = document.get(kitTypeName);
                    int elo = eloNumber != null && eloNumber instanceof Number ? ((Number)eloNumber).intValue() : 1000;
                    toInsert.put(PatchedPlayerUtils.getFormattedName(UUID.fromString((String)((ArrayList)((Object)document.get((Object)"players", ArrayList.class))).get(0))), elo);
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void mapPremiumTop10(String kitTypeName, Map<String, Integer> toInsert) {
        try {
            MongoUtils.getCollection(MONGO_COLLECTION_PREMIUM_NAME).find().sort(Sorts.descending(kitTypeName)).limit(10).forEach(document -> {
                Object eloNumber = document.get(kitTypeName);
                int elo = eloNumber instanceof Number ? ((Number)eloNumber).intValue() : 1000;
                toInsert.put(PatchedPlayerUtils.getFormattedName(UUID.fromString((String)((ArrayList)((Object)document.get((Object)"players", ArrayList.class))).get(0))), elo);
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

