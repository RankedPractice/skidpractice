package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class EloResetCommand {

    @Command(names = "eloreset", permission = "practice.admin", description = "Manually reset the player's elo")
    public static void eloreset(Player sender, @Param(name="target") OfflinePlayer target) {
        PotPvP.getInstance().getEloHandler().resetElo(target.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "Resetting elo of " + target.getName() + ChatColor.GREEN + ".");
    }
}
