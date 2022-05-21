/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.queue.MatchQueue;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

public abstract class MatchQueueEntry {
    private final MatchQueue queue;
    private final Instant timeJoined;

    MatchQueueEntry(MatchQueue queue) {
        this.queue = queue;
        this.timeJoined = Instant.now();
    }

    public abstract Set<UUID> getMembers();

    public int getWaitSeconds() {
        return (int)ChronoUnit.SECONDS.between(this.timeJoined, Instant.now());
    }

    public MatchQueue getQueue() {
        return this.queue;
    }

    public Instant getTimeJoined() {
        return this.timeJoined;
    }
}

