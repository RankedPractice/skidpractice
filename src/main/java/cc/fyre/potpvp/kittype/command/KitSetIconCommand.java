/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.kittype.command;

import cc.fyre.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class KitSetIconCommand {
    @Command(names={"kittype seticon"}, permission="op", description="Sets a kit-type's icon")
    public static void execute(Player player, @Param(name="kittype") KitType kitType) {
        if (player.getItemInHand() == null) {
            player.sendMessage(ChatColor.RED + "Please hold an item in your hand.");
            return;
        }
        kitType.setIcon(player.getItemInHand().getData());
        kitType.saveAsync();
        player.sendMessage(ChatColor.GREEN + "You've updated this kit-type's icon.");
    }
}

