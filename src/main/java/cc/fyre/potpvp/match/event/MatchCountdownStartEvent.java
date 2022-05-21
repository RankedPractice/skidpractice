/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.match.event;

import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.event.MatchEvent;
import org.bukkit.event.HandlerList;

public final class MatchCountdownStartEvent
extends MatchEvent {
    private static HandlerList handlerList = new HandlerList();

    public MatchCountdownStartEvent(Match match) {
        super(match);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

