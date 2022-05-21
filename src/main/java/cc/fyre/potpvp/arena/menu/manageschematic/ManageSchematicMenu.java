/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.arena.menu.manageschematic;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.menu.manageschematic.CreateCopiesButton;
import cc.fyre.potpvp.arena.menu.manageschematic.ManageEventsMenu;
import cc.fyre.potpvp.arena.menu.manageschematic.RemoveCopiesButton;
import cc.fyre.potpvp.arena.menu.manageschematic.SaveModelButton;
import cc.fyre.potpvp.arena.menu.manageschematic.SchematicStatusButton;
import cc.fyre.potpvp.arena.menu.manageschematic.TeleportToModelButton;
import cc.fyre.potpvp.arena.menu.manageschematic.ToggleEnabledButton;
import cc.fyre.potpvp.arena.menu.manageschematics.ManageSchematicsMenu;
import cc.fyre.potpvp.util.menu.BooleanTraitButton;
import cc.fyre.potpvp.util.menu.IntegerTraitButton;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class ManageSchematicMenu
extends Menu {
    private final ArenaSchematic schematic;

    public ManageSchematicMenu(ArenaSchematic schematic) {
        super("Manage " + schematic.getName());
        this.setAutoUpdate(true);
        this.schematic = schematic;
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(0, new SchematicStatusButton(this.schematic));
        buttons.put(1, new ToggleEnabledButton(this.schematic));
        buttons.put(3, new TeleportToModelButton(this.schematic));
        buttons.put(4, new SaveModelButton(this.schematic));
        if (PotPvP.getInstance().getArenaHandler().getGrid().isBusy()) {
            Button busyButton = Button.placeholder((Material)Material.WOOL, (byte)DyeColor.SILVER.getWoolData(), (String)(ChatColor.GRAY.toString() + ChatColor.BOLD + "Grid is busy"));
            buttons.put(7, busyButton);
            buttons.put(8, busyButton);
        } else {
            buttons.put(7, new CreateCopiesButton(this.schematic));
            buttons.put(8, new RemoveCopiesButton(this.schematic));
        }
        buttons.put(9, new MenuBackButton(p -> new ManageSchematicsMenu().openMenu((Player)p)));
        Consumer<ArenaSchematic> save = schematic -> {
            try {
                PotPvP.getInstance().getArenaHandler().saveSchematics();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        buttons.put(18, new IntegerTraitButton<ArenaSchematic>(this.schematic, "Max Player Count", ArenaSchematic::setMaxPlayerCount, ArenaSchematic::getMaxPlayerCount, save));
        buttons.put(19, new IntegerTraitButton<ArenaSchematic>(this.schematic, "Min Player Count", ArenaSchematic::setMinPlayerCount, ArenaSchematic::getMinPlayerCount, save));
        buttons.put(20, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Supports Ranked", ArenaSchematic::setSupportsRanked, ArenaSchematic::isSupportsRanked, save));
        buttons.put(21, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Archer Only", ArenaSchematic::setArcherOnly, ArenaSchematic::isArcherOnly, save));
        buttons.put(22, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Sumo Only", ArenaSchematic::setSumoOnly, ArenaSchematic::isSumoOnly, save));
        buttons.put(23, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Spleef Only", ArenaSchematic::setSpleefOnly, ArenaSchematic::isSpleefOnly, save));
        buttons.put(24, new BooleanTraitButton<ArenaSchematic>(this.schematic, "BuildUHC Only", ArenaSchematic::setBuildUHCOnly, ArenaSchematic::isBuildUHCOnly, save));
        buttons.put(25, new BooleanTraitButton<ArenaSchematic>(this.schematic, "HCF Only", ArenaSchematic::setHCFOnly, ArenaSchematic::isHCFOnly, save));
        buttons.put(26, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Team Fights Only", ArenaSchematic::setTeamFightsOnly, ArenaSchematic::isTeamFightsOnly, save));
        buttons.put(27, new BooleanTraitButton<ArenaSchematic>(this.schematic, "Bridges Only", ArenaSchematic::setBridgesOnly, ArenaSchematic::isBridgesOnly, save));
        buttons.put(28, new BooleanTraitButton<ArenaSchematic>(this.schematic, "MLGRush Only", ArenaSchematic::setMlgOnly, ArenaSchematic::isMlgOnly, save));
        buttons.put(29, new BooleanTraitButton<ArenaSchematic>(this.schematic, "BedFight Only", ArenaSchematic::setBedFightsOnly, ArenaSchematic::isBedFightsOnly, save));
        buttons.put(30, new Button(){

            public String getName(Player player) {
                return ChatColor.GREEN + "Game Events";
            }

            public List<String> getDescription(Player player) {
                return Collections.singletonList(ChatColor.GRAY + "Manage which events can utilize this arena.");
            }

            public Material getMaterial(Player player) {
                return Material.EMERALD;
            }

            public void clicked(Player player, int slot, ClickType clickType) {
                new ManageEventsMenu(ManageSchematicMenu.this.schematic).openMenu(player);
            }
        });
        return buttons;
    }
}

