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
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class ArenaCreateSchematicCommand {
    @Command(names={"arena createSchematic"}, permission="op")
    public static void arenaCreateSchematic(Player sender, @Param(name="schematic") String schematicName) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        if (arenaHandler.getSchematic(schematicName) != null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " already exists");
            return;
        }
        ArenaSchematic schematic = new ArenaSchematic(schematicName);
        File schemFile = schematic.getSchematicFile();
        if (!schemFile.exists()) {
            sender.sendMessage(ChatColor.RED + "No file for " + schematicName + " found. (" + schemFile.getPath() + ")");
            return;
        }
        arenaHandler.registerSchematic(schematic);
        try {
            schematic.pasteModelArena();
            arenaHandler.saveSchematics();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        sender.sendMessage(ChatColor.GREEN + "Schematic created.");
    }
}

