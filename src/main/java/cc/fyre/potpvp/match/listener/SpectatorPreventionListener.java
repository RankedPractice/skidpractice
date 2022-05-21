/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryMoveItemEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerBucketFillEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.InventoryHolder
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.event.SettingUpdateEvent;
import cc.fyre.potpvp.util.VisibilityUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public final class SpectatorPreventionListener
implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchSpectating(event.getPlayer());
        if (match != null) {
            match.removeSpectator(event.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch(event.getEntity())) {
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player damager;
        MatchHandler matchHandler;
        if (event.getDamager() instanceof Player && (matchHandler = PotPvP.getInstance().getMatchHandler()).isSpectatingMatch(damager = (Player)event.getDamager())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropitem(PlayerDropItemEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupitem(PlayerPickupItemEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch((Player)event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isSpectatingMatch((Player)event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        MatchHandler matchHandler;
        InventoryHolder inventoryHolder = event.getSource().getHolder();
        if (inventoryHolder instanceof Player && (matchHandler = PotPvP.getInstance().getMatchHandler()).isSpectatingMatch((Player)inventoryHolder)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() == Setting.VIEW_OTHER_SPECTATORS) {
            VisibilityUtils.updateVisibility(event.getPlayer());
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        MatchHandler matchHandler;
        Entity shooter = (Entity)event.getEntity().getShooter();
        if (shooter instanceof Player && (matchHandler = PotPvP.getInstance().getMatchHandler()).isSpectatingMatch((Player)shooter)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (!(entity instanceof Player) || !matchHandler.isSpectatingMatch((Player)entity)) continue;
            event.setIntensity(entity, 0.0);
        }
    }
}

