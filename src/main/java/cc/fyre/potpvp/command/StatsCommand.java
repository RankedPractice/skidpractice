package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class StatsCommand {

    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();

    @Command(names = {"elo", "stats", "mypos", "myposition"}, permission = "", description = "Check your stats!")
    public static void elo(Player player, @Param(name = "target", defaultValue = "self") Player target) {
        if (player == target) {
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
            player.sendMessage(ChatColor.YELLOW + "Your elo!");
            for (KitType kitType : KitType.getAllTypes()) {
                if (kitType.isSupportsRanked()) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "• " + ChatColor.YELLOW + kitType.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + eloHandler.getElo(player, kitType));
                }
            }
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
//        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "• " + ChatColor.YELLOW + "Global" + ChatColor.GRAY + ": " + ChatColor.WHITE +  eloHandler.getGlobalElo(target.getUniqueId()));
//        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
        } else {
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
            player.sendMessage(ChatColor.YELLOW + target.getName() + "'s elo!");
            for (KitType kitType : KitType.getAllTypes()) {
                if (kitType.isSupportsRanked()) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "• " + ChatColor.YELLOW + kitType.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + eloHandler.getElo(player, kitType));
                }
            }
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
//        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "• " + ChatColor.YELLOW + "Global" + ChatColor.GRAY + ": " + ChatColor.WHITE +  eloHandler.getGlobalElo(target.getUniqueId()));
//        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.STRIKETHROUGH + "----------------");
        }
    }
}
