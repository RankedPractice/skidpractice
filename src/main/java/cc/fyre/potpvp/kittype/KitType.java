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
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.MongoUtils;
import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.qLib;

public final class KitType {

    private static final String MONGO_COLLECTION_NAME = "kitTypes";
    public static final List<KitType> allTypes = new ArrayList<>();
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
    public static KitType nodebuff = new KitType();
    public static KitType debuff = new KitType();
    public static KitType archer = new KitType();
    public static KitType combo = new KitType();
    public static KitType builduchc = new KitType();
    public static KitType sumo = new KitType();
    public static KitType spleef = new KitType();
    public static KitType baseraiding = new KitType();
    public static KitType hcf = new KitType();
    public static KitType hcfbard = new KitType();
    public static KitType hcfarcher = new KitType();
    public static KitType hcfdiamond = new KitType();
    public static KitType soup = new KitType();
    public static KitType boxing = new KitType();
    public static KitType battlerush = new KitType();
    public static KitType skywars = new KitType();
    public static KitType wizard = new KitType();
    public static KitType bridges = new KitType();
    public static KitType pearlFight = new KitType();
    public static KitType bedFight = new KitType();

    static {
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);

        collection.find().iterator().forEachRemaining(doc -> {
            allTypes.add(qLib.PLAIN_GSON.fromJson(doc.toJson(), KitType.class));
        });

        nodebuff.icon = new MaterialData(Material.DIAMOND_SWORD);
        nodebuff.id = "NoDebuff";
        nodebuff.displayName = CC.translate("&aNo Debuff");
        nodebuff.displayColor = ChatColor.GREEN;

        battlerush.icon = new MaterialData(Material.WATER_LILY);
        battlerush.id = "BattleRush";
        battlerush.displayName = CC.translate("&aBattle Rush");
        battlerush.displayColor = ChatColor.BOLD;

        bedFight.icon = new MaterialData(Material.BED);
        bedFight.id = "BedFight";
        bedFight.displayName = CC.translate("&aBed Fight");
        bedFight.displayColor = ChatColor.BOLD;

        debuff.icon = new MaterialData(Material.POTION);
        debuff.id = "Debuff";
        debuff.displayName = CC.translate("&aDebuff");
        debuff.displayColor = ChatColor.GREEN;

        pearlFight.icon = new MaterialData(Material.ENDER_PEARL);
        pearlFight.id = "PearlFight";
        pearlFight.displayName = CC.translate("&aPearl Fight");
        pearlFight.displayColor = ChatColor.BOLD;

        archer.icon = new MaterialData(Material.BOW);
        archer.id = "Archer";
        archer.displayName = CC.translate("&aArcher");
        archer.displayColor = ChatColor.GREEN;

        combo.icon = new MaterialData(Material.RED_ROSE);
        combo.id = "Combo";
        combo.displayName = CC.translate("&aCombo");
        combo.displayColor = ChatColor.GREEN;

        builduchc.icon = new MaterialData(Material.GOLDEN_APPLE);
        builduchc.id = "BuildUHC";
        builduchc.displayName = CC.translate("&aBuild UHC");
        builduchc.displayColor = ChatColor.GREEN;

        sumo.icon = new MaterialData(Material.LEASH);
        sumo.id = "Sumo";
        sumo.displayName = CC.translate("&aSumo");
        sumo.displayColor = ChatColor.GREEN;

        baseraiding.icon = new MaterialData(Material.BLAZE_POWDER);
        baseraiding.id = "HCFTrapping";
        baseraiding.displayName = CC.translate("&aBase Raiding");
        baseraiding.displayColor = ChatColor.BOLD;

        hcf.icon = new MaterialData(Material.DIAMOND_HELMET);
        hcf.id = "HCF";
        hcf.displayName = CC.translate("&aHCF");
        hcf.displayColor = ChatColor.GREEN;

        spleef.icon = new MaterialData(Material.DIAMOND_SPADE);
        spleef.id = "Spleef";
        spleef.displayName = CC.translate("&aSpleef");
        spleef.displayColor = ChatColor.GREEN;

