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
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class ArenaScaleCommand {
    @Command(names={"arena scale"}, permission="op")
    public static void arenaScale(Player sender, @Param(name="schematic") String schematicName, @Param(name="count") int count) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);
        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Starting...");
        arenaHandler.getGrid().scaleCopies(schematic, count, () -> sender.sendMessage(ChatColor.GREEN + "Scaled " + schematic.getName() + " to " + count + " copies."));
    }

    @Command(names={"arena rescaleall"}, permission="op")
    public static void arenaRescaleAll(Player sender) {
        PotPvP.getInstance().getArenaHandler().getSchematics().forEach(schematic -> {
            ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
            int totalCopies = 0;
            boolean inUseCopies = false;
            for (Arena arena : arenaHandler.getArenas((ArenaSchematic)schematic)) {
                ++totalCopies;
            }
            ArenaScaleCommand.arenaScale(sender, schematic.getName(), 0);
            ArenaScaleCommand.arenaScale(sender, schematic.getName(), totalCopies);
        });
    }
}

