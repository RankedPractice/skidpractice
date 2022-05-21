/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.game;

import kotlin.Metadata;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lcc/fyre/potpvp/game/GameState;", "", "(Ljava/lang/String;I)V", "STARTING", "RUNNING", "ENDED", "potpvp-si"})
public final class GameState
extends Enum<GameState> {
    public static final /* enum */ GameState STARTING = new GameState();
    public static final /* enum */ GameState RUNNING = new GameState();
    public static final /* enum */ GameState ENDED = new GameState();
    private static final /* synthetic */ GameState[] $VALUES;

    public static GameState[] values() {
        return (GameState[])$VALUES.clone();
    }

    public static GameState valueOf(String value) {
        return Enum.valueOf(GameState.class, value);
    }

    static {
        $VALUES = gameStateArray = new GameState[]{GameState.STARTING, GameState.RUNNING, GameState.ENDED};
    }
}

