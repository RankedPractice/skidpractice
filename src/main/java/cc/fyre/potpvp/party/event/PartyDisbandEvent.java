/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.party.event;

import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.event.PartyEvent;
import org.bukkit.event.HandlerList;

public final class PartyDisbandEvent
extends PartyEvent {
    private static HandlerList handlerList = new HandlerList();

    public PartyDisbandEvent(Party party) {
        super(party);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

