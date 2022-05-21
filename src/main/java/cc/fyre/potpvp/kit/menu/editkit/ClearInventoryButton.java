/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.PotPvP;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.menu.Button;

final class ClearInventoryButton
extends Button {
    ClearInventoryButton() {
    }

    public String getName(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Clear Inventory";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "This will clear your inventory"), (ChatColor.YELLOW + "so you can start over."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.YELLOW.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.getInventory().clear();
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }
}

