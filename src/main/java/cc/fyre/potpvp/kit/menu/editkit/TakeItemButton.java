/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.PotPvP;
import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.menu.Button;

final class TakeItemButton
extends Button {
    private final ItemStack item;

    TakeItemButton(ItemStack item) {
        this.item = (ItemStack)Preconditions.checkNotNull((Object)item, (Object)"item");
    }

    public ItemStack getButtonItem(Player player) {
        return this.item;
    }

    public String getName(Player player) {
        return null;
    }

    public List<String> getDescription(Player player) {
        return null;
    }

    public Material getMaterial(Player player) {
        return null;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> player.getOpenInventory().getTopInventory().setItem(slot, this.item), 4L);
    }

    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return false;
    }
}

