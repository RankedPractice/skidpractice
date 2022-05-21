/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package cc.fyre.potpvp.party;

import cc.fyre.potpvp.party.Party;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.util.UUID;

public final class PartyInvite {
    private Party party;
    private UUID target;
    private Instant timeSent;

    PartyInvite(Party party, UUID target) {
        this.party = (Party)Preconditions.checkNotNull((Object)party, (Object)"party");
        this.target = (UUID)Preconditions.checkNotNull((Object)target, (Object)"target");
        this.timeSent = Instant.now();
    }

    public Party getParty() {
        return this.party;
    }

    public UUID getTarget() {
        return this.target;
    }

    public Instant getTimeSent() {
        return this.timeSent;
    }
}

