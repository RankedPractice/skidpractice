/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyAccessRestriction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class PartyPasswordCommand {
    @Command(names={"party password", "p password", "t password", "team password", "party pass", "p pass", "t pass", "team pass", "f password", "f pass"}, permission="")
    public static void partyPassword(Player sender, @Param(name="password") String password) {
        Party party = PotPvP.getInstance().getPartyHandler().getParty(sender);
        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            party.setAccessRestriction(PartyAccessRestriction.PASSWORD);
            party.setPassword(password);
            sender.sendMessage(ChatColor.GOLD + "Your party's password is now " + ChatColor.WHITE + password + ChatColor.GOLD + ".");
        }
    }
}

