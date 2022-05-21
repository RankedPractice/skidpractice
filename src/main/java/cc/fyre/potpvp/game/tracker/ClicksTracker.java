/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.game.tracker;

import java.util.HashMap;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R8\u0010\u0003\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006`\u00078\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\b\u0010\u0002\u001a\u0004\b\t\u0010\nR8\u0010\u000b\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f0\u0004j\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f`\u00078\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\r\u0010\u0002\u001a\u0004\b\u000e\u0010\n\u00a8\u0006\u000f"}, d2={"Lcc/fyre/potpvp/game/tracker/ClicksTracker;", "", "()V", "lastClicks", "Ljava/util/HashMap;", "Ljava/util/UUID;", "", "Lkotlin/collections/HashMap;", "getLastClicks$annotations", "getLastClicks", "()Ljava/util/HashMap;", "lastUpdate", "", "getLastUpdate$annotations", "getLastUpdate", "potpvp-si"})
public final class ClicksTracker {
    @NotNull
    public static final ClicksTracker INSTANCE = new ClicksTracker();
    @NotNull
    private static final HashMap<UUID, Long> lastUpdate;
    @NotNull
    private static final HashMap<UUID, Integer> lastClicks;

    private ClicksTracker() {
    }

    @NotNull
    public static final HashMap<UUID, Long> getLastUpdate() {
        return lastUpdate;
    }

    @JvmStatic
    public static /* synthetic */ void getLastUpdate$annotations() {
    }

    @NotNull
    public static final HashMap<UUID, Integer> getLastClicks() {
        return lastClicks;
    }

    @JvmStatic
    public static /* synthetic */ void getLastClicks$annotations() {
    }

    static {
        boolean bl = false;
        lastUpdate = new HashMap();
        bl = false;
        lastClicks = new HashMap();
    }
}

