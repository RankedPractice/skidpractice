/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableSet
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.queue.MatchQueue;
import cc.fyre.potpvp.queue.MatchQueueEntry;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.UUID;

public final class SoloMatchQueueEntry
extends MatchQueueEntry {
    private final UUID player;

    SoloMatchQueueEntry(MatchQueue queue, UUID player) {
        super(queue);
        this.player = (UUID)Preconditions.checkNotNull((Object)player, (Object)"player");
    }

    @Override
    public Set<UUID> getMembers() {
        return ImmutableSet.of((Object)this.player);
    }

    public UUID getPlayer() {
        return this.player;
    }
}

