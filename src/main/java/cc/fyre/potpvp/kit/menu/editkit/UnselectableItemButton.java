/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

final class UnselectableItemButton
extends Button {
    UnselectableItemButton() {
    }

    public String getName(Player player) {
        return ChatColor.RED + "You can only reorganize your inventory.";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((Object)(ChatColor.RED + "No items can be changed in this kit."));
    }

    public Material getMaterial(Player player) {
        return Material.STAINED_GLASS_PANE;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.GRAY.getWoolData();
    }
}

