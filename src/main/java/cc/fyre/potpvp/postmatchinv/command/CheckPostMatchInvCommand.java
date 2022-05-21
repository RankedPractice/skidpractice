/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.postmatchinv.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.postmatchinv.PostMatchInvHandler;
import cc.fyre.potpvp.postmatchinv.PostMatchPlayer;
import cc.fyre.potpvp.postmatchinv.menu.PostMatchMenu;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class CheckPostMatchInvCommand {
    @Command(names={"_"}, permission="")
    public static void checkPostMatchInv(Player sender, @Param(name="target") UUID target) {
        PostMatchInvHandler postMatchInvHandler = PotPvP.getInstance().getPostMatchInvHandler();
        Map<UUID, PostMatchPlayer> players = postMatchInvHandler.getPostMatchData(sender.getUniqueId());
        if (players.containsKey(target)) {
            new PostMatchMenu(players.get(target)).openMenu(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Data for " + FrozenUUIDCache.name((UUID)target) + " not found.");
        }
    }
}

