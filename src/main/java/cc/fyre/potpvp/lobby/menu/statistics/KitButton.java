/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.lobby.menu.statistics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

public class KitButton
extends Button {
    private static EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
    private KitType kitType;

    public KitButton(KitType kitType) {
        this.kitType = kitType;
    }

    public String getName(Player player) {
        return this.kitType.getColoredDisplayName() + ChatColor.GRAY.toString() + ChatColor.BOLD + " | " + ChatColor.WHITE + "Top 10";
    }

    public List<String> getDescription(Player player) {
        ArrayList description2 = Lists.newArrayList();
        description2.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        int counter = 1;
        for (Map.Entry<String, Integer> entry : eloHandler.topElo(this.kitType).entrySet()) {
            String color = (counter <= 3 ? ChatColor.DARK_PURPLE : ChatColor.GRAY).toString();
            description2.add(color + counter + ChatColor.GRAY.toString() + ChatColor.BOLD + " | " + entry.getKey() + ChatColor.GRAY + ": " + ChatColor.WHITE + entry.getValue());
            ++counter;
        }
        description2.add(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------");
        return description2;
    }

    public Material getMaterial(Player player) {
        return this.kitType.getIcon().getItemType();
    }

    public byte getDamageValue(Player player) {
        return this.kitType.getIcon().getData();
    }
}

