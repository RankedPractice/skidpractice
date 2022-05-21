/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.leaderboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class LeaderboardCommand {
    @Command(names={"lb refresh"}, permission="op", async=true)
    public static void refresh(CommandSender sender) {
        PotPvP.getInstance().getLeaderboardHandler().loadLeaderboards();
        PotPvP.getInstance().getServer().getScheduler().runTask((Plugin)PotPvP.getInstance(), () -> PotPvP.getInstance().getLeaderboardHandler().loadHolograms());
    }

    @Command(names={"lb setloc"}, permission="op", async=true)
    public static void setLocation(Player player, @Param(name="id") String id) {
        if (id.equals("GLOBAL") || KitType.byId(id) != null) {
            PotPvP.getInstance().getLeaderboardHandler().getHologramLocations().put(id, player.getEyeLocation());
            PotPvP.getInstance().getLeaderboardHandler().loadHolograms();
            PotPvP.getInstance().getLeaderboardHandler().writeFile();
            player.sendMessage(ChatColor.GOLD + "Updated leaderboard location!");
        }
    }
}

