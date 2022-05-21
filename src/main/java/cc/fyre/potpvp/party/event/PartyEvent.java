/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.event.Event
 */
package cc.fyre.potpvp.party.event;

import cc.fyre.potpvp.party.Party;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;

abstract class PartyEvent
extends Event {
    private final Party party;

    PartyEvent(Party party) {
        this.party = (Party)Preconditions.checkNotNull((Object)party, (Object)"party");
    }

    public Party getParty() {
        return this.party;
    }
}

