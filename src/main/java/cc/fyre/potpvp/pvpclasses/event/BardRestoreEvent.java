/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package cc.fyre.potpvp.pvpclasses.event;

import cc.fyre.potpvp.pvpclasses.PvPClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BardRestoreEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private PvPClass.SavedPotion potions;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public BardRestoreEvent(Player player, PvPClass.SavedPotion potions) {
        this.player = player;
        this.potions = potions;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PvPClass.SavedPotion getPotions() {
        return this.potions;
    }
}

