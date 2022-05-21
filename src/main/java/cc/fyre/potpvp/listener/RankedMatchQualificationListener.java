/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  redis.clients.jedis.Jedis
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchEndEvent;
import cc.fyre.potpvp.util.MongoUtils;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import redis.clients.jedis.Jedis;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.qLib;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RankedMatchQualificationListener implements Listener {

    public static final String KEY_PREFIX = "potpvp:rankedMatchQualification:";
    public static final int MIN_MATCH_WINS = 10;
    private static final Map<UUID, Integer> rankedMatchQualificationWins = new ConcurrentHashMap<>();

    public static int getWinsNeededToQualify(UUID playerUuid) {
        return Math.max(0, MIN_MATCH_WINS - rankedMatchQualificationWins.getOrDefault(playerUuid, 0));
    }

    public static boolean isQualified(UUID playerUuid) {
        return rankedMatchQualificationWins.getOrDefault(playerUuid, 0) >= MIN_MATCH_WINS;
    }

    @EventHandler(priority = EventPriority.LOWEST) // LOWEST runs first
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            String existing = jedis.get(KEY_PREFIX + event.getUniqueId());

            if (existing != null && !existing.isEmpty()) {
                rankedMatchQualificationWins.put(event.getUniqueId(), Integer.parseInt(existing));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR) // MONITOR runs last
    public void onPlayerQuit(PlayerQuitEvent event) {
        rankedMatchQualificationWins.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        Match match = event.getMatch();

        // make sure match was unranked + had a winner
        if (match.getWinner() == null || match.getQueueType().isRanked()) {
            return;
        }

        List<MatchTeam> teams = match.getTeams();

        // make sure it's a 1v1
        if (teams.size() != 2 || teams.get(0).getAllMembers().size() != 1 || teams.get(1).getAllMembers().size() != 1) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(PotPvP.getInstance(), () -> {
            try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
                UUID winner = match.getWinner().getFirstAliveMember();
                rankedMatchQualificationWins.put(winner, jedis.incr(KEY_PREFIX + winner).intValue());
            }
        });
    }

    @Command(names = {"rmqRead"}, permission = "op")
    public static void rmqRead(Player sender, @Param(name="target",defaultValue="self") Player target) {
        sender.sendMessage(ChatColor.AQUA + "Wins: " + ChatColor.GRAY.toString() + rankedMatchQualificationWins.getOrDefault(target.getUniqueId(), 0));
        sender.sendMessage(ChatColor.AQUA + "Qualified: " + ChatColor.GRAY.toString() + isQualified(target.getUniqueId()));
    }

    @Command(names = {"rmqSet"}, permission = "op")
    public static void rmqSet(Player sender, @Param(name="target") Player target, @Param(name="count") int count) {
        rankedMatchQualificationWins.put(target.getUniqueId(), count);

        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            jedis.set(KEY_PREFIX + target.getUniqueId(), String.valueOf(count));
        }

        sender.sendMessage(ChatColor.AQUA + "Updated!");
    }

    @Command(names = {"rmqImport"}, permission = "op", async = true)
    public static void rmqImport(Player sender) {
        MongoCollection<Document> matchCollection = MongoUtils.getCollection(MatchHandler.MONGO_COLLECTION_NAME);

        sender.sendMessage(ChatColor.AQUA + "Starting...");

        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            matchCollection.find(new Document("ranked", false).append("winner", new Document("$gte", 0))).forEach((Block<Document>) match -> {
                List<Document> teams = (List<Document>) match.get("teams", List.class);

                // make sure we have 2 teams
                if (teams.size() != 2) {
                    return;
                }

                // and make sure they both have one player each
                for (Document team : teams) {
                    int size = team.get("allMembers", List.class).size();

                    if (size != 1) {
                        return;
                    }
                }

                Document winnerTeam = teams.get(match.getInteger("winner"));
                List<String> winnerPlayers = (List<String>) winnerTeam.get("allMembers", List.class);

                for (String winnerPlayer : winnerPlayers) {
                    jedis.incr(KEY_PREFIX + winnerPlayer);
                }

                sender.sendMessage(ChatColor.AQUA + "Imported match " + ChatColor.YELLOW + match.getObjectId("_id") + ChatColor.AQUA + ".");
            });
        }

        sender.sendMessage(ChatColor.GREEN + "Done!");
    }

}

