/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.rematch;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.rematch.RematchData;
import cc.fyre.potpvp.rematch.listener.RematchGeneralListener;
import cc.fyre.potpvp.rematch.listener.RematchItemListener;
import cc.fyre.potpvp.rematch.listener.RematchUnloadListener;
import cc.fyre.potpvp.util.InventoryUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class RematchHandler {
    private static final int REMATCH_TIMEOUT_SECONDS = 30;
    private final Map<UUID, RematchData> rematches = new ConcurrentHashMap<UUID, RematchData>();

    public RematchHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new RematchGeneralListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new RematchItemListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new RematchUnloadListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getScheduler().runTaskTimer((Plugin)PotPvP.getInstance(), () -> {
            Iterator<RematchData> iterator2 = this.rematches.values().iterator();
            while (iterator2.hasNext()) {
                RematchData rematchData = iterator2.next();
                if (!rematchData.isExpired()) continue;
                Player sender = Bukkit.getPlayer((UUID)rematchData.getSender());
                if (sender != null) {
                    InventoryUtils.resetInventoryDelayed(sender);
                }
                iterator2.remove();
            }
        }, 20L, 20L);
    }

    public void registerRematches(Match match) {
        if (!match.isAllowRematches()) {
            return;
        }
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() == 2) {
            MatchTeam team1 = teams.get(0);
            MatchTeam team2 = teams.get(1);
            if (team1.getAllMembers().size() != 1 || team2.getAllMembers().size() != 1) {
                return;
            }
            UUID player1Uuid = team1.getFirstMember();
            UUID player2Uuid = team2.getFirstMember();
            KitType kitType = match.getKitType();
            this.rematches.put(player1Uuid, new RematchData(player1Uuid, player2Uuid, kitType, 30, match.getArena().getSchematic()));
            this.rematches.put(player2Uuid, new RematchData(player2Uuid, player1Uuid, kitType, 30, match.getArena().getSchematic()));
        }
    }

    public RematchData getRematchData(Player player) {
        return this.rematches.get(player.getUniqueId());
    }

    public void unloadRematchData(Player player) {
        RematchData removed = this.rematches.remove(player.getUniqueId());
        if (removed != null) {
            this.rematches.remove(removed.getTarget());
            Player targetPlayer = Bukkit.getPlayer((UUID)removed.getTarget());
            if (targetPlayer != null) {
                InventoryUtils.resetInventoryDelayed(targetPlayer);
            }
        }
    }
}

