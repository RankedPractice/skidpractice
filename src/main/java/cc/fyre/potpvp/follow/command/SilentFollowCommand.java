/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.follow.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.command.FollowCommand;
import cc.fyre.potpvp.match.command.LeaveCommand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class SilentFollowCommand {
    @Command(names={"silentfollow"}, permission="potpvp.silent")
    public static void silentfollow(Player sender, @Param(name="target") Player target) {
        sender.setMetadata("ModMode", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
        sender.setMetadata("invisible", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
        if (PotPvP.getInstance().getPartyHandler().hasParty(sender)) {
            LeaveCommand.leave(sender);
        }
        FollowCommand.follow(sender, target);
    }
}

