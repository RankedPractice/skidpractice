/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.arena.menu.select;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.menu.select.ArenaButton;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.MatchHandler;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public class SelectArenaMenu
extends Menu {
    private KitType kitType;
    private Callback<ArenaSchematic> selectionCallback;
    private final String title;

    public SelectArenaMenu(KitType kitType, Callback<ArenaSchematic> selectionCallback, String title) {
        this.title = title;
        this.kitType = kitType;
        this.selectionCallback = selectionCallback;
    }

    public String getTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + this.title;
    }

    @NotNull
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        HashMap buttons = Maps.newHashMap();
        int i = 0;
        for (ArenaSchematic schematic : PotPvP.getInstance().getArenaHandler().getSchematics()) {
            if (!MatchHandler.canUseSchematic(this.kitType, schematic)) continue;
            buttons.put(i++, new ArenaButton(schematic, this.selectionCallback));
        }
        return buttons;
    }
}

