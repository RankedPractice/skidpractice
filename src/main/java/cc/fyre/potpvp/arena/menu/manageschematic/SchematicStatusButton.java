/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.arena.menu.manageschematic;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;

final class SchematicStatusButton
extends Button {
    private final ArenaSchematic schematic;

    SchematicStatusButton(ArenaSchematic schematic) {
        this.schematic = (ArenaSchematic)Preconditions.checkNotNull((Object)schematic, (Object)"schematic");
    }

    public String getName(Player player) {
        return ChatColor.YELLOW + this.schematic.getName() + " Status";
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
        description2.add(ChatColor.GREEN + "Copies: " + ChatColor.WHITE + totalCopies);
        description2.add(ChatColor.GREEN + "Copies in use: " + ChatColor.WHITE + inUseCopies);
        return description2;
    }

    public int getAmount(Player player) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        return arenaHandler.getArenas(this.schematic).size();
    }

    public Material getMaterial(Player player) {
        return Material.NAME_TAG;
    }
}

