/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.bridge.BridgeGlobal
 *  rip.bridge.bridge.global.profile.Profile
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.menu.Button;

public class PlayerButton
extends Button {
    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();

    public String getName(Player player) {
        return this.getColoredName(player) + ChatColor.WHITE + ChatColor.BOLD + " | " + ChatColor.WHITE + "Statistics";
    }

    public List<String> getDescription(Player player) {
        ArrayList description2 = Lists.newArrayList();
        description2.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        for (KitType kitType : KitType.getAllTypes()) {
            if (!kitType.isSupportsRanked()) continue;
            description2.add(ChatColor.DARK_PURPLE + kitType.getDisplayName() + ChatColor.GRAY + ": " + eloHandler.getElo(player, kitType));
        }
        description2.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        description2.add(ChatColor.DARK_PURPLE + "Global" + ChatColor.GRAY + ": " + eloHandler.getGlobalElo(player.getUniqueId()));
        description2.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        return description2;
    }

    public Material getMaterial(Player player) {
        return Material.SKULL_ITEM;
    }

    public byte getDamageValue(Player player) {
        return 3;
    }

    private String getColoredName(Player player) {
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(player.getUniqueId());
        if (profile != null) {
            return profile.getCurrentGrant().getRank().getColor() + player.getName();
        }
        return player.getName();
    }
}

