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
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class PartyLeaveCommand {
    @Command(names={"party leave", "p leave", "t leave", "team leave", "leave", "f leave"}, permission="")
    public static void partyLeave(Player sender) {
        Party party = PotPvP.getInstance().getPartyHandler().getParty(sender);
        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else {
            party.leave(sender);
        }
    }
}

