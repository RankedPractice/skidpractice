/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package cc.fyre.potpvp.duel;

import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class DuelInvite<T> {
    private final T sender;
    private final T target;
    private final KitType kitType;
    private final ArenaSchematic arenaSchematic;
    private final Instant timeSent;

    public DuelInvite(T sender, T target, KitType kitType, ArenaSchematic arenaSchematic) {
        this.sender = Preconditions.checkNotNull(sender, (Object)"sender");
        this.target = Preconditions.checkNotNull(target, (Object)"target");
        this.kitType = (KitType)Preconditions.checkNotNull((Object)kitType, (Object)"kitType");
        this.arenaSchematic = arenaSchematic;
        this.timeSent = Instant.now();
    }

    public boolean isExpired() {
        long sentAgo = ChronoUnit.SECONDS.between(this.timeSent, Instant.now());
        return sentAgo > 30L;
    }

    public T getSender() {
        return this.sender;
    }

    public T getTarget() {
        return this.target;
    }

    public KitType getKitType() {
        return this.kitType;
    }

    public ArenaSchematic getArenaSchematic() {
        return this.arenaSchematic;
    }

    public Instant getTimeSent() {
        return this.timeSent;
    }
}

