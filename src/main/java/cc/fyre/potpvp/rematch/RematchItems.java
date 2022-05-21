/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.rematch;

import cc.fyre.potpvp.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class RematchItems {
    public static final ItemStack REQUEST_REMATCH_ITEM = new ItemStack(Material.DIAMOND);
    public static final ItemStack SENT_REMATCH_ITEM = new ItemStack(Material.DIAMOND);
    public static final ItemStack ACCEPT_REMATCH_ITEM = new ItemStack(Material.DIAMOND);

    private RematchItems() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        ItemUtils.setDisplayName(REQUEST_REMATCH_ITEM, ChatColor.DARK_PURPLE + "Request Rematch");
        ItemUtils.setDisplayName(SENT_REMATCH_ITEM, ChatColor.GREEN + "Sent Rematch");
        ItemUtils.setDisplayName(ACCEPT_REMATCH_ITEM, ChatColor.GREEN + "Accept Rematch");
    }
}

