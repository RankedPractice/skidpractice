/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryMoveItemEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.InventoryHolder
 *  org.spigotmc.event.player.PlayerSpawnLocationEvent
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.lobby.listener;

import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.lobby.listener.LobbyParkourListener;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import rip.bridge.qlib.menu.Menu;

public final class LobbyGeneralListener
implements Listener {
    private final LobbyHandler lobbyHandler;

    public LobbyGeneralListener(LobbyHandler lobbyHandler) {
        this.lobbyHandler = lobbyHandler;
    }

    @EventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        LobbyParkourListener.Parkour parkour = LobbyParkourListener.getParkourMap().get(event.getPlayer().getUniqueId());
        if (parkour != null && parkour.getCheckpoint() != null) {
            event.setSpawnLocation(parkour.getCheckpoint().getLocation());
            return;
        }
        event.setSpawnLocation(this.lobbyHandler.getLobbyLocation());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.lobbyHandler.returnToLobby(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player)event.getEntity();
        if (this.lobbyHandler.isInLobby(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                this.lobbyHandler.returnToLobby(player);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (this.lobbyHandler.isInLobby((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (this.lobbyHandler.isInLobby(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!this.lobbyHandler.isInLobby(player)) {
            return;
        }
        Menu openMenu = (Menu)Menu.currentlyOpenedMenus.get(player.getName());
        if (player.hasMetadata("Build") || openMenu != null && openMenu.isNoncancellingInventory()) {
            event.getItemDrop().remove();
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player clicked = (Player)event.getWhoClicked();
        if (!this.lobbyHandler.isInLobby(clicked) || clicked.hasMetadata("Build") || Menu.currentlyOpenedMenus.containsKey(clicked.getName())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player clicked = (Player)event.getWhoClicked();
        if (!this.lobbyHandler.isInLobby(clicked) || clicked.hasMetadata("Build") || Menu.currentlyOpenedMenus.containsKey(clicked.getName())) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.lobbyHandler.isInLobby(event.getEntity())) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        InventoryHolder inventoryHolder = event.getSource().getHolder();
        if (inventoryHolder instanceof Player) {
            Player player = (Player)inventoryHolder;
            if (!this.lobbyHandler.isInLobby(player) || Menu.currentlyOpenedMenus.containsKey(player.getName())) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameMode gameMode = event.getPlayer().getGameMode();
        if (this.lobbyHandler.isInLobby(event.getPlayer()) && gameMode != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }
        if (this.lobbyHandler.isInLobby(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

