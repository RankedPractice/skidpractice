package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.util.CC;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

import java.util.List;
import java.util.Map.Entry;

public class KitButton extends Button {

    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();

    private KitType kitType;

    public KitButton(KitType kitType) {
        this.kitType = kitType;
    }

    @Override
    public String getName(Player player) {
        return kitType.getColoredDisplayName() + ChatColor.GRAY.toString() + ChatColor.BOLD + " ┃ " + ChatColor.WHITE + "Top 10";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        int counter = 1;

        for (Entry<String, Integer> entry : eloHandler.topElo(kitType).entrySet()) {
            String color = (ChatColor.WHITE).toString();

            description.add(CC.translate("&7" + counter + ") &f" + entry.getKey() + "&7: &a" + entry.getValue()));

            counter++;
        }



        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return kitType.getIcon().getItemType();
    }

    @Override
    public byte getDamageValue(Player player) {
        return kitType.getIcon().getData();
    }
}
