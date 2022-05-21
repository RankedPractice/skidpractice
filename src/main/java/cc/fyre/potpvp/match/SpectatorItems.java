/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.match;

import cc.fyre.potpvp.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class SpectatorItems {
    public static final ItemStack SHOW_SPECTATORS_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.GRAY.getDyeData());
    public static final ItemStack HIDE_SPECTATORS_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.LIME.getDyeData());
    public static final ItemStack VIEW_INVENTORY_ITEM = new ItemStack(Material.BOOK);
    public static final ItemStack RETURN_TO_LOBBY_ITEM = new ItemStack(Material.FIRE);
    public static final ItemStack LEAVE_PARTY_ITEM = new ItemStack(Material.FIRE);

    private SpectatorItems() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        ItemUtils.setDisplayName(SHOW_SPECTATORS_ITEM, ChatColor.YELLOW + "Show spectators");
        ItemUtils.setDisplayName(HIDE_SPECTATORS_ITEM, ChatColor.YELLOW + "Hide spectators");
        ItemUtils.setDisplayName(VIEW_INVENTORY_ITEM, ChatColor.YELLOW + "View player inventory");
        ItemUtils.setDisplayName(RETURN_TO_LOBBY_ITEM, ChatColor.YELLOW + "Return to lobby");
        ItemUtils.setDisplayName(LEAVE_PARTY_ITEM, ChatColor.YELLOW + "Leave party");
    }
}

