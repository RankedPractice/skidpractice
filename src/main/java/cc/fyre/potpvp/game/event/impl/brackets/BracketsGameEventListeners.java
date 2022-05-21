/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cc.fyre.potpvp.game.event.impl.brackets;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.PlayerGameInteractionEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerQuitGameEvent;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEventLogic;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEventLogic;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H\u0007\u00a8\u0006\u0015"}, d2={"Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameEventListeners;", "Lorg/bukkit/event/Listener;", "()V", "onEntityDamageByEntityEvent", "", "event", "Lorg/bukkit/event/entity/EntityDamageByEntityEvent;", "onFoodLevelChangeEvent", "Lorg/bukkit/event/entity/FoodLevelChangeEvent;", "onPlayerDamageEvent", "Lorg/bukkit/event/entity/EntityDamageEvent;", "onPlayerDeathEvent", "Lorg/bukkit/event/entity/PlayerDeathEvent;", "onPlayerInteractEntityEvent", "Lorg/bukkit/event/player/PlayerInteractEntityEvent;", "onPlayerMoveEvent", "Lorg/bukkit/event/player/PlayerMoveEvent;", "onPlayerQuitEvent", "Lorg/bukkit/event/player/PlayerQuitEvent;", "onPlayerQuitGameEvent", "Lcc/fyre/potpvp/game/bukkit/event/PlayerQuitGameEvent;", "potpvp-si"})
public final class BracketsGameEventListeners
implements Listener {
    @EventHandler
    public final void onPlayerMoveEvent(@NotNull PlayerMoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            BracketsGameEventLogic bracketsGameEventLogic;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            Object object = game2.getLogic();
            BracketsGameEventLogic bracketsGameEventLogic2 = bracketsGameEventLogic = object instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)object : null;
            if (bracketsGameEventLogic == null) {
                return;
            }
            BracketsGameEventLogic logic = bracketsGameEventLogic;
            Player player = event.getPlayer();
            Intrinsics.checkNotNullExpressionValue(player, "event.player");
            object = logic.get(player);
            if (object == null) {
                return;
            }
            Object participant = object;
            if (((GameTeam)participant).getStarting() && (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
                event.getPlayer().teleport(event.getFrom());
                event.getPlayer().setVelocity(new Vector(0, -1, 0));
                return;
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public final void onEntityDamageByEntityEvent(@NotNull EntityDamageByEntityEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player) {
            Entity entity = event.getEntity();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            Player player = (Player)entity;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            if (!(game2.getLogic() instanceof GameTeamEventLogic)) {
                return;
            }
            GameTeam participant = ((GameTeamEventLogic)game2.getLogic()).get(player);
            if (game2.getSpectators().contains(player)) {
                event.setCancelled(true);
                return;
            }
            if (event.getDamager() instanceof Player) {
                Entity entity2 = event.getDamager();
                if (entity2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
                }
                if (game2.getSpectators().contains((Player)entity2)) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (game2.getPlayers().contains(player)) {
                Entity entity3;
                Player opponent = null;
                if (event.getDamager() instanceof Player) {
                    entity3 = event.getDamager();
                    if (entity3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
                    }
                    opponent = (Player)entity3;
                }
                if (event.getDamager() instanceof Projectile) {
                    entity3 = event.getDamager();
                    if (entity3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Projectile");
                    }
                    if (((Projectile)entity3).getShooter() instanceof Player) {
                        Entity entity4 = event.getDamager();
                        if (entity4 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Projectile");
                        }
                        entity3 = ((Projectile)entity4).getShooter();
                        if (entity3 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
                        }
                        opponent = (Player)entity3;
                    }
                }
                if (participant != null && opponent != null) {
                    GameTeam opponentParticipant;
                    if (ArraysKt.contains(participant.getPlayers(), opponent)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (participant.getFighting() && (opponentParticipant = ((GameTeamEventLogic)game2.getLogic()).get(opponent)) != null && opponentParticipant.getFighting()) {
                        event.setCancelled(false);
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onPlayerDamageEvent(@NotNull EntityDamageEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player) {
            BracketsGameEventLogic bracketsGameEventLogic;
            Entity entity = event.getEntity();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            Player player = (Player)entity;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            GameEventLogic gameEventLogic = game2.getLogic();
            BracketsGameEventLogic bracketsGameEventLogic2 = bracketsGameEventLogic = gameEventLogic instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)gameEventLogic : null;
            if (bracketsGameEventLogic == null) {
                return;
            }
            BracketsGameEventLogic logic = bracketsGameEventLogic;
            if (game2.getPlayers().contains(player) && game2.getState() != GameState.STARTING) {
                GameTeam participant = logic.get(player);
                if (participant != null && participant.getFighting() && !participant.hasDied(player)) {
                    event.setCancelled(false);
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onPlayerInteractEntityEvent(@NotNull PlayerInteractEntityEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player && (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)) {
            Entity entity = event.getRightClicked();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            Player clicked = (Player)entity;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            GameEventLogic logic = game2.getLogic();
            GameParameterOption gameParameterOption = game2.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass());
            if (gameParameterOption == null) {
                return;
            }
            if (!(logic instanceof GameTeamEventLogic)) {
                return;
            }
            if (game2.getPlayers().contains(player) && game2.getPlayers().contains(clicked) && game2.getState() == GameState.STARTING) {
                PluginManager pluginManager = Bukkit.getPluginManager();
                Intrinsics.checkNotNullExpressionValue(player, "player");
                pluginManager.callEvent((Event)new PlayerGameInteractionEvent(player, game2));
                Bukkit.getPluginManager().callEvent((Event)new PlayerGameInteractionEvent(clicked, game2));
            }
        }
    }

    @EventHandler
    public final void onPlayerQuitEvent(@NotNull PlayerQuitEvent event) {
        BracketsGameEventLogic bracketsGameEventLogic;
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null) {
            return;
        }
        Game game2 = game;
        Object object = game2.getLogic();
        BracketsGameEventLogic bracketsGameEventLogic2 = bracketsGameEventLogic = object instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)object : null;
        if (bracketsGameEventLogic == null) {
            return;
        }
        BracketsGameEventLogic logic = bracketsGameEventLogic;
        Player player = event.getPlayer();
        Intrinsics.checkNotNullExpressionValue(player, "event.player");
        object = logic.get(player);
        if (object == null) {
            return;
        }
        Object participant = object;
        if (((GameTeam)participant).getFighting() || ((GameTeam)participant).getStarting()) {
            object = event.getPlayer();
            Intrinsics.checkNotNullExpressionValue(object, "event.player");
            ((GameTeam)participant).died((Player)object);
            logic.check();
        } else if (((GameTeam)participant).getPlayers().length == 1 || game2.getState() == GameState.STARTING) {
            logic.getParticipants().remove(participant);
        } else {
            List<Player> newPlayers = ArraysKt.toMutableList(((GameTeam)participant).getPlayers());
            newPlayers.remove(event.getPlayer());
            Collection $this$toTypedArray$iv = newPlayers;
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            Player[] playerArray = thisCollection$iv.toArray(new Player[0]);
            if (playerArray == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            ((GameTeam)participant).setPlayers(playerArray);
        }
    }

    @EventHandler
    public final void onPlayerQuitGameEvent(@NotNull PlayerQuitGameEvent event) {
        BracketsGameEventLogic bracketsGameEventLogic;
        Intrinsics.checkNotNullParameter((Object)event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null) {
            return;
        }
        Game game2 = game;
        Object object = game2.getLogic();
        BracketsGameEventLogic bracketsGameEventLogic2 = bracketsGameEventLogic = object instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)object : null;
        if (bracketsGameEventLogic == null) {
            return;
        }
        BracketsGameEventLogic logic = bracketsGameEventLogic;
        object = logic.get(event.getPlayer());
        if (object == null) {
            return;
        }
        Object participant = object;
        if (((GameTeam)participant).getFighting() || ((GameTeam)participant).getStarting()) {
            ((GameTeam)participant).died(event.getPlayer());
            logic.check();
        } else if (((GameTeam)participant).getPlayers().length == 1 || game2.getState() == GameState.STARTING) {
            logic.getParticipants().remove(participant);
        } else {
            List<Player> newPlayers = ArraysKt.toMutableList(((GameTeam)participant).getPlayers());
            newPlayers.remove(event.getPlayer());
            Collection $this$toTypedArray$iv = newPlayers;
            boolean $i$f$toTypedArray = false;
            Collection thisCollection$iv = $this$toTypedArray$iv;
            Player[] playerArray = thisCollection$iv.toArray(new Player[0]);
            if (playerArray == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            ((GameTeam)participant).setPlayers(playerArray);
        }
    }

    @EventHandler
    public final void onPlayerDeathEvent(@NotNull PlayerDeathEvent event) {
        SumoGameEventLogic sumoGameEventLogic;
        Intrinsics.checkNotNullParameter(event, "event");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null) {
            return;
        }
        Game game2 = game;
        Object object = game2.getLogic();
        SumoGameEventLogic sumoGameEventLogic2 = sumoGameEventLogic = object instanceof SumoGameEventLogic ? (SumoGameEventLogic)object : null;
        if (sumoGameEventLogic == null) {
            return;
        }
        SumoGameEventLogic logic = sumoGameEventLogic;
        Player player = event.getEntity();
        Intrinsics.checkNotNullExpressionValue(player, "event.entity");
        object = logic.get(player);
        if (object == null) {
            return;
        }
        Object participant = object;
        event.getDrops().clear();
        if (((GameTeam)participant).getFighting()) {
            object = event.getEntity();
            Intrinsics.checkNotNullExpressionValue(object, "event.entity");
            ((GameTeam)participant).died((Player)object);
            if (((GameTeam)participant).isFinished()) {
                event.getEntity().setHealth(event.getEntity().getMaxHealth());
                logic.check();
            } else {
                new BukkitRunnable(event, game2){
                    final /* synthetic */ PlayerDeathEvent $event;
                    final /* synthetic */ Game $game;
                    {
                        this.$event = $event;
                        this.$game = $game;
                    }

                    public void run() {
                        this.$event.getEntity().spigot().respawn();
                        this.$event.getEntity().teleport(this.$game.getArena().getSpectatorSpawn());
                        PluginManager pluginManager = Bukkit.getPluginManager();
                        Player player = this.$event.getEntity();
                        Intrinsics.checkNotNullExpressionValue(player, "event.entity");
                        pluginManager.callEvent((Event)new PlayerGameInteractionEvent(player, this.$game));
                    }
                }.runTaskLater((Plugin)PotPvP.getInstance(), 2L);
            }
        }
    }

    @EventHandler
    public final void onFoodLevelChangeEvent(@NotNull FoodLevelChangeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player) {
            BracketsGameEventLogic bracketsGameEventLogic;
            HumanEntity humanEntity = event.getEntity();
            if (humanEntity == null) {
                throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Player");
            }
            Player player = (Player)humanEntity;
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game2 = game;
            Object object = game2.getLogic();
            BracketsGameEventLogic bracketsGameEventLogic2 = bracketsGameEventLogic = object instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)object : null;
            if (bracketsGameEventLogic == null) {
                return;
            }
            BracketsGameEventLogic logic = bracketsGameEventLogic;
            object = logic.get(player);
            if (object == null) {
                return;
            }
            Object participant = object;
            if (!((GameTeam)participant).getFighting()) {
                event.setFoodLevel(20);
            }
        }
    }
}

