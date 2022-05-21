/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.util.ItemBuilder
 */
package cc.fyre.potpvp.kit;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.util.ItemUtils;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.util.ItemBuilder;

public final class Kit {
    private String name;
    private int slot;
    private KitType type;
    private ItemStack[] inventoryContents;

    public static Kit ofDefaultKitCustomName(KitType kitType, String name) {
        return Kit.ofDefaultKit(kitType, name, 0);
    }

    public static Kit ofDefaultKit(KitType kitType) {
        return Kit.ofDefaultKit(kitType, "Default Kit", 0);
    }

    public static Kit ofDefaultKit(KitType kitType, String name, int slot) {
        Kit kit = new Kit();
        kit.setName(name);
        kit.setType(kitType);
        kit.setSlot(slot);
        kit.setInventoryContents(kitType.getDefaultInventory());
        return kit;
    }

    public void apply(Player player) {
        PatchedPlayerUtils.resetInventory(player);
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        if (match.getKitType().getId().equalsIgnoreCase("Bridges") || match.getKitType().getId().equalsIgnoreCase("bedfight")) {
            ItemStack redHelmet = ItemBuilder.of((Material)Material.LEATHER_HELMET).color(Color.RED).build();
            ItemStack redChestplate = ItemBuilder.of((Material)Material.LEATHER_CHESTPLATE).color(Color.RED).build();
            ItemStack redLeggings = ItemBuilder.of((Material)Material.LEATHER_LEGGINGS).color(Color.RED).build();
            ItemStack redBoots = ItemBuilder.of((Material)Material.LEATHER_BOOTS).color(Color.RED).build();
            ItemStack blueHelmet = ItemBuilder.of((Material)Material.LEATHER_HELMET).color(Color.BLUE).build();
            ItemStack blueChestplate = ItemBuilder.of((Material)Material.LEATHER_CHESTPLATE).color(Color.BLUE).build();
            ItemStack blueLeggings = ItemBuilder.of((Material)Material.LEATHER_LEGGINGS).color(Color.BLUE).build();
            ItemStack blueBoots = ItemBuilder.of((Material)Material.LEATHER_BOOTS).color(Color.BLUE).build();
            MatchTeam team = match.getTeam(player.getUniqueId());
            if (team != null) {
                if (team.getId() == 0) {
                    player.getInventory().setHelmet(blueHelmet);
                    player.getInventory().setChestplate(blueChestplate);
                    player.getInventory().setLeggings(blueLeggings);
                    player.getInventory().setBoots(blueBoots);
                } else {
                    player.getInventory().setHelmet(redHelmet);
                    player.getInventory().setChestplate(redChestplate);
                    player.getInventory().setLeggings(redLeggings);
                    player.getInventory().setBoots(redBoots);
                }
            }
        } else {
            player.getInventory().setArmorContents(this.type.getDefaultArmor());
        }
        player.getInventory().setContents(this.inventoryContents);
        ArrayList<Integer> woolSlots = new ArrayList<Integer>();
        int i = 0;
        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null || content.getType() == Material.AIR) {
                ++i;
                continue;
            }
            if (content.getType() == Material.WOOL) {
                woolSlots.add(i);
            }
            ++i;
        }
        for (Integer woolSlot : woolSlots) {
            MatchTeam team = match.getTeam(player.getUniqueId());
            if (team == null) continue;
            if (team.getId() == 0) {
                player.getInventory().setItem(woolSlot.intValue(), new ItemStack(Material.WOOL, 64, 11));
                continue;
            }
            player.getInventory().setItem(woolSlot.intValue(), new ItemStack(Material.WOOL, 64, 14));
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }

    public int countHeals() {
        return ItemUtils.countStacksMatching(this.inventoryContents, ItemUtils.INSTANT_HEAL_POTION_PREDICATE);
    }

    public int countDebuffs() {
        return ItemUtils.countStacksMatching(this.inventoryContents, ItemUtils.DEBUFF_POTION_PREDICATE);
    }

    public int countFood() {
        return ItemUtils.countStacksMatching(this.inventoryContents, ItemUtils.EDIBLE_PREDICATE);
    }

    public int countPearls() {
        return ItemUtils.countStacksMatching(this.inventoryContents, v -> v.getType() == Material.ENDER_PEARL);
    }

    public boolean isSelectionItem(ItemStack itemStack) {
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD + this.name);
    }

    public ItemStack createSelectionItem() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + this.name);
        item.setItemMeta(itemMeta);
        return item;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public KitType getType() {
        return this.type;
    }

    public void setType(KitType type2) {
        this.type = type2;
    }

    public ItemStack[] getInventoryContents() {
        return this.inventoryContents;
    }

    public void setInventoryContents(ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }
}

