/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.PluginManager
 */
package cc.fyre.potpvp.game;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.PlayerQuitGameEvent;
import cc.fyre.potpvp.game.event.GameEvent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H\u0007\u00a8\u0006\u0015"}, d2={"Lcc/fyre/potpvp/game/GameListeners;", "Lorg/bukkit/event/Listener;", "()V", "onEntityDamageByEntityEvent", "", "event", "Lorg/bukkit/event/entity/EntityDamageByEntityEvent;", "onFoodLevelChangeEvent", "Lorg/bukkit/event/entity/FoodLevelChangeEvent;", "onPlayerDamageEvent", "Lorg/bukkit/event/entity/EntityDamageEvent;", "onPlayerDropItemEvent", "Lorg/bukkit/event/player/PlayerDropItemEvent;", "onPlayerInteractEvent", "Lorg/bukkit/event/player/PlayerInteractEvent;", "onPlayerInventoryClickEvent", "Lorg/bukkit/event/inventory/InventoryClickEvent;", "onPlayerPickupItemEvent", "Lorg/bukkit/event/player/PlayerPickupItemEvent;", "onPlayerQuitEvent", "Lorg/bukkit/event/player/PlayerQuitEvent;", "potpvp-si"})
public final class GameListeners
implements Listener {
    @EventHandler
    public final void onPlayerDamageEvent(@NotNull EntityDamageEvent event) {
        Game game;
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player && (game = PotPvP.getInstance().gameHandler.getOngoingGame()) != null && game.getState() == GameState.STARTING) {
            Entity entity = event.getEntity();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            if (game.getPlayers().contains((Player)entity)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
        Game game;
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getDamager() instanceof Player && (game = PotPvP.getInstance().gameHandler.getOngoingGame()) != null) {
            Entity entity = event.getDamager();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            if (game.getSpectators().contains((Player)entity)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onPlayerQuitEvent(@NotNull PlayerQuitEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getPlayers().contains(event.getPlayer())) {
            PluginManager pluginManager = Bukkit.getPluginManager();
            Player player = event.getPlayer();
            Intrinsics.checkNotNullExpressionValue(player, "event.player");
            pluginManager.callEvent((Event)new PlayerQuitGameEvent(player, game));
        }
    }

    @EventHandler
    public final void onPlayerDropItemEvent(@NotNull PlayerDropItemEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getPlayers().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public final void onPlayerInventoryClickEvent(@NotNull InventoryClickEvent event) {
        HumanEntity humanEntity;
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getState() == GameState.STARTING) {
            humanEntity = event.getWhoClicked();
            if (humanEntity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            if (game.getPlayers().contains((Player)humanEntity)) {
                event.setCancelled(true);
            }
        }
        if (game != null) {
            humanEntity = event.getWhoClicked();
            if (humanEntity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            if (game.getSpectators().contains((Player)humanEntity)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onPlayerPickupItemEvent(@NotNull PlayerPickupItemEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getSpectators().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public final void onFoodLevelChangeEvent(@NotNull FoodLevelChangeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getState() == GameState.STARTING) {
            HumanEntity humanEntity = event.getEntity();
            if (humanEntity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            if (game.getSpectators().contains((Player)humanEntity)) {
                event.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public final void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && Intrinsics.areEqual(GameEvent.Companion.getLeaveItem(), event.getItem())) {
            PluginManager pluginManager = Bukkit.getPluginManager();
            Player player = event.getPlayer();
            Intrinsics.checkNotNullExpressionValue(player, "event.player");
            pluginManager.callEvent((Event)new PlayerQuitGameEvent(player, game));
            event.getPlayer().sendMessage(ChatColor.RED.toString() + "You left the " + game.getEventDisplayName() + " event.");
            return;
        }
        if (game != null && game.getSpectators().contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

