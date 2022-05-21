/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.setting.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public class ToggleSpectatorsCommand {
    @Command(names={"tobogganers", "togglespecs"}, permission="", description="Allow/deny players from spectating your matches")
    public static void execute(Player player) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        boolean toggle = !settingHandler.getSetting(player, Setting.ALLOW_SPECTATORS);
        settingHandler.updateSetting(player, Setting.ALLOW_SPECTATORS, toggle);
        if (toggle) {
            player.sendMessage(ChatColor.GREEN + "Toggled spectators on.");
        } else {
            player.sendMessage(ChatColor.RED + "Toggled spectators off.");
        }
    }
}

