/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.follow.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.follow.command.UnfollowCommand;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.validation.PotPvPValidation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class FollowCommand {
    @Command(names={"follow"}, permission="")
    public static void follow(Player sender, @Param(name="target") Player target) {
        if (!PotPvPValidation.canFollowSomeone(sender)) {
            return;
        }
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "No, you can't follow yourself.");
            return;
        }
        if (!settingHandler.getSetting(target, Setting.ALLOW_SPECTATORS)) {
            if (sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Bypassing " + target.getName() + "'s no spectators preference...");
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " doesn't allow spectators at the moment.");
                return;
            }
        }
        followHandler.getFollowing(sender).ifPresent(fo -> UnfollowCommand.unfollow(sender));
        if (matchHandler.isSpectatingMatch(sender)) {
            matchHandler.getMatchSpectating(sender).removeSpectator(sender);
        }
        followHandler.startFollowing(sender, target);
    }
}

