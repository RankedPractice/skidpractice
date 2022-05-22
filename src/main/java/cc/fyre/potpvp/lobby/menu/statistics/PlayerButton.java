package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.util.CC;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.menu.Button;

import java.util.List;

public class PlayerButton extends Button {

    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();

    @Override
    public String getName(Player player) {
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId());
        return profile.getCurrentGrant().getRank().getColor() + player.getName() + ChatColor.WHITE + ChatColor.BOLD + " ┃ "  + ChatColor.WHITE + "Elo Statistics";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        for (KitType kitType : KitType.getAllTypes()) {
            if (kitType.isSupportsRanked()) {
                description.add(CC.translate("&7┃ ") + ChatColor.WHITE + kitType.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.GREEN + eloHandler.getElo(player, kitType));
            }
        }

        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        description.add(CC.translate("&7┃ ") + ChatColor.YELLOW + "Global" + ChatColor.GRAY + ": " + ChatColor.GREEN + eloHandler.getGlobalElo(player.getUniqueId()));
        description.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) 3;
    }
}
