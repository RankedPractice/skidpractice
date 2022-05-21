/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.party;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.menu.oddmanout.OddManOutMenu;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.validation.PotPvPValidation;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.util.Callback;

public final class PartyUtils {
    public static void startTeamSplit(Party party, Player initiator) {
        if (!PotPvPValidation.canStartTeamSplit(party, initiator)) {
            return;
        }
        new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
            initiator.closeInventory();
            if (party.getMembers().size() % 2 == 0) {
                PartyUtils.startTeamSplit(party, initiator, kitType, false);
            } else {
                new OddManOutMenu((Callback<Boolean>)((Callback)oddManOut -> {
                    initiator.closeInventory();
                    PartyUtils.startTeamSplit(party, initiator, kitType, oddManOut);
                })).openMenu(initiator);
            }
        }), "Start a Team Split...").openMenu(initiator);
    }

    public static void startTeamSplit(Party party, Player initiator, KitType kitType, boolean oddManOut) {
        Match match;
        if (!PotPvPValidation.canStartTeamSplit(party, initiator)) {
            return;
        }
        ArrayList<UUID> members2 = new ArrayList<UUID>(party.getMembers());
        Collections.shuffle(members2);
        HashSet<UUID> team1 = new HashSet<UUID>();
        HashSet<UUID> team2 = new HashSet<UUID>();
        Player spectator = null;
        while (members2.size() >= 2) {
            team1.add((UUID)members2.remove(0));
            team2.add((UUID)members2.remove(0));
        }
        if (!members2.isEmpty()) {
            if (oddManOut) {
                spectator = Bukkit.getPlayer((UUID)((UUID)members2.remove(0)));
                party.message(ChatColor.YELLOW + spectator.getName() + " was selected as the odd-man out.");
            } else {
                team1.add((UUID)members2.remove(0));
            }
        }
        if ((match = PotPvP.getInstance().getMatchHandler().startMatch((List<MatchTeam>)ImmutableList.of((Object)new MatchTeam(0, team1), (Object)new MatchTeam(1, team2)), kitType, null, QueueType.UNRANKED, false)) == null) {
            initiator.sendMessage(ChatColor.RED + "Failed to start team split.");
            return;
        }
        party.message(ChatColor.YELLOW + "Starting Party Split match");
        if (spectator != null) {
            match.addSpectator(spectator, null);
        }
    }

    private PartyUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

