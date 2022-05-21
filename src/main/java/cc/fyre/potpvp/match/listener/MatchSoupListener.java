/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.HealingMethod;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class MatchSoupListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player;
        if (!event.hasItem() || event.getItem().getType() != Material.MUSHROOM_SOUP || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(player = event.getPlayer());
        if (match != null && match.getKitType().getHealingMethod() == HealingMethod.SOUP && player.getHealth() <= 19.0) {
            double current = player.getHealth();
            double max = player.getMaxHealth();
            player.getItemInHand().setType(Material.BOWL);
            player.setHealth(Math.min(max, current + 7.0));
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying((Player)event.getEntity());
        if (match != null && match.getKitType().getHealingMethod() == HealingMethod.SOUP) {
            event.setFoodLevel(20);
        }
    }
}

