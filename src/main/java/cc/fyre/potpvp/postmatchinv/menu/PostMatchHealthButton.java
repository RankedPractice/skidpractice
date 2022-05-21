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

final class PostMatchHealthButton
extends Button {
    private final double health;

    PostMatchHealthButton(double health) {
        this.health = health;
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + this.health + "/10  \u2764";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of();
    }

    public Material getMaterial(Player player) {
        return Material.SPECKLED_MELON;
    }

    public int getAmount(Player player) {
        return (int)Math.ceil(this.health);
    }
}

