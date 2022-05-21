/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.validation.PotPvPValidation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.util.Callback;

public final class PartyFfaCommand {
    @Command(names={"party ffa", "p ffa", "t ffa", "team ffa", "f ffa"}, permission="")
    public static void partyFfa(Player sender) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);
        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            if (!PotPvPValidation.canStartFfa(party, sender, null)) {
                return;
            }
            new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                sender.closeInventory();
                if (!PotPvPValidation.canStartFfa(party, sender, kitType)) {
                    return;
                }
                ArrayList<MatchTeam> teams = new ArrayList<MatchTeam>();
                for (UUID member : party.getMembers()) {
                    teams.add(new MatchTeam(0, member));
                }
                matchHandler.startMatch((List<MatchTeam>)teams, (KitType)kitType, null, QueueType.UNRANKED, false);
            }), "Start a Party FFA...").openMenu(sender);
        }
    }

    @Command(names={"party devffa", "p devffa", "t devffa", "team devffa", "f devffa"}, permission="")
    public static void partyDevFfa(Player sender, @Param(name="team size", defaultValue="1") int teamSize) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);
        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            if (!PotPvPValidation.canStartFfa(party, sender, null)) {
                return;
            }
            new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                sender.closeInventory();
                if (!PotPvPValidation.canStartFfa(party, sender, kitType)) {
                    return;
                }
                ArrayList<UUID> availableMembers = new ArrayList<UUID>(party.getMembers());
                Collections.shuffle(availableMembers);
                ArrayList<MatchTeam> teams = new ArrayList<MatchTeam>();
                while (availableMembers.size() >= teamSize) {
                    ArrayList<UUID> teamMembers = new ArrayList<UUID>();
                    for (int i = 0; i < teamSize; ++i) {
                        teamMembers.add((UUID)availableMembers.remove(0));
                    }
                    teams.add(new MatchTeam(0, teamMembers));
                }
                Match match = matchHandler.startMatch((List<MatchTeam>)teams, (KitType)kitType, null, QueueType.UNRANKED, false);
                if (match != null) {
                    for (UUID leftOut : availableMembers) {
                        match.addSpectator(Bukkit.getPlayer((UUID)leftOut), null);
                    }
                }
            }), "Start Dev Party FFA...").openMenu(sender);
        }
    }
}

