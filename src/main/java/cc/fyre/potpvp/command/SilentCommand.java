/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.util.VisibilityUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.command.Command;

public final class SilentCommand {
    @Command(names={"silent"}, permission="potpvp.silent")
    public static void silent(Player sender) {
        if (sender.hasMetadata("ModMode")) {
            sender.removeMetadata("ModMode", (Plugin)PotPvP.getInstance());
            sender.removeMetadata("invisible", (Plugin)PotPvP.getInstance());
            sender.sendMessage(ChatColor.RED + "Silent mode disabled.");
        } else {
            sender.setMetadata("ModMode", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
            sender.setMetadata("invisible", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
            sender.sendMessage(ChatColor.GREEN + "Silent mode enabled.");
        }
        VisibilityUtils.updateVisibility(sender);
    }
}

