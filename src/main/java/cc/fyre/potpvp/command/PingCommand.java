/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.util.PlayerUtils
 */
package cc.fyre.potpvp.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.util.PlayerUtils;

public class PingCommand {
    @Command(names={"ping"}, permission="")
    public static void ping(Player sender, @Param(name="target", defaultValue="self") Player target) {
        sender.sendMessage(target.getDisplayName() + ChatColor.YELLOW + "'s Ping: " + ChatColor.GREEN + PlayerUtils.getPing((Player)target) + "ms");
    }
}

