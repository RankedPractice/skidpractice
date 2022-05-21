/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.event.Event
 */
package cc.fyre.potpvp.match.event;

import cc.fyre.potpvp.match.Match;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;

abstract class MatchEvent
extends Event {
    private final Match match;

    MatchEvent(Match match) {
        this.match = (Match)Preconditions.checkNotNull((Object)match, (Object)"match");
    }

    public Match getMatch() {
        return this.match;
    }
}

