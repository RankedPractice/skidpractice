/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.util.VisibilityUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class UpdateVisibilityCommands {
    @Command(names={"updatevisibility", "updatevis", "upvis", "uv"}, permission="")
    public static void updateVisibility(Player sender) {
        VisibilityUtils.updateVisibility(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your visibility.");
    }

    @Command(names={"updatevisibilityFlicker", "updatevisFlicker", "upvisFlicker", "uvf"}, permission="")
    public static void updateVisibilityFlicker(Player sender) {
        VisibilityUtils.updateVisibilityFlicker(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your visibility (flicker mode).");
    }
}

