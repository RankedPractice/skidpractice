/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.queue.MatchQueue;
import cc.fyre.potpvp.queue.MatchQueueEntry;
import com.google.common.base.Preconditions;
import java.util.Set;
import java.util.UUID;

public final class PartyMatchQueueEntry
extends MatchQueueEntry {
    private final Party party;

    PartyMatchQueueEntry(MatchQueue queue, Party party) {
        super(queue);
        this.party = (Party)Preconditions.checkNotNull((Object)party, (Object)"party");
    }

    @Override
    public Set<UUID> getMembers() {
        return this.party.getMembers();
    }

    public Party getParty() {
        return this.party;
    }
}

