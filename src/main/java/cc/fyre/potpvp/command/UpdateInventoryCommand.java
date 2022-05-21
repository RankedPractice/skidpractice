/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.util.InventoryUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class UpdateInventoryCommand {
    @Command(names={"updateinventory", "updateinv", "upinv", "ui"}, permission="")
    public static void updateInventory(Player sender) {
        InventoryUtils.resetInventoryDelayed(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your inventory.");
    }
}

