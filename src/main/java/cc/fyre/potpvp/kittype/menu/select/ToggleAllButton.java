/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kittype.menu.select;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

public class ToggleAllButton
extends Button {
    private Set<String> allMaps;
    private Set<String> maps;

    public List<String> getDescription(Player arg0) {
        return ImmutableList.of();
    }

    public Material getMaterial(Player arg0) {
        return this.maps.isEmpty() ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF;
    }

    public String getName(Player arg0) {
        return this.maps.isEmpty() ? ChatColor.GREEN + "Enable all maps" : ChatColor.RED + "Disable all maps";
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        if (this.maps.isEmpty()) {
            this.maps.addAll(this.allMaps);
        } else {
            this.maps.clear();
        }
    }

    public ToggleAllButton(Set<String> allMaps, Set<String> maps) {
        this.allMaps = allMaps;
        this.maps = maps;
    }
}

