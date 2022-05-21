/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package cc.fyre.potpvp.game.event.impl.sumo;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEvent;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeam;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\u0007\u00a8\u0006\u000b"}, d2={"Lcc/fyre/potpvp/game/event/impl/sumo/SumoGameEventListeners;", "Lorg/bukkit/event/Listener;", "()V", "onFoodLevelChangeEvent", "", "event", "Lorg/bukkit/event/entity/FoodLevelChangeEvent;", "onPlayerDamageEvent", "Lorg/bukkit/event/entity/EntityDamageEvent;", "onPlayerMoveEvent", "Lorg/bukkit/event/player/PlayerMoveEvent;", "potpvp-si"})
public final class SumoGameEventListeners
implements Listener {
    @EventHandler
    public final void onPlayerMoveEvent(@NotNull PlayerMoveEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            SumoGameEventLogic sumoGameEventLogic;
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
            Player player = event.getPlayer();
            Intrinsics.checkNotNullExpressionValue(player, "event.player");
            object = logic.get(player);
            if (object == null) {
                return;
            }
            Object participant = object;
            if (event.getTo().getBlockY() + 5 < game2.getFirstSpawnLocations()[0].getBlockY() && ((GameTeam)participant).getFighting()) {
                object = event.getPlayer();
                Intrinsics.checkNotNullExpressionValue(object, "event.player");
                ((GameTeam)participant).died((Player)object);
                if (((GameTeam)participant).isFinished()) {
                    logic.check();
                } else {
                    object = event.getPlayer();
                    Intrinsics.checkNotNullExpressionValue(object, "event.player");
                    game2.addSpectator((Player)object);
                }
            }
        }
    }

    @EventHandler
    public final void onPlayerDamageEvent(@NotNull EntityDamageEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player) {
            SumoGameEventLogic sumoGameEventLogic;
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
            SumoGameEventLogic sumoGameEventLogic2 = sumoGameEventLogic = gameEventLogic instanceof SumoGameEventLogic ? (SumoGameEventLogic)gameEventLogic : null;
            if (sumoGameEventLogic == null) {
                return;
            }
            SumoGameEventLogic logic = sumoGameEventLogic;
            if (!Intrinsics.areEqual(game2.getEvent(), SumoGameEvent.INSTANCE)) {
                return;
            }
            if (game2.getPlayers().contains(player) && game2.getState() != GameState.STARTING) {
                GameTeam participant = logic.get(player);
                if (participant != null && participant.getFighting() && !participant.hasDied(player)) {
                    event.setDamage(0.0);
                    event.setCancelled(false);
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onFoodLevelChangeEvent(@NotNull FoodLevelChangeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getEntity() instanceof Player) {
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
            if (!Intrinsics.areEqual(game2.getEvent(), SumoGameEvent.INSTANCE)) {
                return;
            }
            if (game2.getPlayers().contains(player)) {
                event.setFoodLevel(20);
            }
        }
    }
}

