/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.party.event;

import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.event.PartyEvent;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public final class PartyMemberJoinEvent
extends PartyEvent {
    private static HandlerList handlerList = new HandlerList();
    private final Player member;

    public PartyMemberJoinEvent(Player member, Party party) {
        super(party);
        this.member = (Player)Preconditions.checkNotNull((Object)member, (Object)"member");
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getMember() {
        return this.member;
    }
}

