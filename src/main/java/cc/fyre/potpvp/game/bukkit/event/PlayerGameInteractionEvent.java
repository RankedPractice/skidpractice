/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.game.bukkit.event;

import cc.fyre.potpvp.game.Game;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000e"}, d2={"Lcc/fyre/potpvp/game/bukkit/event/PlayerGameInteractionEvent;", "Lorg/bukkit/event/Event;", "player", "Lorg/bukkit/entity/Player;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lorg/bukkit/entity/Player;Lcc/fyre/potpvp/game/Game;)V", "getGame", "()Lcc/fyre/potpvp/game/Game;", "getPlayer", "()Lorg/bukkit/entity/Player;", "getHandlers", "Lorg/bukkit/event/HandlerList;", "Companion", "potpvp-si"})
public final class PlayerGameInteractionEvent
extends Event {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Player player;
    @NotNull
    private final Game game;
    @NotNull
    private static final HandlerList handlerList = new HandlerList();

    public PlayerGameInteractionEvent(@NotNull Player player, @NotNull Game game) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(game, "game");
        this.player = player;
        this.game = game;
    }

    @NotNull
    public final Player getPlayer() {
        return this.player;
    }

    @NotNull
    public final Game getGame() {
        return this.game;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }

    @NotNull
    public static final HandlerList getHandlerList() {
        return Companion.getHandlerList();
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2={"Lcc/fyre/potpvp/game/bukkit/event/PlayerGameInteractionEvent$Companion;", "", "()V", "handlerList", "Lorg/bukkit/event/HandlerList;", "getHandlerList$annotations", "getHandlerList", "()Lorg/bukkit/event/HandlerList;", "potpvp-si"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final HandlerList getHandlerList() {
            return handlerList;
        }

        @JvmStatic
        public static /* synthetic */ void getHandlerList$annotations() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

