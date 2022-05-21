/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.arena.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaGrid;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class ArenaRepasteSchematicCommand {
    @Command(names={"arena repasteSchematic"}, permission="op")
    public static void arenaRepasteSchematic(Player sender, @Param(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);
        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }
        int currentCopies = arenaHandler.countArenas(schematic);
        if (currentCopies == 0) {
            sender.sendMessage(ChatColor.RED + "No copies of " + schematic.getName() + " exist.");
            return;
        }
        ArenaGrid arenaGrid = arenaHandler.getGrid();
        sender.sendMessage(ChatColor.GREEN + "Starting...");
        arenaGrid.scaleCopies(schematic, 0, () -> {
            sender.sendMessage(ChatColor.GREEN + "Removed old maps, creating new copies...");
            arenaGrid.scaleCopies(schematic, currentCopies, () -> sender.sendMessage(ChatColor.GREEN + "Repasted " + currentCopies + " arenas using the newest " + schematic.getName() + " schematic."));
        });
    }
}

