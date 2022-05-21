/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.leaderboard;

import java.util.UUID;

public class LeaderboardEntry {
    private int position;
    private UUID uuid;
    private String displayName;
    private int value;

    public LeaderboardEntry(int position, UUID uuid, String displayName, int value) {
        this.position = position;
        this.uuid = uuid;
        this.displayName = displayName;
        this.value = value;
    }

    public int getPosition() {
        return this.position;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getValue() {
        return this.value;
    }
}

