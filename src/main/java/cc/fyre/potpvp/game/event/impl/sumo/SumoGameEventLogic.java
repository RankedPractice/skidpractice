/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.game.event.impl.sumo;

import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEventLogic;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000f\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016\u00a2\u0006\u0002\u0010\u0007\u00a8\u0006\b"}, d2={"Lcc/fyre/potpvp/game/event/impl/sumo/SumoGameEventLogic;", "Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameEventLogic;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lcc/fyre/potpvp/game/Game;)V", "getRound", "", "()Ljava/lang/Integer;", "potpvp-si"})
public final class SumoGameEventLogic
extends BracketsGameEventLogic {
    public SumoGameEventLogic(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        super(game);
    }

    @Override
    @Nullable
    public Integer getRound() {
        return this.getRoundsPlayed();
    }
}

