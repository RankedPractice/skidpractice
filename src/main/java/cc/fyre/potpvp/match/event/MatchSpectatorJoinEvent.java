/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.match.event;

import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.event.MatchEvent;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public final class MatchSpectatorJoinEvent
extends MatchEvent {
    private static HandlerList handlerList = new HandlerList();
    private final Player spectator;

    public MatchSpectatorJoinEvent(Player spectator, Match match) {
        super(match);
        this.spectator = (Player)Preconditions.checkNotNull((Object)spectator, (Object)"spectator");
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getSpectator() {
        return this.spectator;
    }
}

