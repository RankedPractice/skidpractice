/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.event.Event
 */
package cc.fyre.potpvp.arena.event;

import cc.fyre.potpvp.arena.Arena;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;

abstract class ArenaEvent
extends Event {
    private final Arena arena;

    ArenaEvent(Arena arena) {
        this.arena = (Arena)Preconditions.checkNotNull((Object)arena, (Object)"arena");
    }

    public Arena getArena() {
        return this.arena;
    }
}

