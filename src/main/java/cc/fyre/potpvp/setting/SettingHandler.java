/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.setting;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.event.SettingUpdateEvent;
import cc.fyre.potpvp.setting.listener.SettingLoadListener;
import cc.fyre.potpvp.setting.repository.MongoSettingRepository;
import cc.fyre.potpvp.setting.repository.SettingRepository;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class SettingHandler {
    private final Map<UUID, Map<Setting, Boolean>> settingsData = new ConcurrentHashMap<UUID, Map<Setting, Boolean>>();
    private final SettingRepository settingRepository;

    public SettingHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new SettingLoadListener(), (Plugin)PotPvP.getInstance());
        this.settingRepository = new MongoSettingRepository();
    }

    public boolean getSetting(Player player, Setting setting) {
        Map<Setting, Boolean> playerSettings = this.settingsData.getOrDefault(player.getUniqueId(), ImmutableMap.of());
        return playerSettings.getOrDefault((Object)setting, setting.getDefaultValue());
    }

    public void updateSetting(Player player, Setting setting, boolean enabled) {
        Map playerSettings = this.settingsData.computeIfAbsent(player.getUniqueId(), i -> new HashMap());
        playerSettings.put(setting, enabled);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
            try {
                this.settingRepository.saveSettings(player.getUniqueId(), playerSettings);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Bukkit.getPluginManager().callEvent((Event)new SettingUpdateEvent(player, setting, enabled));
    }

    public void loadSettings(UUID playerUuid) {
        ConcurrentHashMap<Setting, Boolean> playerSettings;
        try {
            playerSettings = new ConcurrentHashMap<Setting, Boolean>(this.settingRepository.loadSettings(playerUuid));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            playerSettings = new ConcurrentHashMap();
        }
        this.settingsData.put(playerUuid, playerSettings);
    }

    public void unloadSettings(Player player) {
        this.settingsData.remove(player.getUniqueId());
    }
}

