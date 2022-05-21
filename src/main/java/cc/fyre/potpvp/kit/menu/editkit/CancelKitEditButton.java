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
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.kit.menu.kits.KitsMenu;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class CancelKitEditButton
extends Button {
    private final KitType kitType;

    CancelKitEditButton(KitType kitType) {
        this.kitType = (KitType)Preconditions.checkNotNull(kitType, "kitType");
    }

    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Cancel";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click this to abort editing your kit,"), (ChatColor.YELLOW + "and return to the kit menu."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.RED.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        InventoryUtils.resetInventoryDelayed(player);
        new KitsMenu(this.kitType).openMenu(player);
    }
}

