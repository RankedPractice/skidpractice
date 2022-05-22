/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.validation;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.tournament.TournamentHandler;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class PotPvPValidation {
    private static final String CANNOT_DUEL_SELF = ChatColor.RED + "You can't duel yourself!";
    private static final String CANNOT_DUEL_OWN_PARTY = ChatColor.RED + "You can't duel your own party!";
    private static final String CANNOT_DO_THIS_WHILE_IN_PARTY = ChatColor.RED + "You can't do this while in a party!";
    private static final String CANNOT_DO_THIS_WHILE_QUEUED = ChatColor.RED + "You can't do this while queued!";
    private static final String CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY = ChatColor.RED + "You can't do this while you're not in the lobby!";
    private static final String CANNOT_DO_THIS_WHILE_IN_MATCH = ChatColor.RED + "You can't do this while participating in or spectating a match!";
    private static final String CANNOT_DO_THIS_WHILE_FOLLOWING = ChatColor.RED + "You cannot do this while following someone! Type /unfollow to exit.";
    private static final String CANNOT_DO_THIS_IN_SILENT_MODE = ChatColor.RED + "You cannot do this while in silent mode!";
    private static final String CANNOT_DO_THIS_WHILST_IN_TOURNAMENT = ChatColor.RED + "You cannot do this whilst in the tournament!";
    private static final String TARGET_PARTY_NOT_IN_LOBBY = ChatColor.RED + "That party is not in the lobby!";
    private static final String TARGET_PLAYER_NOT_IN_LOBBY = ChatColor.RED + "That player is not in the lobby!";
    private static final String TARGET_PLAYER_FOLLOWING_SOMEONE = ChatColor.RED + "That player is currently following someone!";
    private static final String TARGET_PLAYER_HAS_DUELS_DISABLED = ChatColor.RED + "The player has duels disabled!";
    private static final String TARGET_IN_PARTY = ChatColor.RED + "That player is in a party!";
    private static final String TARGET_PARTY_HAS_DUELS_DISABLED = ChatColor.RED + "The party has duels disabled!";
    private static final String TARGET_PARTY_REACHED_MAXIMUM_SIZE = ChatColor.RED + "The party is full.";
    private static final String TARGET_PARTY_IN_TOURNAMENT = ChatColor.RED + "That party is in a tournament!";

    public static boolean canSendDuel(Player sender, Player target) {
        if (PotPvPValidation.isInSilentMode(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_IN_SILENT_MODE);
            return false;
        }
        if (PotPvPValidation.isInSilentMode(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_IN_SILENT_MODE);
            return false;
        }
        if (sender == target) {
            sender.sendMessage(CANNOT_DUEL_SELF);
            return false;
        }
        if (!PotPvPValidation.isInLobby(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(target)) {
            sender.sendMessage(TARGET_PLAYER_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_WHILE_FOLLOWING);
            return false;
        }
        if (!PotPvPValidation.getSetting(target, Setting.RECEIVE_DUELS)) {
            sender.sendMessage(TARGET_PLAYER_HAS_DUELS_DISABLED);
            return false;
        }
        return true;
    }

    public static boolean canAcceptDuel(Player sender, Player duelSentBy) {
        if (PotPvPValidation.isInSilentMode(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_IN_SILENT_MODE);
            return false;
        }
        if (!PotPvPValidation.isInLobby(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(duelSentBy)) {
            sender.sendMessage(TARGET_PLAYER_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_WHILE_FOLLOWING);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(duelSentBy)) {
            sender.sendMessage(TARGET_PLAYER_FOLLOWING_SOMEONE);
            return false;
        }
        if (PotPvPValidation.isInParty(sender)) {
            sender.sendMessage(CANNOT_DO_THIS_WHILE_IN_PARTY);
            return false;
        }
        if (PotPvPValidation.isInParty(duelSentBy)) {
            sender.sendMessage(TARGET_IN_PARTY);
            return false;
        }
        return true;
    }

    public static boolean canSendDuel(Party sender, Party target, Player initiator) {
        if (sender == target) {
            initiator.sendMessage(CANNOT_DUEL_OWN_PARTY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(initiator)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(Bukkit.getPlayer((UUID)target.getLeader()))) {
            initiator.sendMessage(TARGET_PARTY_NOT_IN_LOBBY);
            return false;
        }
        if (!PotPvPValidation.getSetting(Bukkit.getPlayer((UUID)target.getLeader()), Setting.RECEIVE_DUELS)) {
            initiator.sendMessage(TARGET_PARTY_HAS_DUELS_DISABLED);
            return false;
        }
        return true;
    }

    public static boolean canAcceptDuel(Party target, Party sender, Player initiator) {
        if (!PotPvPValidation.isInLobby(initiator)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(Bukkit.getPlayer((UUID)target.getLeader()))) {
            initiator.sendMessage(TARGET_PLAYER_NOT_IN_LOBBY);
            return false;
        }
        return true;
    }

    public static boolean canJoinParty(Player player, Party party) {
        if (PotPvPValidation.isInParty(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_PARTY);
            return false;
        }
        if (!PotPvPValidation.isInLobby(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_FOLLOWING);
            return false;
        }
        if (party.getMembers().size() >= 30 && !Bukkit.getPlayer((UUID)party.getLeader()).isOp()) {
            player.sendMessage(TARGET_PARTY_REACHED_MAXIMUM_SIZE);
            return false;
        }
        return true;
    }

    public static boolean canUseSpectateItem(Player player) {
        if (!PotPvPValidation.isInLobby(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        return PotPvPValidation.canUseSpectateItemIgnoreMatchSpectating(player);
    }

    public static boolean canUseSpectateItemIgnoreMatchSpectating(Player player) {
        if (PotPvPValidation.isInParty(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_PARTY);
            return false;
        }
        if (PotPvPValidation.isInQueue(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (PotPvPValidation.isInMatch(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_MATCH);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_FOLLOWING);
            return false;
        }
        return true;
    }

    public static boolean canFollowSomeone(Player player) {
        if (PotPvPValidation.isInParty(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_PARTY);
            return false;
        }
        if (PotPvPValidation.isInQueue(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (PotPvPValidation.isInMatch(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_MATCH);
            return false;
        }
        if (!PotPvPValidation.isInLobby(player)) {
            player.sendMessage(ChatColor.RED + "You can't do that here!");
            return false;
        }
        return PotPvPValidation.isInLobby(player);
    }

    public static boolean canJoinQueue(Player player) {
        if (PotPvPValidation.isInSilentMode(player)) {
            player.sendMessage(CANNOT_DO_THIS_IN_SILENT_MODE);
            return false;
        }
        if (PotPvPValidation.isInParty(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_IN_PARTY);
            return false;
        }
        if (PotPvPValidation.isInQueue(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (!PotPvPValidation.isInLobby(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isFollowingSomeone(player)) {
            player.sendMessage(CANNOT_DO_THIS_WHILE_FOLLOWING);
            return false;
        }
        return true;
    }

    public static boolean canJoinQueue(Party party) {
        if (PotPvPValidation.isInQueue(party)) {
            party.message(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (PotPvPValidation.isInTournament(party)) {
            party.message(CANNOT_DO_THIS_WHILST_IN_TOURNAMENT);
            return false;
        }
        return true;
    }

    public static boolean canStartTeamSplit(Party party, Player initiator) {
        if (PotPvPValidation.isInQueue(party)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (!PotPvPValidation.isInLobby(initiator)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isInTournament(party)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILST_IN_TOURNAMENT);
            return false;
        }
        return true;
    }

    public static boolean canStartFfa(Party party, Player initiator, KitType type2) {
        if (PotPvPValidation.isInQueue(party)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_QUEUED);
            return false;
        }
        if (!PotPvPValidation.isInLobby(initiator)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILE_NOT_IN_LOBBY);
            return false;
        }
        if (PotPvPValidation.isInTournament(party)) {
            initiator.sendMessage(CANNOT_DO_THIS_WHILST_IN_TOURNAMENT);
            return false;
        }
        if (type2 != null && type2.getId().equals("MLGRush")) {
            initiator.sendMessage(ChatColor.RED + "You cannot play MLGRush in a party ffa.");
            return false;
        }
        return true;
    }

    private static boolean getSetting(Player player, Setting setting) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        return settingHandler.getSetting(player, setting);
    }

    private static boolean isInParty(Player player) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        return partyHandler.hasParty(player);
    }

    private static boolean isInQueue(Player player) {
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        return queueHandler.isQueued(player.getUniqueId());
    }

    private static boolean isInQueue(Party party) {
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        return queueHandler.isQueued(party);
    }

    private static boolean isInMatch(Player player) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        return matchHandler.isPlayingMatch(player);
    }

    private static boolean isInLobby(Player player) {
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        return lobbyHandler.isInLobby(player);
    }

    private static boolean isFollowingSomeone(Player player) {
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        return followHandler.getFollowing(player).isPresent();
    }

    private static boolean isInTournament(Party party) {
        TournamentHandler tournamentHandler = PotPvP.getInstance().getTournamentHandler();
        return tournamentHandler.isInTournament(party);
    }

    private static boolean isInSilentMode(Player player) {
        return player.hasMetadata("ModMode");
    }

    private PotPvPValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

