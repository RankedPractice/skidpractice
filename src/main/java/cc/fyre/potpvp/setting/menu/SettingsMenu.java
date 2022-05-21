/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.setting.menu;

import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.menu.SettingButton;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class SettingsMenu
extends Menu {
    public SettingsMenu() {
        super("Edit settings");
        this.setAutoUpdate(true);
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int index = 0;
        for (Setting setting : Setting.values()) {
            if (!setting.canUpdate(player)) continue;
            buttons.put(index++, new SettingButton(setting));
        }
        return buttons;
    }
}

