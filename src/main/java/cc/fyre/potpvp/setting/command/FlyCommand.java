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

public final class FlyCommand {
    @Command(names={"fly"}, permission="")
    public static void fly(Player sender) {
        if (!Setting.LOBBY_FLY.canUpdate(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(sender, Setting.LOBBY_FLY);
        settingHandler.updateSetting(sender, Setting.LOBBY_FLY, enabled);
        if (enabled) {
            sender.sendMessage(ChatColor.GREEN + "Flight enabled.");
        } else {
            sender.sendMessage(ChatColor.RED + "Flight disabled.");
        }
        if (PotPvP.getInstance().getLobbyHandler().isInLobby(sender)) {
            sender.setAllowFlight(enabled);
            sender.setFlying(enabled);
        }
    }
}

