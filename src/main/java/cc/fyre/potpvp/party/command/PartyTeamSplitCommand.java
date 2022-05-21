/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.PartyUtils;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class PartyTeamSplitCommand {
    @Command(names={"party teamsplit", "p teamsplit", "t teamsplit", "team teamsplit", "f teamsplit"}, permission="")
    public static void partyTeamSplit(Player sender) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);
        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            PartyUtils.startTeamSplit(party, sender);
        }
    }
}

