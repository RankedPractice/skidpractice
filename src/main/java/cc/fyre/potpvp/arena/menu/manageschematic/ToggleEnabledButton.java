/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.arena.menu.manageschematic;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class ToggleEnabledButton
extends Button {
    private final ArenaSchematic schematic;

    ToggleEnabledButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull(schematic, "schematic");
    }

    public String getName(Player player) {
        if (this.schematic.isEnabled()) {
            return ChatColor.RED + "Disable " + this.schematic.getName();
        }
        return ChatColor.GREEN + "Enable " + this.schematic.getName();
    }

    public List<String> getDescription(Player player) {
        if (this.schematic.isEnabled()) {
            return ImmutableList.of("", (ChatColor.YELLOW + "Click to disable " + this.schematic.getName() + ", which will prevent matches"), (ChatColor.YELLOW + "being scheduled on these arenas. Admin"), (ChatColor.YELLOW + "commands will not be impacted."));
        }
        return ImmutableList.of("", (ChatColor.YELLOW + "Click to enable " + this.schematic.getName() + ", which will allow matches"), (ChatColor.YELLOW + "to be scheduled on these arenas."));
    }

    public Material getMaterial(Player player) {
        return this.schematic.isEnabled() ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        this.schematic.setEnabled(!this.schematic.isEnabled());
        try {
            PotPvP.getInstance().getArenaHandler().saveSchematics();
        }
        catch (IOException ex) {
            player.sendMessage(ChatColor.RED + "Failed to save " + this.schematic.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

