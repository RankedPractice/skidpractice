/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.game.util;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import java.util.function.Function;
import java.util.function.Supplier;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u0001:\u0001\u001aB?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001b"}, d2={"Lcc/fyre/potpvp/game/util/GameEventCountdown;", "", "duration", "", "intervalFunction", "Ljava/util/function/Function;", "", "startSupplier", "Ljava/util/function/Supplier;", "runnable", "Ljava/lang/Runnable;", "game", "Lcc/fyre/potpvp/game/Game;", "(ILjava/util/function/Function;Ljava/util/function/Supplier;Ljava/lang/Runnable;Lcc/fyre/potpvp/game/Game;)V", "getDuration", "()I", "setDuration", "(I)V", "getGame", "()Lcc/fyre/potpvp/game/Game;", "getIntervalFunction", "()Ljava/util/function/Function;", "getRunnable", "()Ljava/lang/Runnable;", "getStartSupplier", "()Ljava/util/function/Supplier;", "Countdown", "potpvp-si"})
public final class GameEventCountdown {
    private int duration;
    @NotNull
    private final Function<Integer, String> intervalFunction;
    @NotNull
    private final Supplier<String> startSupplier;
    @NotNull
    private final Runnable runnable;
    @NotNull
    private final Game game;

    public GameEventCountdown(int duration, @NotNull Function<Integer, String> intervalFunction, @NotNull Supplier<String> startSupplier, @NotNull Runnable runnable, @NotNull Game game) {
        Intrinsics.checkNotNullParameter(intervalFunction, "intervalFunction");
        Intrinsics.checkNotNullParameter(startSupplier, "startSupplier");
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        Intrinsics.checkNotNullParameter(game, "game");
        this.duration = duration;
        this.intervalFunction = intervalFunction;
        this.startSupplier = startSupplier;
        this.runnable = runnable;
        this.game = game;
        new Countdown().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), 0L, 20L);
    }

    public final int getDuration() {
        return this.duration;
    }

    public final void setDuration(int n) {
        this.duration = n;
    }

    @NotNull
    public final Function<Integer, String> getIntervalFunction() {
        return this.intervalFunction;
    }

    @NotNull
    public final Supplier<String> getStartSupplier() {
        return this.startSupplier;
    }

    @NotNull
    public final Runnable getRunnable() {
        return this.runnable;
    }

    @NotNull
    public final Game getGame() {
        return this.game;
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016\u00a8\u0006\u0005"}, d2={"Lcc/fyre/potpvp/game/util/GameEventCountdown$Countdown;", "Lorg/bukkit/scheduler/BukkitRunnable;", "(Lcc/fyre/potpvp/game/util/GameEventCountdown;)V", "run", "", "potpvp-si"})
    public final class Countdown
    extends BukkitRunnable {
        public Countdown() {
            Intrinsics.checkNotNullParameter(GameEventCountdown.this, "this$0");
        }

        public void run() {
            String string;
            Object object;
            if (GameEventCountdown.this.getDuration() == -1) {
                this.cancel();
                return;
            }
            if (GameEventCountdown.this.getDuration() > 0) {
                Game game = GameEventCountdown.this.getGame();
                object = new String[1];
                string = GameEventCountdown.this.getIntervalFunction().apply(GameEventCountdown.this.getDuration());
                Intrinsics.checkNotNullExpressionValue(string, "intervalFunction.apply(duration)");
                object[0] = string;
                game.sendMessage((String[])object);
            } else {
                Game game = GameEventCountdown.this.getGame();
                object = new String[1];
                string = GameEventCountdown.this.getStartSupplier().get();
                Intrinsics.checkNotNullExpressionValue(string, "startSupplier.get()");
                object[0] = string;
                game.sendMessage((String[])object);
            }
            for (Player player : GameEventCountdown.this.getGame().getPlayers()) {
                if (GameEventCountdown.this.getDuration() > 0) {
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
                    continue;
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 2.0f);
            }
            if (GameEventCountdown.this.getDuration() == 0) {
                GameEventCountdown.this.getRunnable().run();
            }
            object = GameEventCountdown.this;
            int n = ((GameEventCountdown)object).getDuration();
            ((GameEventCountdown)object).setDuration(n + -1);
        }
    }
}

