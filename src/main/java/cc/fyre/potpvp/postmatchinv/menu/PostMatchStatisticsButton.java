/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.postmatchinv.menu;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

final class PostMatchStatisticsButton
extends Button {
    private final int totalHits;
    private final int longestCombo;

    PostMatchStatisticsButton(int totalHits, int longestCombo) {
        this.totalHits = totalHits;
        this.longestCombo = longestCombo;
    }

    public String getName(Player player) {
        return ChatColor.GREEN + "Statistics";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((ChatColor.AQUA + "Longest Combo" + ChatColor.GRAY.toString() + " - " + this.longestCombo + " Hit" + (this.longestCombo == 1 ? "" : "s")), (ChatColor.AQUA + "Total Hits" + ChatColor.GRAY.toString() + " - " + this.totalHits + " Hit" + (this.totalHits == 1 ? "" : "s")));
    }

    public Material getMaterial(Player player) {
        return Material.DIAMOND_SWORD;
    }

    public int getAmount(Player player) {
        return 1;
    }
}

