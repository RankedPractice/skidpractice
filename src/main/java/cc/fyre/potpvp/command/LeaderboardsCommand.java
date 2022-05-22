package cc.fyre.potpvp.command;

import cc.fyre.potpvp.lobby.menu.StatisticsMenu;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public class LeaderboardsCommand {

    @Command(names={"leaderboards", "leaderboard", "lb", "lbs"}, permission = "")
    public static void manage(Player sender) {
        new StatisticsMenu().openMenu(sender);
    }
}
