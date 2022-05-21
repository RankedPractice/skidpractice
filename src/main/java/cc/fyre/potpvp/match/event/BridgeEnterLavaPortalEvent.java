/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.match.event;

import cc.fyre.potpvp.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BridgeEnterLavaPortalEvent
extends Event {
    private static HandlerList handlers = new HandlerList();
    private final Player player;
    private final Match match;

    public BridgeEnterLavaPortalEvent(Player player, Match match) {
        this.player = player;
        this.match = match;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Match getMatch() {
        return this.match;
    }
}

