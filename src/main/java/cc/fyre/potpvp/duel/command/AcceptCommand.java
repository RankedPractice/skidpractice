/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.duel.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.duel.DuelHandler;
import cc.fyre.potpvp.duel.DuelInvite;
import cc.fyre.potpvp.duel.PartyDuelInvite;
import cc.fyre.potpvp.duel.PlayerDuelInvite;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.DisguiseUtil;
import cc.fyre.potpvp.validation.PotPvPValidation;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class AcceptCommand {
    @Command(names={"accept"}, permission="")
    public static void accept(Player sender, @Param(name="player") Player target) {
        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You can't accept a duel from yourself!");
            return;
        }
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        Party senderParty = partyHandler.getParty(sender);
        Party targetParty = partyHandler.getParty(target);
        if (senderParty != null && targetParty != null) {
            PartyDuelInvite invite = duelHandler.findInvite(targetParty, senderParty);
            if (invite != null) {
                AcceptCommand.acceptParty(sender, senderParty, targetParty, invite);
            } else {
                String leaderName = FrozenUUIDCache.name((UUID)targetParty.getLeader());
                sender.sendMessage(ChatColor.RED + "Your party doesn't have a duel invite from " + leaderName + "'s party.");
            }
        } else if (senderParty == null && targetParty == null) {
            PlayerDuelInvite invite = duelHandler.findInvite(target, sender);
            if (invite != null) {
                AcceptCommand.acceptPlayer(sender, target, invite);
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have a duel invite from " + target.getName() + ".");
            }
        } else if (senderParty == null) {
            sender.sendMessage(ChatColor.RED + "You don't have a duel invite from " + target.getName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "Your party doesn't have a duel invite from " + target.getName() + "'s party.");
        }
    }

    private static void acceptParty(Player sender, Party senderParty, Party targetParty, DuelInvite invite) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        if (!senderParty.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
            return;
        }
        if (!PotPvPValidation.canAcceptDuel(senderParty, targetParty, sender)) {
            return;
        }
        Match match = matchHandler.startMatch(ImmutableList.of(new MatchTeam(0, senderParty.getMembers()), new MatchTeam(1, targetParty.getMembers())), invite.getKitType(), invite.getArenaSchematic(), QueueType.UNRANKED, true);
        if (match != null) {
            duelHandler.removeInvite(invite);
            senderParty.message(ChatColor.YELLOW + "Starting duel against " + ChatColor.GREEN + DisguiseUtil.getDisguisedName(targetParty.getLeader()) + "'s Party");
            targetParty.message(ChatColor.YELLOW + "Starting duel against " + ChatColor.GREEN + DisguiseUtil.getDisguisedName(targetParty.getLeader()) + "'s Party");
        } else {
            senderParty.message(PotPvPLang.ERROR_WHILE_STARTING_MATCH);
            targetParty.message(PotPvPLang.ERROR_WHILE_STARTING_MATCH);
        }
    }

    private static void acceptPlayer(Player sender, Player target, DuelInvite invite) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        if (!PotPvPValidation.canAcceptDuel(sender, target)) {
            return;
        }
        Match match = matchHandler.startMatch((List<MatchTeam>)ImmutableList.of(new MatchTeam(0, sender.getUniqueId()), new MatchTeam(1, target.getUniqueId())), invite.getKitType(), invite.getArenaSchematic(), QueueType.UNRANKED, true);
        if (match != null) {
            duelHandler.removeInvite(invite);
            sender.sendMessage(ChatColor.YELLOW + "Starting duel against " + ChatColor.GREEN + DisguiseUtil.getDisguisedName(target.getUniqueId()));
            target.sendMessage(ChatColor.YELLOW + "Starting duel against " + ChatColor.GREEN + DisguiseUtil.getDisguisedName(sender.getUniqueId()));
        } else {
            sender.sendMessage(PotPvPLang.ERROR_WHILE_STARTING_MATCH);
            target.sendMessage(PotPvPLang.ERROR_WHILE_STARTING_MATCH);
        }
    }
}

