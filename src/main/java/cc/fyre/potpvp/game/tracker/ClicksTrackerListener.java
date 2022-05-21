/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.game.tracker;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/tracker/ClicksTrackerListener;", "Lorg/bukkit/event/Listener;", "()V", "onPlayerQuit", "", "event", "Lorg/bukkit/event/player/PlayerQuitEvent;", "potpvp-si"})
public final class ClicksTrackerListener
implements Listener {
    @EventHandler
    public final void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }
}

