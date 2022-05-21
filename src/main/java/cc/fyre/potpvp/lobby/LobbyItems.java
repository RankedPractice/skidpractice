/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.lobby;

import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class LobbyItems {
    public static final ItemStack CREATE_PARTY_ITEM = new ItemStack(Material.NETHER_STAR);
    public static final ItemStack SPECTATE_RANDOM_ITEM = new ItemStack(Material.COMPASS);
    public static final ItemStack SPECTATE_MENU_ITEM = new ItemStack(Material.PAPER);
    public static final ItemStack EVENTS_ITEM = new ItemStack(Material.EMERALD);
    public static final ItemStack ENABLE_SPEC_MODE_ITEM = new ItemStack(Material.REDSTONE_TORCH_ON);
    public static final ItemStack DISABLE_SPEC_MODE_ITEM = new ItemStack(Material.LEVER);
    public static final ItemStack UNFOLLOW_ITEM = new ItemStack(Material.INK_SACK, 1, (short)DyeColor.RED.getDyeData());
    public static final ItemStack PLAYER_STATISTICS = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

    private LobbyItems() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        ItemUtils.setDisplayName(CREATE_PARTY_ITEM, ChatColor.BLUE.toString() + "Create Team");
        ItemUtils.setDisplayName(SPECTATE_RANDOM_ITEM, ChatColor.GOLD + "Spectate Random Match");
        ItemUtils.setDisplayName(SPECTATE_MENU_ITEM, ChatColor.GOLD + "Spectate Menu");
        ItemUtils.setDisplayName(EVENTS_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Events" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(ENABLE_SPEC_MODE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.AQUA + ChatColor.BOLD + "Enable Spectator Mode" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(DISABLE_SPEC_MODE_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + PotPvPLang.LEFT_ARROW + ChatColor.AQUA + ChatColor.BOLD + "Disable Spectator Mode" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(UNFOLLOW_ITEM, ChatColor.BLUE.toString() + ChatColor.BOLD + ChatColor.RED + "Stop Following" + ChatColor.BLUE + ChatColor.BOLD + PotPvPLang.RIGHT_ARROW);
        ItemUtils.setDisplayName(PLAYER_STATISTICS, ChatColor.LIGHT_PURPLE.toString() + "Statistics");
    }
}

