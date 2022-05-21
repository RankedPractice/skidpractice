/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package cc.fyre.potpvp.listener;

import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.event.SettingUpdateEvent;
import cc.fyre.potpvp.util.VisibilityUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ToggleVisibilityListener
implements Listener {
    @EventHandler
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() == Setting.SHOW_PLAYERS_IN_LOBBY) {
            VisibilityUtils.updateVisibility(event.getPlayer());
        }
    }
}

