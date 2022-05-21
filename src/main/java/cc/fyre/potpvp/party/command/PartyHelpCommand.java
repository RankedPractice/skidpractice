/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvPLang;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class PartyHelpCommand {
    private static final List<String> HELP_MESSAGE;

    public PartyHelpCommand() {
    }

    @Command(
            names = {"team", "p", "t", "team", "f", "team help", "p help", "t help", "team help", "f help"},
            permission = ""
    )
    public static void team(Player sender) {
        HELP_MESSAGE.forEach(sender::sendMessage);
    }

    static {
        HELP_MESSAGE = ImmutableList.of(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE, "§d§lParty Help §7- §fInformation on how to use team commands", ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE, "§5Team Commands:", "§d/team invite §7- Invite a player to join your team", "§d/team leave §7- Leave your current team", "§d/team accept [player] §7- Accept team invitation", "§d/team info [player] §7- View the roster of the team", "", "§9Leader Commands:", "§d/team kick <player> §7- Kick a player from your team", "§d/team leader <player> §7- Transfer team leadership", new String[]{"§d/team disband §7 - Disbands team", "§d/team lock §7 - Lock team from others joining", "§d/team open §7 - Open team to others joining", "§d/team password <password> §7 - Sets team password", "", "§9Other Help:", "§dTo use §dteam chat§d, prefix your message with the §7'§d@§7' §dsign.", ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE});
    }
}