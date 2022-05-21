/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class QueueItems {
    public static final ItemStack JOIN_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_SOLO_UNRANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.RED.getDyeData());
    public static final ItemStack JOIN_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_SOLO_RANKED_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.RED.getDyeData());
    public static final ItemStack JOIN_SOLO_PREMIUM_QUEUE_ITEM = new ItemStack(Material.GOLD_SWORD);
    public static final ItemStack LEAVE_SOLO_PREMIUM_QUEUE_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.RED.getDyeData());
    public static final ItemStack JOIN_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.IRON_SWORD);
    public static final ItemStack LEAVE_PARTY_UNRANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);
    public static final ItemStack JOIN_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack LEAVE_PARTY_RANKED_QUEUE_ITEM = new ItemStack(Material.ARROW);

    static {
        ItemUtils.setDisplayName(JOIN_SOLO_UNRANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.GREEN + ChatColor.BOLD + "Join " + ChatColor.GRAY + ChatColor.BOLD + "Unranked " + ChatColor.GREEN + ChatColor.BOLD + "Queue" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_UNRANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.RED + ChatColor.BOLD + "Leave Unranked Queue" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(JOIN_SOLO_RANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.GREEN + ChatColor.BOLD + "Join " + ChatColor.AQUA + ChatColor.BOLD + "Ranked " + ChatColor.GREEN + ChatColor.BOLD + "Queue" + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(LEAVE_SOLO_RANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.RED + ChatColor.BOLD + "Leave Ranked Queue" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(JOIN_PARTY_UNRANKED_QUEUE_ITEM, ChatColor.BLUE + "Un-Ranked 2v2 Queue");
        ItemUtils.setDisplayName(LEAVE_PARTY_UNRANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.RED + ChatColor.BOLD + "Leave Unranked 2v2 Queue" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(JOIN_PARTY_RANKED_QUEUE_ITEM, ChatColor.GREEN + "Ranked 2v2 Queue");
        ItemUtils.setDisplayName(LEAVE_PARTY_RANKED_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.RED + ChatColor.BOLD + "Leave Ranked 2v2 Queue" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(JOIN_SOLO_PREMIUM_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + "\u00bb " + ChatColor.GREEN + ChatColor.BOLD + "Join " + ChatColor.RED + ChatColor.BOLD + "Premium" + ChatColor.GREEN + ChatColor.BOLD + " Queue" + ChatColor.BLUE + ChatColor.BOLD + " \u00ab");
        ItemUtils.setDisplayName(LEAVE_SOLO_PREMIUM_QUEUE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + "\u00bb " + ChatColor.RED + ChatColor.BOLD + "Leave Premium Queue" + ChatColor.BLUE + ChatColor.BOLD + " \u00ab");
    }
}

