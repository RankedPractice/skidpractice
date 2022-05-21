/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 */
package cc.fyre.potpvp.rematch;

import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class RematchData {
    private final UUID sender;
    private final UUID target;
    private final KitType kitType;
    private final Instant expiresAt;
    private final String arenaName;

    RematchData(UUID sender, UUID target, KitType kitType, int durationSeconds, String arenaName) {
        this.sender = (UUID)Preconditions.checkNotNull((Object)sender, (Object)"sender");
        this.target = (UUID)Preconditions.checkNotNull((Object)target, (Object)"target");
        this.kitType = (KitType)Preconditions.checkNotNull((Object)kitType, (Object)"kitType");
        this.expiresAt = Instant.now().plusSeconds(durationSeconds);
        this.arenaName = (String)Preconditions.checkNotNull((Object)arenaName, (Object)"arenaName");
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public int getSecondsUntilExpiration() {
        return (int)ChronoUnit.SECONDS.between(Instant.now(), this.expiresAt);
    }

    public String toString() {
        return "RematchData(sender=" + this.getSender() + ", target=" + this.getTarget() + ", kitType=" + this.getKitType() + ", expiresAt=" + this.getExpiresAt() + ", arenaName=" + this.getArenaName() + ")";
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getTarget() {
        return this.target;
    }

    public KitType getKitType() {
        return this.kitType;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public String getArenaName() {
        return this.arenaName;
    }
}

