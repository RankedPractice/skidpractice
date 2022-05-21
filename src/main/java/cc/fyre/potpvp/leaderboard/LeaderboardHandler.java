/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.io.Files
 *  com.google.gson.reflect.TypeToken
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.bridge.BridgeGlobal
 *  rip.bridge.bridge.global.profile.Profile
 *  rip.bridge.qlib.hologram.FrozenHologramHandler
 *  rip.bridge.qlib.hologram.construct.Hologram
 */
package cc.fyre.potpvp.leaderboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.leaderboard.LeaderboardEntry;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.hologram.FrozenHologramHandler;
import rip.bridge.qlib.hologram.construct.Hologram;

public class LeaderboardHandler {
    private static final String LEADERBOARDS_FILE_NAME = "leaderboards.json";
    private Map<String, Location> hologramLocations = new HashMap<String, Location>();
    private List<Hologram> holograms = new ArrayList<Hologram>();
    private List<LeaderboardEntry> globalLeaderboard = null;
    private Map<String, List<LeaderboardEntry>> kitTypeLeaderboards = new HashMap<String, List<LeaderboardEntry>>();

    public void load() {
        File leaderboardsFile = new File(PotPvP.getInstance().getDataFolder(), LEADERBOARDS_FILE_NAME);
        if (leaderboardsFile.exists()) {
            try (BufferedReader reader = Files.newReader((File)leaderboardsFile, (Charset)Charsets.UTF_8);){
                Type mapType = new TypeToken<Map<String, Location>>(){}.getType();
                this.hologramLocations = (Map)PotPvP.gson.fromJson((Reader)reader, mapType);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        PotPvP.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            this.loadLeaderboards();
            if (this.holograms.isEmpty()) {
                PotPvP.getInstance().getServer().getScheduler().runTask((Plugin)PotPvP.getInstance(), this::loadHolograms);
            }
            PotPvP.getInstance().getLogger().info("Loaded leaderboards...");
        }, 0L, 2400L);
    }

    private FindIterable<Document> fetchDocuments() {
        Document query = new Document().append("players", new Document("$size", 1));
        MongoCollection<Document> collection = PotPvP.getInstance().getMongoDatabase().getCollection("elo");
        return collection.find(query);
    }

    private List<LeaderboardEntry> mapTop10(String field, FindIterable<Document> documents) {
        ArrayList<Document> sortedDocuments = new ArrayList<Document>();
        for (Document document : documents) {
            if (!document.containsKey(field)) continue;
            sortedDocuments.add(document);
        }
        sortedDocuments.sort(Comparator.comparingInt(o -> o.getInteger(field)).reversed());
        ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();
        for (Document document : sortedDocuments) {
            UUID playerId = UUID.fromString(document.getList("players", String.class).get(0));
            Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUIDOrCreate(playerId);
            LeaderboardEntry leaderboardEntry = new LeaderboardEntry(leaderboardEntries.size(), UUID.fromString(document.getList("players", String.class).get(0)), profile.getColor() + profile.getUsername(), document.getInteger(field));
            leaderboardEntries.add(leaderboardEntry);
            if (leaderboardEntries.size() <= 9) continue;
            break;
        }
        return leaderboardEntries;
    }

    void loadLeaderboards() {
        FindIterable<Document> documents = this.fetchDocuments();
        this.globalLeaderboard = this.mapTop10("GLOBAL", documents);
        for (KitType kitType : KitType.getAllTypes()) {
            this.kitTypeLeaderboards.put(kitType.id, this.mapTop10(kitType.id, documents));
        }
    }

    void loadHolograms() {
        for (Hologram hologram : this.holograms) {
            hologram.destroy();
        }
        this.holograms.clear();
        if (this.hologramLocations.containsKey("GLOBAL")) {
            this.createHolograms(this.hologramLocations.get("GLOBAL"), "Global", () -> this.globalLeaderboard);
        }
        for (KitType kitType : KitType.getAllTypes()) {
            if (!this.hologramLocations.containsKey(kitType.id)) continue;
            this.createHolograms(this.hologramLocations.get(kitType.id), kitType.getDisplayName(), () -> this.kitTypeLeaderboards.get(kitType.id));
        }
    }

    private void createHolograms(Location location, String title, Supplier<List<LeaderboardEntry>> leaderboardConsumer) {
        Hologram hologram = FrozenHologramHandler.createHologram().at(location).updates().interval(10L, TimeUnit.SECONDS).onUpdate(updatingHologram -> {
            List leaderboard = (List)leaderboardConsumer.get();
            updatingHologram.setLine(0, ChatColor.BLUE.toString() + ChatColor.BOLD + title + " Leaderboard");
            if (leaderboard == null) {
                updatingHologram.setLine(1, ChatColor.YELLOW + "Loading...");
                return;
            }
            for (LeaderboardEntry entry : leaderboard) {
                String positionPart = ChatColor.YELLOW.toString() + (entry.getPosition() + 1) + ". ";
                String valuePart = ChatColor.YELLOW.toString() + entry.getValue();
                updatingHologram.setLine(entry.getPosition() + 1, positionPart + ChatColor.RESET + entry.getDisplayName() + " - " + valuePart);
            }
        }).build();
        this.holograms.add(hologram);
        hologram.send();
    }

    public void writeFile() {
        try {
            Files.write((CharSequence)PotPvP.gson.toJson(this.hologramLocations), (File)new File(PotPvP.getInstance().getDataFolder(), LEADERBOARDS_FILE_NAME), (Charset)Charsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Location> getHologramLocations() {
        return this.hologramLocations;
    }

    public List<Hologram> getHolograms() {
        return this.holograms;
    }
}

