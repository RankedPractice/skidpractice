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

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class CreateCopiesButton
extends Button {
    private final ArenaSchematic schematic;

    CreateCopiesButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull(schematic, "schematic");
    }

    public String getName(Player player) {
        return ChatColor.GREEN + "Create copies of " + this.schematic.getName() + "";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.GREEN.toString() + ChatColor.BOLD + "CLICK " + ChatColor.GREEN + "to create 1 new copy"), (ChatColor.GREEN.toString() + ChatColor.BOLD + "SHIFT-CLICK " + ChatColor.GREEN + "to create 10 new copies"), "", (ChatColor.AQUA + "Scale directly to a desired quantity"), (ChatColor.AQUA + "with /arena scale " + this.schematic.getName() + " <count>"));
    }

    public Material getMaterial(Player player) {
        return Material.EMERALD_BLOCK;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        int existing = arenaHandler.countArenas(this.schematic);
        int create = clickType.isShiftClick() ? 10 : 1;
        int desired = existing + create;
        if (arenaHandler.getGrid().isBusy()) {
            player.sendMessage(ChatColor.RED + "Grid is busy.");
            return;
        }
        try {
            player.sendMessage(ChatColor.GREEN + "Starting...");
            arenaHandler.getGrid().scaleCopies(this.schematic, desired, () -> player.sendMessage(ChatColor.GREEN + "Scaled " + this.schematic.getName() + " to " + desired + "."));
        }
        catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "Failed to paste " + this.schematic.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

