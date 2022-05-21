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

final class PostMatchFoodLevelButton
extends Button {
    private final int foodLevel;

    PostMatchFoodLevelButton(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + this.foodLevel + "/20 Hunger";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of();
    }

    public Material getMaterial(Player player) {
        return Material.COOKED_BEEF;
    }

    public int getAmount(Player player) {
        return this.foodLevel;
    }
}

