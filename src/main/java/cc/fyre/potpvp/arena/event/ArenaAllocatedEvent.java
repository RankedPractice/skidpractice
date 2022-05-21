/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.arena.event;

import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.event.ArenaEvent;
import org.bukkit.event.HandlerList;

public final class ArenaAllocatedEvent
extends ArenaEvent {
    private static HandlerList handlerList = new HandlerList();

    public ArenaAllocatedEvent(Arena arena) {
        super(arena);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