        hcfdiamond.icon = new MaterialData(Material.DIAMOND_CHESTPLATE);
        hcfdiamond.id = "DIAMOND_HCF";
        hcfdiamond.displayName = "Diamond Class";
        hcfdiamond.displayColor = ChatColor.GREEN;

        hcfarcher.icon = new MaterialData(Material.LEATHER_CHESTPLATE);
        hcfarcher.id = "ARCHER_HCF";
        hcfarcher.displayName = "Archer Class";
        hcfarcher.displayColor = ChatColor.GREEN;

        hcfbard.icon = new MaterialData(Material.GOLD_CHESTPLATE);
        hcfbard.id = "BARD_HCF";
        hcfbard.displayName = "Bard Class";
        hcfbard.displayColor = ChatColor.GREEN;

        boxing.icon = new MaterialData(Material.STICK);
        boxing.id = "Boxing";
        boxing.displayName = CC.translate("&aBoxing");
        boxing.displayColor = ChatColor.BOLD;

        skywars.icon = new MaterialData(Material.GRASS);
        skywars.id = "SkyWars";
        skywars.displayName = CC.translate("&aSkyWars");
        skywars.displayColor = ChatColor.BOLD;

        bridges.icon = new MaterialData(Material.STAINED_CLAY);
        bridges.id = "Bridges";
        bridges.displayName = CC.translate("&aBridges");
        bridges.displayColor = ChatColor.BOLD;

        MaterialData data = new MaterialData(Material.MUSHROOM_SOUP);
        data.toItemStack().addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        soup.icon = data;
        soup.id = "Soup";
        soup.displayName = CC.translate("&aSoup");
        soup.displayColor = ChatColor.GREEN;
        soup.setHealingMethod(HealingMethod.SOUP);

        wizard.icon = new MaterialData(Material.EYE_OF_ENDER);
        wizard.id = "Wizard";
        wizard.displayName = CC.translate("&aWizard");
        wizard.displayColor = ChatColor.BOLD;

        if (!allTypes.contains(byId("NoDebuff"))) {
            allTypes.add(nodebuff);
        }
        if (!allTypes.contains(byId("Debuff"))) {
            allTypes.add(debuff);
        }
        if (!allTypes.contains(byId("Wizard"))) {
            allTypes.add(wizard);
        }
        if (!allTypes.contains(byId("PearlFight"))) {
            allTypes.add(pearlFight);
        }
        if (!allTypes.contains(byId("BattleRush"))) {
            allTypes.add(battlerush);
        }
        if (!allTypes.contains(byId("BedFight"))) {
            allTypes.add(bedFight);
        }
        if (!allTypes.contains(byId("HCFTrapping"))) {
            allTypes.add(baseraiding);
        }
        if (!allTypes.contains(byId("SkyWars"))) {
            allTypes.add(skywars);
        }
        if (!allTypes.contains(byId("Bridges"))) {
            allTypes.add(bridges);
        }
        if (!allTypes.contains(byId("Boxing"))) {
            allTypes.add(boxing);
        }
        if (!allTypes.contains(byId("Sumo"))) {
            allTypes.add(sumo);
        }
        if (!allTypes.contains(byId("HCF"))) {
            allTypes.add(hcf);
        }
        if (!allTypes.contains(byId("BuildUHC"))) {
            allTypes.add(builduchc);
        }
        if (!allTypes.contains(byId("Spleef"))) {
            allTypes.add(spleef);
        }
        if (!allTypes.contains(byId("Combo"))) {
            allTypes.add(combo);
        }
        if (!allTypes.contains(byId("Archer"))) {
            allTypes.add(archer);
        }
        if (!allTypes.contains(byId("DIAMOND_HCF"))) {
            allTypes.add(hcfdiamond);
        }
        if (!allTypes.contains(byId("BARD_HCF"))) {
            allTypes.add(hcfbard);
        }
        if (!allTypes.contains(byId("ARCHER_HCF"))) {
            allTypes.add(hcfarcher);
        }
        if (!allTypes.contains(byId("Soup"))) {
            allTypes.add(soup);
        }
        allTypes.sort(Comparator.comparing(KitType::getSort));
    }

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
}

