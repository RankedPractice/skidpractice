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
    private static final List<String> HELP_MESSAGE = ImmutableList.of((Object)(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE), (Object)"\u00a7d\u00a7lParty Help \u00a77- \u00a7fInformation on how to use party commands", (Object)(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE), (Object)"\u00a79Party Commands:", (Object)"\u00a7e/party invite \u00a77- Invite a player to join your party", (Object)"\u00a7e/party leave \u00a77- Leave your current party", (Object)"\u00a7e/party accept [player] \u00a77- Accept party invitation", (Object)"\u00a7e/party info [player] \u00a77- View the roster of the party", (Object)"", (Object)"\u00a79Leader Commands:", (Object)"\u00a7e/party kick <player> \u00a77- Kick a player from your party", (Object)"\u00a7e/party leader <player> \u00a77- Transfer party leadership", (Object[])new String[]{"\u00a7e/party disband \u00a77 - Disbands party", "\u00a7e/party lock \u00a77 - Lock party from others joining", "\u00a7e/party open \u00a77 - Open party to others joining", "\u00a7e/party password <password> \u00a77 - Sets party password", "", "\u00a79Other Help:", "\u00a7eTo use \u00a7dparty chat\u00a7e, prefix your message with the \u00a77'\u00a7d@\u00a77' \u00a7esign.", ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE});

    @Command(names={"party", "p", "t", "team", "f", "party help", "p help", "t help", "team help", "f help"}, permission="")
    public static void party(Player sender) {
        HELP_MESSAGE.forEach(arg_0 -> ((Player)sender).sendMessage(arg_0));
    }
}

