/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.follow.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class UnfollowCommand {
    @Command(names={"unfollow"}, permission="")
    public static void unfollow(Player sender) {
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!followHandler.getFollowing(sender).isPresent()) {
            sender.sendMessage(ChatColor.RED + "You're not following anybody.");
            return;
        }
        Match spectating = matchHandler.getMatchSpectating(sender);
        if (spectating != null) {
            spectating.removeSpectator(sender);
        }
        followHandler.stopFollowing(sender);
    }
}

