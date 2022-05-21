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

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class KitDeleteCommand {
    @Command(names={"kittype delete"}, permission="op", description="Deletes an existing kit-type")
    public static void execute(Player player, @Param(name="kittype") KitType kitType) {
        kitType.deleteAsync();
        KitType.getAllTypes().remove(kitType);
        PotPvP.getInstance().getQueueHandler().removeQueues(kitType);
        player.sendMessage(ChatColor.GREEN + "You've deleted the kit-type by the ID \"" + kitType.id + "\".");
    }
}

