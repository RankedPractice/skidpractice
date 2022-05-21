/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 */
package cc.fyre.potpvp.setting.event;

import cc.fyre.potpvp.setting.Setting;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public final class SettingUpdateEvent
extends PlayerEvent {
    private static HandlerList handlerList = new HandlerList();
    private final Setting setting;
    private final boolean enabled;

    public SettingUpdateEvent(Player player, Setting setting, boolean enabled) {
        super(player);
        this.setting = (Setting)((Object)Preconditions.checkNotNull((Object)((Object)setting), (Object)"setting"));
        this.enabled = enabled;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

