/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.arena.menu.manageschematic;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.event.GameEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class ManageEventsMenu
extends Menu {
    private final ArenaSchematic schematic;

    public ManageEventsMenu(ArenaSchematic schematic) {
        this.setAutoUpdate(true);
        this.schematic = schematic;
    }

    public String getTitle(Player player) {
        return "Manage " + this.schematic.getName() + "Events";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (final GameEvent event : GameEvent.getEvents()) {
            buttons.put(buttons.size(), new Button(){

                public String getName(Player player) {
                    if (ManageEventsMenu.this.schematic.getEvent() == event) {
                        return ChatColor.GREEN + event.getName();
                    }
                    return ChatColor.RED + event.getName();
                }

                public List<String> getDescription(Player player) {
                    ArrayList<String> toReturn = new ArrayList<String>();
                    if (ManageEventsMenu.this.schematic.getEvent() != event) {
                        toReturn.add(ChatColor.GRAY + "Click to lock this arena to the " + event.getName() + " event.");
                    } else {
                        toReturn.add(ChatColor.GRAY + "Click to unbind this arena from the " + event.getName() + " event.");
                    }
                    return toReturn;
                }

                public Material getMaterial(Player player) {
                    return event.getIcon().getType();
                }

                public void clicked(Player player, int slot, ClickType clickType) {
                    if (ManageEventsMenu.this.schematic.getEvent() != event) {
                        ManageEventsMenu.this.schematic.setArcherOnly(false);
                        ManageEventsMenu.this.schematic.setBuildUHCOnly(false);
                        ManageEventsMenu.this.schematic.setHCFOnly(false);
                        ManageEventsMenu.this.schematic.setSpleefOnly(false);
                        ManageEventsMenu.this.schematic.setSupportsRanked(false);
                        ManageEventsMenu.this.schematic.setEventName(event.getName());
                    } else {
                        ManageEventsMenu.this.schematic.setEventName(null);
                    }
                    try {
                        PotPvP.getInstance().getArenaHandler().saveSchematics();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return buttons;
    }
}

