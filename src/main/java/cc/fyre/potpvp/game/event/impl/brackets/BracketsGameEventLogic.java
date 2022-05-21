/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Firework
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.FireworkMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.game.event.impl.brackets;

import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.PlayerGameInteractionEvent;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameKitParameter;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEvent;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.game.util.GameEventCountdown;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamEventLogic;
import java.util.concurrent.ThreadLocalRandom;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.qLib;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0006\u0010\u0011\u001a\u00020\u000eJ\n\u0010\u0012\u001a\u0004\u0018\u00010\u0010H\u0002J\u0012\u0010\u0013\u001a\u0004\u0018\u00010\u00102\b\u0010\u0014\u001a\u0004\u0018\u00010\u0010J\u0006\u0010\u0015\u001a\u00020\bJ\u000f\u0010\u0016\u001a\u0004\u0018\u00010\bH\u0016\u00a2\u0006\u0002\u0010\u0017J\n\u0010\u0018\u001a\u0004\u0018\u00010\u0010H\u0002J\u0010\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u000eH\u0002J\b\u0010\u001b\u001a\u00020\u000eH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f\u00a8\u0006\u001c"}, d2={"Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameEventLogic;", "Lcc/fyre/potpvp/game/util/team/GameTeamEventLogic;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lcc/fyre/potpvp/game/Game;)V", "getGame", "()Lcc/fyre/potpvp/game/Game;", "roundsPlayed", "", "getRoundsPlayed", "()I", "setRoundsPlayed", "(I)V", "broadcastWinner", "", "winner", "Lcc/fyre/potpvp/game/util/team/GameTeam;", "check", "getLoser", "getNextParticipant", "exclude", "getPlayersLeft", "getRound", "()Ljava/lang/Integer;", "getWinner", "launchFireworksWinner", "next", "start", "potpvp-si"})
public class BracketsGameEventLogic
extends GameTeamEventLogic {
    @NotNull
    private final Game game;
    private int roundsPlayed;

    public BracketsGameEventLogic(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        super(game);
        this.game = game;
    }

    @NotNull
    public final Game getGame() {
        return this.game;
    }

    public final int getRoundsPlayed() {
        return this.roundsPlayed;
    }

    public final void setRoundsPlayed(int n) {
        this.roundsPlayed = n;
    }

    @Override
    public void start() {
        super.start();
        this.next();
    }

    public final void check() {
        GameTeam gameTeam = this.getWinner();
        if (gameTeam == null) {
            return;
        }
        GameTeam winner = gameTeam;
        GameTeam object = this.getLoser();
        if (object == null) {
            return;
        }
        GameTeam loser = object;
        BracketsGameEventLogic bracketsGameEventLogic = this;
        int n = bracketsGameEventLogic.roundsPlayed;
        bracketsGameEventLogic.roundsPlayed = n + 1;
        this.game.setStartingAt(0L);
        winner.reset();
        GameTeam gameTeam2 = winner;
        gameTeam2.setRound(gameTeam2.getRound() + 1);
        winner.setFighting(false);
        this.getParticipants().remove(loser);
        for (Player player : winner.getPlayers()) {
            this.game.reset(player);
        }
        for (Player player : loser.getPlayers()) {
            this.game.addSpectator(player);
        }
        String[] stringArray = new String[]{ChatColor.GREEN.toString() + winner.getName(false) + ChatColor.GRAY + " has eliminated " + ChatColor.RED + loser.getName(false) + ChatColor.GRAY + '.'};
        this.game.sendMessage(stringArray);
        if (this.getNextParticipant(winner) == null) {
            this.game.setPendingEnd(true);
            this.game.setWinner(winner);
            this.broadcastWinner(winner);
            this.launchFireworksWinner(winner);
            new BukkitRunnable(this){
                final /* synthetic */ BracketsGameEventLogic this$0;
                {
                    this.this$0 = $receiver;
                }

                public void run() {
                    this.this$0.getGame().end();
                }
            }.runTaskLater((Plugin)qLib.getInstance(), 100L);
        } else {
            this.next();
        }
    }

    private final void broadcastWinner(GameTeam winner) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String[] stringArray = new String[]{"", winner.getName(false) + ' ' + ChatColor.GOLD + "has " + ChatColor.GREEN + "won " + ChatColor.GOLD + "the " + ChatColor.WHITE + this.game.getEvent().getName() + " Event" + ChatColor.GOLD + '!', ""};
            player.sendMessage(stringArray);
        }
    }

    private final void launchFireworksWinner(GameTeam winner) {
        new BukkitRunnable(winner){
            private int i;
            final /* synthetic */ GameTeam $winner;
            {
                this.$winner = $winner;
            }

            public void run() {
                launchFireworksWinner.1 var1_1 = this;
                int n = var1_1.i;
                var1_1.i = n + 1;
                Player[] $this$forEach$iv = this.$winner.getPlayers();
                boolean $i$f$forEach = false;
                Player[] playerArray = $this$forEach$iv;
                int n2 = playerArray.length;
                for (int i = 0; i < n2; ++i) {
                    Color color;
                    Player element$iv;
                    Player it = element$iv = playerArray[i];
                    boolean bl = false;
                    int n3 = ThreadLocalRandom.current().nextInt(3);
                    switch (n3) {
                        case 0: {
                            color = Color.WHITE;
                            break;
                        }
                        case 1: {
                            color = Color.YELLOW;
                            break;
                        }
                        case 2: {
                            color = Color.ORANGE;
                            break;
                        }
                        case 3: {
                            color = Color.RED;
                            break;
                        }
                        default: {
                            color = Color.GREEN;
                        }
                    }
                    Color color2 = color;
                    Entity entity = it.getLocation().getWorld().spawnEntity(it.getLocation(), EntityType.FIREWORK);
                    if (entity == null) {
                        throw new NullPointerException("null cannot be cast to non-null type org.bukkit.entity.Firework");
                    }
                    Firework firework = (Firework)entity;
                    FireworkMeta meta = firework.getFireworkMeta();
                    meta.setPower(10);
                    meta.addEffect(FireworkEffect.builder().withColor(color2).trail(true).build());
                    firework.setFireworkMeta(meta);
                }
                if (this.i >= 4) {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)qLib.getInstance(), 0L, 20L);
    }

    /*
     * WARNING - void declaration
     */
    private final void next() {
        GameTeam opponent;
        GameTeam fighter = this.getNextParticipant(null);
        if (!Intrinsics.areEqual(fighter, opponent = this.getNextParticipant(fighter)) && fighter != null && opponent != null) {
            int index;
            int index2;
            Player it;
            Player element$iv;
            int n;
            this.game.setStartingAt(System.currentTimeMillis() + 3250L);
            if (fighter.getRound() != opponent.getRound()) {
                int n2 = fighter.getRound();
                int n3 = opponent.getRound();
                boolean bl = false;
                fighter.setRound(Math.max(n2, n3));
                opponent.setRound(fighter.getRound());
            }
            Player[] $this$forEach$iv = fighter.getPlayers();
            boolean $i$f$forEach = false;
            Player[] playerArray = $this$forEach$iv;
            int n4 = playerArray.length;
            for (n = 0; n < n4; ++n) {
                it = element$iv = playerArray[n];
                boolean bl = false;
                ((CraftPlayer)it).setKbProfile(this.getGame().getEvent().getKnockback());
                ((CraftPlayer)it).getInventory().setContents(new ItemStack[36]);
                it.updateInventory();
                it.playSound(((CraftPlayer)it).getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
                it.sendMessage("");
                it.sendMessage(ChatColor.GRAY.toString() + "Your Opponent: " + ChatColor.GOLD + opponent.getName(false));
                it.sendMessage("");
            }
            $this$forEach$iv = opponent.getPlayers();
            $i$f$forEach = false;
            playerArray = $this$forEach$iv;
            n4 = playerArray.length;
            for (n = 0; n < n4; ++n) {
                it = element$iv = playerArray[n];
                boolean bl = false;
                ((CraftPlayer)it).setKbProfile(this.getGame().getEvent().getKnockback());
                ((CraftPlayer)it).getInventory().setContents(new ItemStack[36]);
                it.updateInventory();
                it.playSound(((CraftPlayer)it).getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
                it.sendMessage("");
                it.sendMessage(ChatColor.GRAY.toString() + "Your Opponent: " + ChatColor.GOLD + fighter.getName(false));
                it.sendMessage("");
            }
            Player[] $this$forEachIndexed$iv = fighter.getPlayers();
            boolean $i$f$forEachIndexed = false;
            int index$iv = 0;
            for (Player item$iv : $this$forEachIndexed$iv) {
                void player;
                int n5 = index$iv++;
                Player bl = item$iv;
                index2 = n5;
                boolean bl2 = false;
                player.teleport(this.getGame().getFirstSpawnLocations()[index2]);
            }
            $this$forEachIndexed$iv = opponent.getPlayers();
            $i$f$forEachIndexed = false;
            index$iv = 0;
            for (Player item$iv : $this$forEachIndexed$iv) {
                int n6 = index$iv++;
                Player player = item$iv;
                index2 = n6;
                boolean bl = false;
                player.teleport(this.getGame().getSecondSpawnLocations()[index2]);
            }
            fighter.setStarting(true);
            opponent.setStarting(true);
            new GameEventCountdown(3, arg_0 -> BracketsGameEventLogic.next$lambda-4(this, arg_0), () -> BracketsGameEventLogic.next$lambda-5(fighter, opponent), (Runnable)new BukkitRunnable(fighter, opponent, this){
                final /* synthetic */ GameTeam $fighter;
                final /* synthetic */ GameTeam $opponent;
                final /* synthetic */ BracketsGameEventLogic this$0;
                {
                    this.$fighter = $fighter;
                    this.$opponent = $opponent;
                    this.this$0 = $receiver;
                }

                public void run() {
                    this.$fighter.setStarting(false);
                    this.$fighter.setFighting(true);
                    this.$opponent.setStarting(false);
                    this.$opponent.setFighting(true);
                    if (Intrinsics.areEqual(this.this$0.getGame().getEvent(), SumoGameEvent.INSTANCE)) {
                        Player player;
                        Player element$iv;
                        int n;
                        Player[] $this$forEach$iv = this.$fighter.getPlayers();
                        boolean $i$f$forEach = false;
                        Player[] playerArray = $this$forEach$iv;
                        int n2 = playerArray.length;
                        for (n = 0; n < n2; ++n) {
                            player = element$iv = playerArray[n];
                            boolean bl = false;
                            player.getInventory().setContents(new ItemStack[36]);
                            player.updateInventory();
                        }
                        $this$forEach$iv = this.$opponent.getPlayers();
                        $i$f$forEach = false;
                        playerArray = $this$forEach$iv;
                        n2 = playerArray.length;
                        for (n = 0; n < n2; ++n) {
                            player = element$iv = playerArray[n];
                            boolean bl = false;
                            player.getInventory().setContents(new ItemStack[36]);
                            player.updateInventory();
                        }
                    }
                }
            }, this.game);
            GameParameterOption kit = this.game.getParameter(BracketsGameKitParameter.BracketsGameKitOption.class);
            Player[] $this$forEachIndexed$iv2 = fighter.getPlayers();
            boolean $i$f$forEachIndexed2 = false;
            int index$iv2 = 0;
            for (Player item$iv : $this$forEachIndexed$iv2) {
                void player;
                int n7 = index$iv2++;
                Player index3 = item$iv;
                index = n7;
                boolean bl = false;
                this.getGame().getSpectators().remove(player);
                player.getInventory().clear();
                player.setSprinting(false);
                player.updateInventory();
                player.setVelocity(new Vector());
                this.getGame().getSpectators().remove(player);
                Bukkit.getPluginManager().callEvent((Event)new PlayerGameInteractionEvent((Player)player, this.getGame()));
                if (kit == null || !(kit instanceof BracketsGameKitParameter.BracketsGameKitOption)) continue;
                ((BracketsGameKitParameter.BracketsGameKitOption)kit).apply((Player)player);
            }
            $this$forEachIndexed$iv2 = opponent.getPlayers();
            $i$f$forEachIndexed2 = false;
            index$iv2 = 0;
            for (Player item$iv : $this$forEachIndexed$iv2) {
                int n8 = index$iv2++;
                Player player = item$iv;
                index = n8;
                boolean bl = false;
                this.getGame().getSpectators().remove(player);
                player.getInventory().clear();
                player.setSprinting(false);
                player.setVelocity(new Vector());
                player.updateInventory();
                this.getGame().getSpectators().remove(player);
                Bukkit.getPluginManager().callEvent((Event)new PlayerGameInteractionEvent(player, this.getGame()));
                if (kit == null || !(kit instanceof BracketsGameKitParameter.BracketsGameKitOption)) continue;
                ((BracketsGameKitParameter.BracketsGameKitOption)kit).apply(player);
            }
            return;
        }
        this.game.end();
    }

    @Nullable
    public Integer getRound() {
        int n;
        GameTeam gameTeam = this.getNextParticipant(null);
        return 1 + (gameTeam == null ? 0 : (n = gameTeam.getRound()));
    }

    @Nullable
    public final GameTeam getNextParticipant(@Nullable GameTeam exclude) {
        GameTeam current = null;
        for (GameTeam participant : this.getParticipants()) {
            if (Intrinsics.areEqual(participant, exclude) || current != null && participant.getRound() >= current.getRound()) continue;
            current = participant;
        }
        return current;
    }

    private final GameTeam getWinner() {
        for (GameTeam participant : this.getParticipants()) {
            if (!participant.getFighting() || participant.isFinished()) continue;
            return participant;
        }
        return null;
    }

    private final GameTeam getLoser() {
        for (GameTeam participant : this.getParticipants()) {
            if (!participant.getFighting() || !participant.isFinished()) continue;
            return participant;
        }
        return null;
    }

    public final int getPlayersLeft() {
        if (this.game.getState() == GameState.STARTING) {
            return this.game.getPlayers().size();
        }
        return this.getParticipants().size();
    }

    private static final String next$lambda-4(BracketsGameEventLogic this$0, Integer it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(it, "it");
        StringBuilder stringBuilder = new StringBuilder().append(ChatColor.GOLD).append("Round ");
        Integer n = this$0.getRound();
        Intrinsics.checkNotNull(n);
        int n2 = 1;
        return stringBuilder.append(n + 1).append(' ').append(ChatColor.GRAY).append("will start in ").append(ChatColor.GOLD).append(it).append(' ').append(ChatColor.GRAY).append("second").append(it == n2 ? "" : "s").append(ChatColor.GRAY).append('.').toString();
    }

    private static final String next$lambda-5(GameTeam $fighter, GameTeam $opponent) {
        return $fighter.getName(true) + ChatColor.GRAY + " vs. " + ChatColor.RESET + $opponent.getName(true);
    }
}

