/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.nametag.FrozenNametagHandler
 */
package cc.fyre.potpvp.game.event;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.GameStateChangeEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerGameInteractionEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerJoinGameEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerQuitGameEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerSubmitVoteEvent;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.event.GameEventItems;
import cc.fyre.potpvp.util.VisibilityUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.nametag.FrozenNametagHandler;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\bH\u0007J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0010H\u0007\u00a8\u0006\u0011"}, d2={"Lcc/fyre/potpvp/game/event/GameEventListeners;", "Lorg/bukkit/event/Listener;", "()V", "onGameStateChangeEvent", "", "event", "Lcc/fyre/potpvp/game/bukkit/event/GameStateChangeEvent;", "onPlayerGameInteractionEvent", "Lcc/fyre/potpvp/game/bukkit/event/PlayerGameInteractionEvent;", "onPlayerInteractEvent", "Lorg/bukkit/event/player/PlayerInteractEvent;", "onPlayerJoinGameEvent", "Lcc/fyre/potpvp/game/bukkit/event/PlayerJoinGameEvent;", "onPlayerQuitGameEvent", "Lcc/fyre/potpvp/game/bukkit/event/PlayerQuitGameEvent;", "onPlayerSubmitVoteEvent", "Lcc/fyre/potpvp/game/bukkit/event/PlayerSubmitVoteEvent;", "potpvp-si"})
public final class GameEventListeners
implements Listener {
    @EventHandler
    public final void onPlayerInteractEvent(@NotNull PlayerInteractEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Player player = event.getPlayer();
        if (PotPvP.getInstance().lobbyHandler.isInLobby(player)) {
            if (event.getItem() != null && Intrinsics.areEqual(event.getItem(), GameEventItems.getEventItem())) {
                Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
                if (game == null) {
                    return;
                }
                Game game2 = game;
                if (game2.getPlayers().contains(player)) {
                    return;
                }
                if (game2.getState() == GameState.STARTING) {
                    if (game2.getMaxPlayers() > 0 && game2.getPlayers().size() >= game2.getMaxPlayers() && !player.hasPermission("potpvp.event.join.bypass")) {
                        player.sendMessage(ChatColor.RED + "This event is currently full! Sorry!");
                        return;
                    }
                    Intrinsics.checkNotNullExpressionValue(player, "player");
                    game2.add(player);
                } else {
                    Intrinsics.checkNotNullExpressionValue(player, "player");
                    game2.addSpectator(player);
                }
            }
        } else if (event.getItem() != null && event.getItem().isSimilar(GameEventItems.getVoteItem())) {
            Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (game == null) {
                return;
            }
            Game game3 = game;
            if (!game3.getPlayers().contains(player)) {
                return;
            }
            if (game3.getState() == GameState.STARTING) {
                new BukkitRunnable(event, player, game3){
                    final /* synthetic */ PlayerInteractEvent $event;
                    final /* synthetic */ Player $player;
                    final /* synthetic */ Game $game;
                    {
                        this.$event = $event;
                        this.$player = $player;
                        this.$game = $game;
                    }

                    public void run() {
                        Matcher matcher = GameEvent.Companion.getVOTE_PATTERN().matcher(this.$event.getItem().getItemMeta().getDisplayName());
                        if (matcher.matches()) {
                            String mapName = matcher.group(2);
                            PluginManager pluginManager = Bukkit.getServer().getPluginManager();
                            Player player = this.$player;
                            Intrinsics.checkNotNullExpressionValue(player, "player");
                            Intrinsics.checkNotNullExpressionValue(mapName, "mapName");
                            pluginManager.callEvent((Event)new PlayerSubmitVoteEvent(player, this.$game, mapName));
                        }
                    }
                }.runTaskAsynchronously((Plugin)PotPvP.getInstance());
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventHandler
    public final void onPlayerSubmitVoteEvent(@NotNull PlayerSubmitVoteEvent event) {
        void $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter((Object)event, "event");
        Iterable $this$filter$iv = event.getGame().getVoteOptions().keySet();
        boolean $i$f$filter = false;
        Object object = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            ArenaSchematic it = (ArenaSchematic)element$iv$iv;
            boolean bl = false;
            if (!Intrinsics.areEqual(it.name, event.getMapName())) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List arena = (List)destination$iv$iv;
        if (arena.isEmpty()) {
            event.getPlayer().sendMessage(ChatColor.RED + "Could not vote for " + event.getMapName() + ": Map not found");
        } else {
            ArenaSchematic arena2 = (ArenaSchematic)arena.get(0);
            if (event.getGame().getVotes().containsKey(event.getPlayer().getUniqueId()) && event.getGame().getVotes().get(event.getPlayer().getUniqueId()) == arena2) {
                event.getPlayer().sendMessage(ChatColor.RED + "You already voted for that map.");
                return;
            }
            AtomicInteger atomicInteger = event.getGame().getVoteOptions().get(arena2);
            Intrinsics.checkNotNull(atomicInteger);
            atomicInteger.incrementAndGet();
            Map<UUID, ArenaSchematic> map = event.getGame().getVotes();
            object = event.getPlayer().getUniqueId();
            Intrinsics.checkNotNullExpressionValue(object, "event.player.uniqueId");
            boolean bl = false;
            map.put((UUID)object, arena2);
            event.getPlayer().sendMessage(ChatColor.GRAY + "You voted for " + ChatColor.GOLD + event.getMapName() + ChatColor.GRAY + '.');
        }
    }

    @EventHandler
    public final void onGameStateChangeEvent(@NotNull GameStateChangeEvent event) {
        block4: {
            Game game;
            block5: {
                block3: {
                    Intrinsics.checkNotNullParameter((Object)event, "event");
                    game = event.getGame();
                    if (event.getTo() != GameState.STARTING) break block3;
                    PotPvP.getInstance().gameHandler.setOngoingGame(game);
                    break block4;
                }
                if (event.getTo() != GameState.RUNNING) break block5;
                Iterable $this$forEach$iv = game.getPlayers();
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    Player it = (Player)element$iv;
                    boolean bl = false;
                    game.reset(it);
                }
                break block4;
            }
            if (event.getTo() != GameState.ENDED) break block4;
            PotPvP.getInstance().gameHandler.setOngoingGame(null);
            if (game.getArena() != Game.Companion.getLATE_INIT_ARENA()) {
                PotPvP.getInstance().getArenaHandler().releaseArena(game.getArena());
            }
            for (Player player : game.getPlayers()) {
                FrozenNametagHandler.reloadPlayer((Player)player);
                FrozenNametagHandler.reloadOthersFor((Player)player);
                VisibilityUtils.updateVisibility(player);
                PotPvP.getInstance().lobbyHandler.returnToLobby(player);
            }
        }
    }

    @EventHandler
    public final void onPlayerJoinGameEvent(@NotNull PlayerJoinGameEvent event) {
        Intrinsics.checkNotNullParameter((Object)event, "event");
        FrozenNametagHandler.reloadPlayer((Player)event.getPlayer());
        FrozenNametagHandler.reloadOthersFor((Player)event.getPlayer());
        for (Player player : event.getGame().getPlayers()) {
            VisibilityUtils.updateVisibility(player);
        }
    }

    @EventHandler
    public final void onPlayerQuitGameEvent(@NotNull PlayerQuitGameEvent event) {
        Intrinsics.checkNotNullParameter((Object)event, "event");
        FrozenNametagHandler.reloadPlayer((Player)event.getPlayer());
        FrozenNametagHandler.reloadOthersFor((Player)event.getPlayer());
        PotPvP.getInstance().lobbyHandler.returnToLobby(event.getPlayer());
    }

    @EventHandler
    public final void onPlayerGameInteractionEvent(@NotNull PlayerGameInteractionEvent event) {
        Intrinsics.checkNotNullParameter((Object)event, "event");
        FrozenNametagHandler.reloadPlayer((Player)event.getPlayer());
        FrozenNametagHandler.reloadOthersFor((Player)event.getPlayer());
        VisibilityUtils.updateVisibility(event.getPlayer());
    }
}

