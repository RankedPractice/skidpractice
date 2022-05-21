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

final class RemoveCopiesButton
extends Button {
    private final ArenaSchematic schematic;

    RemoveCopiesButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull((Object)schematic, (Object)"schematic");
    }

    public String getName(Player player) {
        return ChatColor.RED + "Remove copies of " + this.schematic.getName() + "";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((Object)"", (Object)(ChatColor.RED.toString() + ChatColor.BOLD + "CLICK " + ChatColor.RED + "to remove 1 copy"), (Object)(ChatColor.RED.toString() + ChatColor.BOLD + "SHIFT-CLICK " + ChatColor.RED + "to remove 10 copies"), (Object)"", (Object)(ChatColor.AQUA + "Scale directly to a desired quantity"), (Object)(ChatColor.AQUA + "with /arena scale " + this.schematic.getName() + " <count>"));
    }

    public Material getMaterial(Player player) {
        return Material.REDSTONE_BLOCK;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        int existing = arenaHandler.countArenas(this.schematic);
        int remove = clickType.isShiftClick() ? 10 : 1;
        int desired = Math.max(existing - remove, 0);
        if (arenaHandler.getGrid().isBusy()) {
            player.sendMessage(ChatColor.RED + "Grid is busy.");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Starting...");
        arenaHandler.getGrid().scaleCopies(this.schematic, desired, () -> player.sendMessage(ChatColor.GREEN + "Scaled " + this.schematic.getName() + " to " + desired + "."));
    }
}

