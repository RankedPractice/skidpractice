/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.arena.menu.manageschematics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.menu.manageschematic.ManageSchematicMenu;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class ManageSchematicButton
extends Button {
    private final ArenaSchematic schematic;

    ManageSchematicButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull((Object)schematic, (Object)"schematic");
    }

    public String getName(Player player) {
        return ChatColor.YELLOW + "Manage " + this.schematic.getName();
    }

    public List<String> getDescription(Player player) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        int totalCopies = 0;
        int inUseCopies = 0;
        for (Arena arena : arenaHandler.getArenas(this.schematic)) {
            ++totalCopies;
            if (!arena.isInUse()) continue;
            ++inUseCopies;
        }
        ArrayList<String> description2 = new ArrayList<String>();
        description2.add("");
        description2.add(ChatColor.GREEN + "Enabled: " + ChatColor.WHITE + (this.schematic.isEnabled() ? "Yes" : "No"));
        description2.add(ChatColor.GREEN + "Copies: " + ChatColor.WHITE + totalCopies);
        description2.add(ChatColor.GREEN + "Copies in use: " + ChatColor.WHITE + inUseCopies);
        return description2;
    }

    public int getAmount(Player player) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        return arenaHandler.getArenas(this.schematic).size();
    }

    public Material getMaterial(Player player) {
        return this.schematic.isEnabled() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        new ManageSchematicMenu(this.schematic).openMenu(player);
    }
}

