/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kittype.menu.manage;

import cc.fyre.potpvp.kittype.KitType;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;

final class SaveKitTypeButton
extends Button {
    private final KitType type;

    SaveKitTypeButton(KitType type2) {
        this.type = type2;
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Save";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click this to save the kit type."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.LIME.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        ItemStack[] fullInv = player.getOpenInventory().getTopInventory().getContents();
        ItemStack[] kitInventory = new ItemStack[28];
        int index = -1;
        for (int x = 2; x <= 5; ++x) {
            for (int z = 2; z < 9; ++z) {
                kitInventory[++index] = fullInv[x * 9 + z];
            }
        }
        this.type.setEditorItems(kitInventory);
        this.type.saveAsync();
        player.closeInventory();
    }
}

