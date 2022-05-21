/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.arena.menu.manageschematics;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.menu.manageschematics.ManageSchematicButton;
import cc.fyre.potpvp.command.ManageCommand;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class ManageSchematicsMenu
extends Menu {
    public ManageSchematicsMenu() {
        super("Manage schematics");
        this.setAutoUpdate(true);
    }

    public Map<Integer, Button> getButtons(Player player) {
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int index = 0;
        buttons.put(index++, new MenuBackButton(p -> new ManageCommand.ManageMenu().openMenu((Player)p)));
        for (ArenaSchematic schematic : arenaHandler.getSchematics()) {
            buttons.put(index++, new ManageSchematicButton(schematic));
        }
        return buttons;
    }
}

