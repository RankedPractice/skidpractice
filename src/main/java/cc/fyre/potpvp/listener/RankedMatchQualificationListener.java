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

import cc.fyre.potpvp.util.MongoUtils;
import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.qLib;

public final class RankedMatchQualificationListener
implements Listener {
    public static final String KEY_PREFIX = "potpvp:rankedMatchQualification:";
    public static final int MIN_MATCH_WINS = 10;
    private static final Map<UUID, Integer> rankedMatchQualificationWins = new ConcurrentHashMap<UUID, Integer>();

    public static int getWinsNeededToQualify(UUID playerUuid) {
        return Math.max(0, 10 - rankedMatchQualificationWins.getOrDefault(playerUuid, 0));
    }

    public static boolean isQualified(UUID playerUuid) {
        return rankedMatchQualificationWins.getOrDefault(playerUuid, 0) >= 10;
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        qLib.getInstance().runRedisCommand(redis -> {
            String existing = redis.get(KEY_PREFIX + event.getUniqueId());
            if (existing != null && !existing.isEmpty()) {
                rankedMatchQualificationWins.put(event.getUniqueId(), Integer.parseInt(existing));
            }
            return null;
        });
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        rankedMatchQualificationWins.remove(event.getPlayer().getUniqueId());
    }

    @Command(names={"rmqRead"}, permission="op")
    public static void rmqRead(Player sender, @Param(name="target", defaultValue="self") Player target) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Wins: " + ChatColor.GRAY.toString() + rankedMatchQualificationWins.getOrDefault(target.getUniqueId(), 0));
        sender.sendMessage(ChatColor.DARK_PURPLE + "Qualified: " + ChatColor.GRAY.toString() + RankedMatchQualificationListener.isQualified(target.getUniqueId()));
    }

    @Command(names={"rmqSet"}, permission="op")
    public static void rmqSet(Player sender, @Param(name="target") Player target, @Param(name="count") int count) {
        rankedMatchQualificationWins.put(target.getUniqueId(), count);
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            jedis.set(KEY_PREFIX + target.getUniqueId(), String.valueOf(count));
        }
        sender.sendMessage(ChatColor.GOLD + "Updated!");
    }

    @Command(names={"rmqImport"}, permission="op", async=true)
    public static void rmqImport(Player sender) {
        MongoCollection<Document> matchCollection = MongoUtils.getCollection("matches");
        sender.sendMessage(ChatColor.GOLD + "Starting...");
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            matchCollection.find(new Document("ranked", false).append("winner", new Document("$gte", 0))).forEach(match -> {
                List teams = (List)((Object)match.get((Object)"teams", List.class));
                if (teams.size() != 2) {
                    return;
                }
                for (Document team : teams) {
                    int size = ((List)((Object)team.get((Object)"allMembers", List.class))).size();
                    if (size == 1) continue;
                    return;
                }
                Document winnerTeam = (Document)teams.get(match.getInteger("winner"));
                List winnerPlayers = (List)((Object)winnerTeam.get((Object)"allMembers", List.class));
                for (String winnerPlayer : winnerPlayers) {
                    jedis.incr(KEY_PREFIX + winnerPlayer);
                }
                sender.sendMessage(ChatColor.GOLD + "Imported match " + ChatColor.WHITE + match.getObjectId("_id") + ChatColor.GOLD + ".");
            });
        }
        sender.sendMessage(ChatColor.GREEN + "Done!");
    }
}

