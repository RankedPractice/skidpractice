/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.setting.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class SettingButton
extends Button {
    private static final String ENABLED_ARROW = ChatColor.BLUE.toString() + ChatColor.BOLD + "  \u25ba ";
    private static final String DISABLED_SPACER = "    ";
    private final Setting setting;

    SettingButton(Setting setting) {
        this.setting = (Setting)((Object)Preconditions.checkNotNull((Object)((Object)setting), (Object)"setting"));
    }

    public String getName(Player player) {
        return this.setting.getName();
    }

    public List<String> getDescription(Player player) {
        ArrayList<String> description2 = new ArrayList<String>();
        description2.add("");
        description2.addAll(this.setting.getDescription());
        description2.add("");
        if (PotPvP.getInstance().getSettingHandler().getSetting(player, this.setting)) {
            description2.add(ENABLED_ARROW + this.setting.getEnabledText());
            description2.add(DISABLED_SPACER + this.setting.getDisabledText());
        } else {
            description2.add(DISABLED_SPACER + this.setting.getEnabledText());
            description2.add(ENABLED_ARROW + this.setting.getDisabledText());
        }
        return description2;
    }

    public Material getMaterial(Player player) {
        return this.setting.getIcon();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        if (!this.setting.canUpdate(player)) {
            return;
        }
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(player, this.setting);
        settingHandler.updateSetting(player, this.setting, enabled);
        if (this.setting == Setting.LOBBY_FLY && PotPvP.getInstance().getLobbyHandler().isInLobby(player)) {
            player.setAllowFlight(enabled);
            player.setFlying(enabled);
        }
    }
}

