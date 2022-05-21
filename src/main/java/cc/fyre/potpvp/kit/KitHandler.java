/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.gson.reflect.TypeToken
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.kit;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.listener.KitEditorListener;
import cc.fyre.potpvp.kit.listener.KitItemListener;
import cc.fyre.potpvp.kit.listener.KitLoadListener;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.util.MongoUtils;
import com.google.common.collect.ImmutableList;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class KitHandler {
    public static final String MONGO_COLLECTION_NAME = "playerKits";
    public static final int KITS_PER_TYPE = 4;
    private final Map<UUID, List<Kit>> kitData = new ConcurrentHashMap<UUID, List<Kit>>();

    public KitHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new KitEditorListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new KitItemListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new KitLoadListener(), (Plugin)PotPvP.getInstance());
    }

    public List<Kit> getKits(Player player, KitType kitType) {
        ArrayList<Kit> kits = new ArrayList<Kit>();
        for (Kit kit : this.kitData.getOrDefault(player.getUniqueId(), ImmutableList.of())) {
            if (kit.getType() != kitType) continue;
            kits.add(kit);
        }
        return kits;
    }

    public Optional<Kit> getKit(Player player, KitType kitType, int slot) {
        return this.kitData.getOrDefault(player.getUniqueId(), ImmutableList.of()).stream().filter(k -> k.getType() == kitType && k.getSlot() == slot).findFirst();
    }

    public Kit saveDefaultKit(Player player, KitType kitType, int slot) {
        Kit created = Kit.ofDefaultKit(kitType, "Kit " + slot, slot);
        this.kitData.computeIfAbsent(player.getUniqueId(), i -> new ArrayList()).add(created);
        this.saveKitsAsync(player);
        return created;
    }

    public void removeKit(Player player, KitType kitType, int slot) {
        boolean removed = kitData.computeIfAbsent(player.getUniqueId(), i -> new ArrayList<>())
                .removeIf(k -> k.getType() == kitType && k.getSlot() == slot);

        if (removed) {
            saveKitsAsync(player);
        }
    }

    public void saveKitsAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            List kitJson = (List)JSON.parse(PotPvP.gson.toJson(this.kitData.getOrDefault(player.getUniqueId(), ImmutableList.of())));
            Document query = new Document("_id", player.getUniqueId().toString());
            Document kitUpdate = new Document("$set", new Document("kits", kitJson));
            collection.updateOne(query, (Bson)kitUpdate, MongoUtils.UPSERT_OPTIONS);
        });
    }

    public void wipeKitsForPlayer(UUID target) {
        this.kitData.remove(target);
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        collection.deleteOne(new Document("_id", target.toString()));
    }

    public int wipeKitsWithType(KitType kitType) {
        for (List<Kit> playerKits : this.kitData.values()) {
            playerKits.removeIf(k -> k.getType() == kitType);
        }
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Document typeQuery = new Document("type", kitType.getId());
        collection.updateMany(new Document("kits", new Document("$elemMatch", typeQuery)), new Document("$pull", new Document("kits", typeQuery)));
        return -1;
    }

    public void loadKits(UUID playerUuid) {
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Document playerKits = (Document)collection.find(new Document("_id", playerUuid.toString())).first();
        if (playerKits != null) {
            List kits = (List)(playerKits.get("kits", List.class));
            Type listKit = new TypeToken<List<Kit>>(){}.getType();
            this.kitData.put(playerUuid, (List)PotPvP.gson.fromJson(JSON.serialize(kits), listKit));
        }
    }

    public void unloadKits(Player player) {
        this.kitData.remove(player.getUniqueId());
    }
}

