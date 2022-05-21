/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.menu.Button;

final class LoadDefaultKitButton
extends Button {
    private final KitType kitType;

    LoadDefaultKitButton(KitType kitType) {
        this.kitType = (KitType)Preconditions.checkNotNull(kitType, "kitType");
    }

    public String getName(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Load default kit";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click this to load the default kit"), (ChatColor.YELLOW + "into the kit editing menu."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.GRAY.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.getInventory().setContents(this.kitType.getDefaultInventory());
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }
}

