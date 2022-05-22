package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

import java.util.List;
import java.util.Map.Entry;

public class GlobalEloButton extends Button {

    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();

    @Override
    public String getName(Player player) {
        return ChatColor.GREEN + "Global" + ChatColor.GRAY + ChatColor.BOLD + " ┃ " + ChatColor.WHITE + "Top 10";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        int counter = 1;

        for (Entry<String, Integer> entry : eloHandler.topElo(null).entrySet()) {
            String color = (ChatColor.WHITE).toString();
            description.add(color + counter + ChatColor.WHITE + ChatColor.BOLD + " ┃ " + entry.getKey() + ChatColor.GRAY + ": " + ChatColor.YELLOW + entry.getValue());

            counter++;
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.NETHER_STAR;
    }
}
