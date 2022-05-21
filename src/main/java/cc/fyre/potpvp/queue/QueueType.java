/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.queue;

public enum QueueType {
    UNRANKED("Unranked"),
    RANKED("Ranked"),
    PREMIUM("Premium");

    private final String name;

    public boolean isRanked() {
        return this == RANKED;
    }

    public boolean isUnranked() {
        return this == UNRANKED;
    }

    public boolean isPremium() {
        return this == PREMIUM;
    }

    public boolean isAllowRematches() {
        return this == UNRANKED;
    }

    private QueueType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

