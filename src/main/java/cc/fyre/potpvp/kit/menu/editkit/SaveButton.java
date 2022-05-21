/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.menu.kits.KitsMenu;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.ItemUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;

final class SaveButton
extends Button {
    private final Kit kit;

    SaveButton(Kit kit) {
        this.kit = (Kit)Preconditions.checkNotNull(kit, "kit");
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Save";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click this to save your kit."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.LIME.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        this.kit.setInventoryContents(player.getInventory().getContents());
        PotPvP.getInstance().getKitHandler().saveKitsAsync(player);
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.closeInventory();
        InventoryUtils.resetInventoryDelayed(player);
        new KitsMenu(this.kit.getType()).openMenu(player);
        ItemStack[] defaultInventory = this.kit.getType().getDefaultInventory();
        int foodInDefault = ItemUtils.countStacksMatching(defaultInventory, v -> v.getType().isEdible());
        int pearlsInDefault = ItemUtils.countStacksMatching(defaultInventory, v -> v.getType() == Material.ENDER_PEARL);
        if (foodInDefault > 0 && this.kit.countFood() == 0) {
            player.sendMessage(ChatColor.RED + "Your saved kit is missing food.");
        }
        if (pearlsInDefault > 0 && this.kit.countPearls() == 0) {
            player.sendMessage(ChatColor.RED + "Your saved kit is missing enderpearls.");
        }
    }
}

