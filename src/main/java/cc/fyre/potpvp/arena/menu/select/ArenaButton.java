/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.arena.menu.select;

import cc.fyre.potpvp.arena.ArenaSchematic;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.util.Callback;

public class ArenaButton
extends Button {
    private ArenaSchematic schematic;
    private Callback<ArenaSchematic> callback;

    @NotNull
    public String getName(@NotNull Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + this.schematic.getName();
    }

    @NotNull
    public List<String> getDescription(@NotNull Player player) {
        return Collections.singletonList(ChatColor.GREEN + "Click here to select " + ChatColor.LIGHT_PURPLE);
    }

    @NotNull
    public Material getMaterial(@NotNull Player player) {
        return Material.WOOL;
    }

    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        player.closeInventory();
        this.callback.callback(this.schematic);
    }

    public ArenaButton(ArenaSchematic schematic, Callback<ArenaSchematic> callback) {
        this.schematic = schematic;
        this.callback = callback;
    }
}

