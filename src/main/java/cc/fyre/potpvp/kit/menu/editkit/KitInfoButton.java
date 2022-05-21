/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.kit.Kit;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

final class KitInfoButton
extends Button {
    private final Kit kit;

    KitInfoButton(Kit kit) {
        this.kit = (Kit)Preconditions.checkNotNull((Object)kit, (Object)"kit");
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Editing: " + ChatColor.AQUA + this.kit.getName();
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((Object)(ChatColor.GRAY + "You are editing '" + this.kit.getName() + "'"));
    }

    public Material getMaterial(Player player) {
        return Material.NAME_TAG;
    }
}

