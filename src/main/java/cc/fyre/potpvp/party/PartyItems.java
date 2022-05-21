/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.party;

import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.util.ItemUtils;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class PartyItems {
    public static final Material ICON_TYPE = Material.NETHER_STAR;
    public static final ItemStack LEAVE_PARTY_ITEM = new ItemStack(Material.FIRE);
    public static final ItemStack ASSIGN_CLASSES = new ItemStack(Material.ITEM_FRAME);
    public static final ItemStack START_TEAM_SPLIT_ITEM = new ItemStack(Material.DIAMOND_SWORD);
    public static final ItemStack START_FFA_ITEM = new ItemStack(Material.GOLD_SWORD);
    public static final ItemStack OTHER_PARTIES_ITEM = new ItemStack(Material.SKULL_ITEM);

    public static ItemStack icon(Party party) {
        ItemStack item = new ItemStack(ICON_TYPE);
        String leaderName = FrozenUUIDCache.name((UUID)party.getLeader());
        String displayName = ChatColor.AQUA.toString() + leaderName + ChatColor.AQUA + "'s Party";
        ItemUtils.setDisplayName(item, displayName);
        return item;
    }

    static {
        ItemUtils.setDisplayName(LEAVE_PARTY_ITEM, ChatColor.RED + "Leave Party");
        ItemUtils.setDisplayName(ASSIGN_CLASSES, ChatColor.GOLD + "HCF Kits");
        ItemUtils.setDisplayName(START_TEAM_SPLIT_ITEM, ChatColor.YELLOW + "Start Team Split");
        ItemUtils.setDisplayName(START_FFA_ITEM, ChatColor.YELLOW + "Start Party FFA");
        ItemUtils.setDisplayName(OTHER_PARTIES_ITEM, ChatColor.GREEN + "Other Parties");
    }
}

