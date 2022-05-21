/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.match.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class ToggleMatchCommands {
    @Command(names={"toggleMatches unranked"}, permission="op")
    public static void toggleMatchesUnranked(Player sender) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        boolean newState = !matchHandler.isUnrankedMatchesDisabled();
        matchHandler.setUnrankedMatchesDisabled(newState);
        sender.sendMessage(ChatColor.YELLOW + "Unranked matches are now " + ChatColor.UNDERLINE + (newState ? "disabled" : "enabled") + ChatColor.YELLOW + ".");
    }

    @Command(names={"toggleMatches ranked"}, permission="op")
    public static void toggleMatchesRanked(Player sender) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        boolean newState = !matchHandler.isRankedMatchesDisabled();
        matchHandler.setRankedMatchesDisabled(newState);
        sender.sendMessage(ChatColor.YELLOW + "Ranked matches are now " + ChatColor.UNDERLINE + (newState ? "disabled" : "enabled") + ChatColor.YELLOW + ".");
    }

    @Command(names={"toggleMatches premium"}, permission="op")
    public static void toggleMatchesPremium(Player sender) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        boolean newState = !matchHandler.isPremiumMatchesDisabled();
        matchHandler.setPremiumMatchesDisabled(newState);
        sender.sendMessage(ChatColor.YELLOW + "Premium matches are now " + ChatColor.UNDERLINE + (newState ? "disabled" : "enabled") + ChatColor.YELLOW + ".");
    }
}

