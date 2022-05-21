/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class SetSpawnCommand {
    @Command(names={"setspawn"}, permission="op")
    public static void setSpawn(Player sender) {
        Location loc = sender.getLocation();
        sender.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
        sender.sendMessage(ChatColor.YELLOW + "Spawn point updated!");
    }
}

