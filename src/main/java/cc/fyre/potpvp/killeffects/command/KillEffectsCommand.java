/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.killeffects.command;

import cc.fyre.potpvp.killeffects.menu.KillEffectMenu;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public class KillEffectsCommand {
    @Command(names={"killeffects"}, permission="")
    public static void killeffects(Player sender) {
        new KillEffectMenu().openMenu(sender);
    }
}

