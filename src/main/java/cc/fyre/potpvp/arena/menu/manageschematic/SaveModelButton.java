/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.arena.menu.manageschematic;

import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.WorldEditUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class SaveModelButton
extends Button {
    private final ArenaSchematic schematic;

    SaveModelButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull(schematic, "schematic");
    }

    public String getName(Player player) {
        return ChatColor.GOLD + "Save model";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.YELLOW + "Click to save the model arena"));
    }

    public Material getMaterial(Player player) {
        return Material.PISTON_BASE;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        try {
            WorldEditUtils.save(this.schematic, this.schematic.getModelArenaLocation());
        }
        catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "Failed to save " + this.schematic.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

