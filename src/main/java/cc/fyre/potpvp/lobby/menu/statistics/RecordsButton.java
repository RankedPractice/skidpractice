package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.profile.Profile;
import cc.fyre.potpvp.util.CC;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

import java.util.List;

public class RecordsButton extends Button {

    @Override
    public String getName(Player player) {
        return player.getDisplayName() + ChatColor.GRAY + " ┃ "  + ChatColor.WHITE + "Records";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        Profile p = PotPvP.getInstance().getProfileManager().getProfile(player);

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        description.add(CC.translate("&7┃ &fGames Played&7: &e" + p.getGamesPlayed()));
        description.add(CC.translate("&7┃ &fWins&7: &e" + p.getGamesWon()));
        description.add(CC.translate("&7┃ &fLoses&7: &e" + p.getLoses()));
        description.add(CC.translate("&7┃ &fKills&7: &e" + p.getKills()));
        description.add(CC.translate("&7┃ &fDeaths&7: &e" + p.getDeaths()));
        description.add(CC.translate("&7┃ &fLongest Combo&7: &e" + p.getHighestCombo()));
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BOOK;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 0;
    }
}
