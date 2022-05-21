/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.game.event;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.GameEventItems;
import cc.fyre.potpvp.lobby.LobbyHandler;
import kotlin.Metadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lcc/fyre/potpvp/game/event/GameEventTask;", "Lorg/bukkit/scheduler/BukkitRunnable;", "()V", "run", "", "potpvp-si"})
public final class GameEventTask
extends BukkitRunnable {
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            LobbyHandler handler = PotPvP.getInstance().lobbyHandler;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (handler.isInLobby(player) || game == null || !game.getPlayers().contains(player) || !player.getInventory().contains(GameEventItems.getEventItem())) continue;
            player.getInventory().remove(Material.EMERALD);
        }
    }
}

