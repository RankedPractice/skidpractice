/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.setting.command;

import cc.fyre.potpvp.setting.menu.SettingsMenu;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class SettingsCommand {
    @Command(names={"settings", "preferences", "prefs", "options"}, permission="")
    public static void settings(Player sender) {
        new SettingsMenu().openMenu(sender);
    }
}

