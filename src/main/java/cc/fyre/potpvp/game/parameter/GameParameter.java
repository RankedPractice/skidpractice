/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.game.parameter;

import cc.fyre.potpvp.game.parameter.GameParameterOption;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u000e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H&\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/parameter/GameParameter;", "", "getDisplayName", "", "getOptions", "", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "potpvp-si"})
public interface GameParameter {
    @NotNull
    public String getDisplayName();

    @NotNull
    public List<GameParameterOption> getOptions();
}

