/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.pagination.PaginatedMenu
 */
package cc.fyre.potpvp.party.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.pvpclasses.PvPClasses;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.pagination.PaginatedMenu;

public class RosterMenu
extends PaginatedMenu {
    private final Party party;

    public RosterMenu(Party party) {
        this.party = party;
        this.setAutoUpdate(true);
        this.setUpdateAfterClick(true);
        this.setPlaceholder(true);
    }

    public String getPrePaginatedTitle(Player player) {
        return "Team Roster";
    }

    public Map<Integer, Button> getAllPagesButtons(Player player) {
        HashMap<Integer, Button> toReturn = new HashMap<Integer, Button>();
        if (this.party.getMembers().size() == 1) {
            player.closeInventory();
            return toReturn;
        }
        for (final UUID uuid : new ArrayList<UUID>(this.party.getKits().keySet())) {
            if (this.party.getMembers().contains(uuid)) continue;
            this.party.getKits().remove(uuid);
        }
        for (final UUID uuid : this.party.getMembers()) {
            final Player member = Bukkit.getPlayer((UUID)uuid);
            if (member == null) continue;
            final PvPClasses selected = this.party.getKits().getOrDefault(uuid, PvPClasses.DIAMOND);
            toReturn.put(toReturn.isEmpty() ? 0 : toReturn.size(), new Button(){

                public String getName(Player player) {
                    return member.getDisplayName();
                }

                public List<String> getDescription(Player player) {
                    ArrayList<String> description2 = new ArrayList<String>();
                    for (PvPClasses kit : PvPClasses.values()) {
                        if (kit == selected) {
                            description2.add(ChatColor.GREEN + "> " + kit.getName());
                            continue;
                        }
                        if (kit.allowed(RosterMenu.this.party)) {
                            description2.add(ChatColor.GRAY + kit.getName());
                            continue;
                        }
                        description2.add(ChatColor.RED + ChatColor.STRIKETHROUGH.toString() + kit.getName());
                    }
                    return description2;
                }

                public Material getMaterial(Player player) {
                    return selected.getIcon();
                }

                public void clicked(Player player, int slot, ClickType clickType) {
                    if (RosterMenu.this.party.isLeader(player.getUniqueId())) {
                        List<PvPClasses> kits = Arrays.asList(PvPClasses.values());
                        int index = kits.indexOf((Object)selected);
                        PvPClasses next2 = null;
                        for (int times = 0; next2 == null && times <= 50; ++times) {
                            if (index + 1 < kits.size()) {
                                next2 = kits.get(index + 1);
                                if (next2.allowed(RosterMenu.this.party)) continue;
                                next2 = null;
                                ++index;
                                continue;
                            }
                            index = -1;
                        }
                        if (next2 == null) {
                            next2 = PvPClasses.DIAMOND;
                        }
                        RosterMenu.this.party.message(player.getDisplayName() + ChatColor.YELLOW + " has set " + member.getDisplayName() + ChatColor.YELLOW + "'s " + ChatColor.YELLOW + "kit to " + ChatColor.GRAY + next2.getName() + ChatColor.YELLOW + ".");
                        RosterMenu.this.party.getKits().put(uuid, next2);
                        for (UUID other : RosterMenu.this.party.getMembers()) {
                            PotPvP.getInstance().getPartyHandler().updatePartyCache(other, RosterMenu.this.party);
                        }
                    }
                }
            });
        }
        return toReturn;
    }

    public int size(Map<Integer, Button> buttons) {
        return 45;
    }

    public int getMaxItemsPerPage(Player player) {
        return 36;
    }
}

