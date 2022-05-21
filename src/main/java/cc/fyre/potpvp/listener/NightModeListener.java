/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package cc.fyre.potpvp.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.setting.event.SettingUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class NightModeListener
implements Listener {
    public static final int NIGHT_TIME = 18000;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        if (settingHandler.getSetting(event.getPlayer(), Setting.NIGHT_MODE)) {
            event.getPlayer().setPlayerTime(18000L, false);
        }
    }

    @EventHandler
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() != Setting.NIGHT_MODE) {
            return;
        }
        if (event.isEnabled()) {
            event.getPlayer().setPlayerTime(18000L, false);
        } else {
            event.getPlayer().resetPlayerTime();
        }
    }
}

