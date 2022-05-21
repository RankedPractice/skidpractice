/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.sk89q.worldedit.Vector
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
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
import com.sk89q.worldedit.Vector;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class TeleportToModelButton
extends Button {
    private final ArenaSchematic schematic;

    TeleportToModelButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull((Object)schematic, (Object)"schematic");
    }

    public String getName(Player player) {
        return ChatColor.GOLD + "Teleport to model";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((Object)"", (Object)(ChatColor.YELLOW + "Click to teleport to the model arena, which"), (Object)(ChatColor.YELLOW + "will allow you to make edits to the schematic."));
    }

    public Material getMaterial(Player player) {
        return Material.BREWING_STAND_ITEM;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        Vector arenaStart = this.schematic.getModelArenaLocation();
        player.teleport(new Location(arenaHandler.getArenaWorld(), arenaStart.getX() + 50.0, arenaStart.getY() + 50.0, arenaStart.getZ() + 50.0));
        player.sendMessage(ChatColor.GREEN + "Teleporting to " + this.schematic.getName());
    }
}

