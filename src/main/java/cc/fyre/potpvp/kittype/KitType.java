/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.kittype;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.HealingMethod;
import cc.fyre.potpvp.util.MongoUtils;
import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.qLib;

public final class KitType {
    private static final String MONGO_COLLECTION_NAME = "kitTypes";
    private static final MongoCollection<Document> collection;
    public static final List<KitType> allTypes;
    public static KitType teamFight;
    @SerializedName(value="_id")
    public String id;
    private String displayName;
    private ChatColor displayColor;
    private MaterialData icon;
    private ItemStack[] editorItems = new ItemStack[0];
    private ItemStack[] defaultArmor = new ItemStack[0];
    private ItemStack[] defaultInventory = new ItemStack[0];
    private boolean editorSpawnAllowed = true;
    private boolean hidden = false;
    private HealingMethod healingMethod = HealingMethod.POTIONS;
    private boolean buildingAllowed = false;
    private boolean healthShown = false;
    private boolean hardcoreHealing = false;
    private boolean pearlDamage = true;
    private int sort = 0;
    private boolean supportsRanked = false;
    private boolean supportsPremium = false;
    private String knockbackName = null;

    public static KitType byId(String id) {
        for (KitType kitType : allTypes) {
            if (!kitType.getId().equalsIgnoreCase(id)) continue;
            return kitType;
        }
        return null;
    }

    public String getColoredDisplayName() {
        return this.displayColor + this.displayName;
    }

    public void deleteAsync() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            collection.deleteOne(new Document("_id", this.id));
        });
    }

    public void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            Document kitTypeDoc = Document.parse(qLib.PLAIN_GSON.toJson((Object)this));
            kitTypeDoc.remove("_id");
            Document query = new Document("_id", this.id);
            Document kitUpdate = new Document("$set", kitTypeDoc);
            collection.updateOne(query, (Bson)kitUpdate, MongoUtils.UPSERT_OPTIONS);
        });
    }

    public String toString() {
        return this.displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public MaterialData getIcon() {
        return this.icon;
    }

    public ItemStack[] getDefaultArmor() {
        return this.defaultArmor;
    }

    public ItemStack[] getDefaultInventory() {
        return this.defaultInventory;
    }

    public static List<KitType> getAllTypes() {
        return allTypes;
    }

    public String getId() {
        return this.id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ChatColor getDisplayColor() {
        return this.displayColor;
    }

    public void setDisplayColor(ChatColor displayColor) {
        this.displayColor = displayColor;
    }

    public void setIcon(MaterialData icon) {
        this.icon = icon;
    }

    public ItemStack[] getEditorItems() {
        return this.editorItems;
    }

    public void setEditorItems(ItemStack[] editorItems) {
        this.editorItems = editorItems;
    }

    public void setDefaultArmor(ItemStack[] defaultArmor) {
        this.defaultArmor = defaultArmor;
    }

    public void setDefaultInventory(ItemStack[] defaultInventory) {
        this.defaultInventory = defaultInventory;
    }

    public boolean isEditorSpawnAllowed() {
        return this.editorSpawnAllowed;
    }

    public void setEditorSpawnAllowed(boolean editorSpawnAllowed) {
        this.editorSpawnAllowed = editorSpawnAllowed;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public HealingMethod getHealingMethod() {
        return this.healingMethod;
    }

    public void setHealingMethod(HealingMethod healingMethod) {
        this.healingMethod = healingMethod;
    }

    public boolean isBuildingAllowed() {
        return this.buildingAllowed;
    }

    public void setBuildingAllowed(boolean buildingAllowed) {
        this.buildingAllowed = buildingAllowed;
    }

    public boolean isHealthShown() {
        return this.healthShown;
    }

    public void setHealthShown(boolean healthShown) {
        this.healthShown = healthShown;
    }

    public boolean isHardcoreHealing() {
        return this.hardcoreHealing;
    }

    public void setHardcoreHealing(boolean hardcoreHealing) {
        this.hardcoreHealing = hardcoreHealing;
    }

    public boolean isPearlDamage() {
        return this.pearlDamage;
    }

    public void setPearlDamage(boolean pearlDamage) {
        this.pearlDamage = pearlDamage;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isSupportsRanked() {
        return this.supportsRanked;
    }

    public boolean isSupportsPremium() {
        return this.supportsPremium;
    }

    public void setSupportsRanked(boolean supportsRanked) {
        this.supportsRanked = supportsRanked;
    }

    public void setSupportsPremium(boolean supportsPremium) {
        this.supportsPremium = supportsPremium;
    }

    public String getKnockbackName() {
        return this.knockbackName;
    }

    public void setKnockbackName(String knockbackName) {
        this.knockbackName = knockbackName;
    }

    static {
        KitType type2;
        collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        allTypes = new ArrayList<KitType>();
        teamFight = new KitType();
        collection.find().iterator().forEachRemaining(doc -> allTypes.add((KitType)qLib.PLAIN_GSON.fromJson(doc.toJson(), KitType.class)));
        if (KitType.byId("NODEBUFF") == null) {
            type2 = new KitType();
            type2.icon = new MaterialData(Material.DIAMOND_SWORD);
            type2.id = "nodebuff";
            type2.displayName = "No Debuff";
            type2.displayColor = ChatColor.GOLD;
            allTypes.add(type2);
        }
        if (KitType.byId("SOUP") == null) {
            type2 = new KitType();
            type2.icon = new MaterialData(Material.MUSHROOM_SOUP);
            type2.id = "soup";
            type2.displayName = "Soup";
            type2.displayColor = ChatColor.GOLD;
            allTypes.add(type2);
        }
        if (KitType.byId("AXE") == null) {
            type2 = new KitType();
            type2.icon = new MaterialData(Material.IRON_AXE);
            type2.id = "axe";
            type2.displayName = "Axe";
            type2.displayColor = ChatColor.GOLD;
            allTypes.add(type2);
        }
        if (KitType.byId("SG") == null) {
            type2 = new KitType();
            type2.icon = new MaterialData(Material.FISHING_ROD);
            type2.id = "sg";
            type2.displayName = "Survival Games";
            type2.displayColor = ChatColor.GOLD;
            allTypes.add(type2);
        }
        KitType.teamFight.icon = new MaterialData(Material.BEACON);
        KitType.teamFight.id = "hcf_teamfight";
        KitType.teamFight.displayName = "HCF Team Fight";
        KitType.teamFight.displayColor = ChatColor.AQUA;
        allTypes.sort(Comparator.comparing(KitType::getSort));
    }
}

