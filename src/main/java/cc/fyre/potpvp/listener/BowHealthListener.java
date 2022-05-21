/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.util.PlayerUtils
 */
package cc.fyre.potpvp.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.util.PlayerUtils;

public final class BowHealthListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || !(event.getDamager() instanceof Arrow)) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Player hit = (Player)event.getEntity();
        Player damager = PlayerUtils.getDamageSource((Entity)event.getDamager());
        if (damager != null) {
            Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> {
                if (!matchHandler.isPlayingMatch(hit)) {
                    return;
                }
                int outOf20 = (int)Math.ceil(hit.getHealth());
                damager.sendMessage(ChatColor.GOLD + hit.getName() + "'s health: " + ChatColor.RED.toString() + (double)outOf20 / 2.0 + ChatColor.DARK_RED + "\u2764");
            }, 1L);
        }
    }
}

