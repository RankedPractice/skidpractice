/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.LinkedList
 *  rip.bridge.qlib.util.TimeUtils
 */
package cc.fyre.potpvp.scoreboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.queue.MatchQueue;
import cc.fyre.potpvp.queue.MatchQueueEntry;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.tournament.Tournament;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.util.LinkedList;
import rip.bridge.qlib.util.TimeUtils;

public final class LobbyScoreGetter
implements BiConsumer<Player, LinkedList<String>> {
    public static int LAST_ONLINE_COUNT = 0;
    public static int LAST_IN_FIGHTS_COUNT = 0;
    public static int LAST_IN_QUEUES_COUNT = 0;
    public static int PREMIUM_MATCHES_COUNT = 0;
    private long lastUpdated = System.currentTimeMillis();

    @Override
    public void accept(Player player, LinkedList<String> scores) {
        Tournament tournament;
        MatchQueueEntry entry;
        MatchQueue queue;
        Optional<UUID> followingOpt = PotPvP.getInstance().getFollowHandler().getFollowing(player);
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
        scores.add((Object)("&fOnline: &b" + LAST_ONLINE_COUNT));
        scores.add((Object)("&fIn Fights: &b" + LAST_IN_FIGHTS_COUNT));
        scores.add((Object)("&fPremium Matches: &b" + PREMIUM_MATCHES_COUNT));
        Party playerParty = partyHandler.getParty(player);
        if (playerParty != null) {
            int size = playerParty.getMembers().size();
            scores.add((Object)"");
            scores.add((Object)("&9Your Party: &b" + size));
        }
        if (1000L <= System.currentTimeMillis() - this.lastUpdated) {
            this.lastUpdated = System.currentTimeMillis();
            LAST_ONLINE_COUNT = Bukkit.getOnlinePlayers().size();
            LAST_IN_FIGHTS_COUNT = matchHandler.countPlayersPlayingInProgressMatches();
            LAST_IN_QUEUES_COUNT = queueHandler.getQueuedCount();
            PREMIUM_MATCHES_COUNT = PotPvP.getInstance().getPremiumMatchesHandler().getPremiumMatches(player.getUniqueId());
        }
        if (followingOpt.isPresent()) {
            MatchQueueEntry targetEntry;
            Player following = Bukkit.getPlayer((UUID)followingOpt.get());
            scores.add((Object)("Following: &6" + following.getName()));
            if (player.hasPermission("stark.staff") && (targetEntry = this.getQueueEntry(following)) != null) {
                queue = targetEntry.getQueue();
                scores.add((Object)"Target queue:");
                scores.add((Object)("&a" + queue.getQueueType().getName() + " " + queue.getKitType().getDisplayName()));
            }
        }
        if ((entry = this.getQueueEntry(player)) != null) {
            int elo;
            String waitTimeFormatted = TimeUtils.formatIntoMMSS((int)entry.getWaitSeconds());
            queue = entry.getQueue();
            scores.add((Object)"&b&7&m--------------------");
            scores.add((Object)(queue.getKitType().getDisplayColor() + queue.getQueueType().getName() + " " + queue.getKitType().getDisplayName()));
            scores.add((Object)("Time: " + ChatColor.BLUE + waitTimeFormatted));
            if (queue.getQueueType().isRanked()) {
                elo = eloHandler.getElo(entry.getMembers(), queue.getKitType());
                int window = entry.getWaitSeconds() * 5;
                scores.add((Object)("Range: &b" + Math.max(0, elo - window) + " &7- &b" + (elo + window)));
            } else if (queue.getQueueType().isPremium()) {
                elo = eloHandler.getPremiumElo(entry.getMembers(), queue.getKitType());
                int window = entry.getWaitSeconds() * 5;
                scores.add((Object)("Range: &b" + Math.max(0, elo - window) + " &7- &b" + (elo + window)));
            }
        }
        if (player.hasMetadata("ModMode")) {
            scores.add((Object)(ChatColor.GOLD + "Silent Mode Enabled"));
        }
        if ((tournament = PotPvP.getInstance().getTournamentHandler().getTournament()) != null) {
            int multiplier;
            scores.add((Object)"&5&7&m--------------------");
            scores.add((Object)"&b&lTournament");
            if (tournament.getStage() == Tournament.TournamentStage.WAITING_FOR_TEAMS) {
                int teamSize = tournament.getRequiredPartySize();
                scores.add((Object)("Kit: &b" + tournament.getType().getDisplayName()));
                scores.add((Object)("Team Size: &b" + teamSize + "v" + teamSize));
                multiplier = teamSize < 3 ? teamSize : 1;
                scores.add((Object)((teamSize < 3 ? "Players" : "Teams") + ": &b" + tournament.getActiveParties().size() * multiplier + "/" + tournament.getRequiredPartiesToStart() * multiplier));
            } else if (tournament.getStage() == Tournament.TournamentStage.COUNTDOWN) {
                if (tournament.getCurrentRound() == 0) {
                    scores.add((Object)("Begins in &b" + tournament.getBeginNextRoundIn() + "&f second" + (tournament.getBeginNextRoundIn() == 1 ? "" : "s")));
                } else {
                    scores.add((Object)("Next Round: &b" + (tournament.getCurrentRound() + 1)));
                    scores.add((Object)("Begins in &b" + tournament.getBeginNextRoundIn() + "&f second" + (tournament.getBeginNextRoundIn() == 1 ? "" : "s")));
                }
            } else if (tournament.getStage() == Tournament.TournamentStage.IN_PROGRESS) {
                scores.add((Object)("Round: &6" + tournament.getCurrentRound()));
                int teamSize = tournament.getRequiredPartySize();
                multiplier = teamSize < 3 ? teamSize : 1;
                scores.add((Object)((teamSize < 3 ? "Players" : "Teams") + ": &6" + tournament.getActiveParties().size() * multiplier + "/" + tournament.getRequiredPartiesToStart() * multiplier));
                scores.add((Object)("Duration: &b" + TimeUtils.formatIntoMMSS((int)((int)(System.currentTimeMillis() - tournament.getRoundStartedAt()) / 1000))));
            }
        }
    }

    private MatchQueueEntry getQueueEntry(Player player) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        Party playerParty = partyHandler.getParty(player);
        if (playerParty != null) {
            return queueHandler.getQueueEntry(playerParty);
        }
        return queueHandler.getQueueEntry(player.getUniqueId());
    }
}

