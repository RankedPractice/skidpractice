/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.menu.manageschematics.ManageSchematicsMenu;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.manage.ManageKitTypeMenu;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.util.VisibilityUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public final class ManageCommand {
    @Command(names={"adminmanage"}, permission="potpvp.admin")
    public static void manage(Player sender) {
        new ManageMenu().openMenu(sender);
    }

    @Command(names={"adminmanage host"}, permission="op")
    public static void toggleHost(CommandSender sender) {
        PotPvP.getInstance().getGameHandler().setHostEnabled(!PotPvP.getInstance().getGameHandler().getHostEnabled());
        if (PotPvP.getInstance().getGameHandler().getHostEnabled()) {
            sender.sendMessage(ChatColor.GOLD + "Players can now host events.");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Players can no longer host events.");
        }
    }

    @Command(names={"adminmanage visibility"}, permission="op")
    public static void toggleVisibility(CommandSender sender) {
        boolean bl = VisibilityUtils.SHOW_IN_LOBBY = !VisibilityUtils.SHOW_IN_LOBBY;
        if (VisibilityUtils.SHOW_IN_LOBBY) {
            sender.sendMessage(ChatColor.GOLD + "Players in the lobby can now see other players in the lobby.");
        } else {
            sender.sendMessage(ChatColor.GOLD + "Players in the lobby can no longer see other players in the lobby.");
        }
        for (Player player : PotPvP.getInstance().getServer().getOnlinePlayers()) {
            VisibilityUtils.updateVisibility(player);
        }
    }

    public static class ManageMenu
    extends Menu {
        public ManageMenu() {
            super("Admin Management Menu");
        }

        public Map<Integer, Button> getButtons(Player player) {
            return ImmutableMap.of((Object)3, (Object)((Object)new ManageKitButton()), (Object)5, (Object)((Object)new ManageArenaButton()));
        }
    }

    private static class ManageArenaButton
    extends Button {
        private ManageArenaButton() {
        }

        public String getName(Player player) {
            return ChatColor.YELLOW + "Manage the arena grid";
        }

        public List<String> getDescription(Player player) {
            return ImmutableList.of();
        }

        public Material getMaterial(Player player) {
            return Material.IRON_PICKAXE;
        }

        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new ManageSchematicsMenu().openMenu(player);
        }
    }

    private static class ManageKitButton
    extends Button {
        private ManageKitButton() {
        }

        public String getName(Player player) {
            return ChatColor.YELLOW + "Manage kit type definitions";
        }

        public List<String> getDescription(Player player) {
            return ImmutableList.of();
        }

        public Material getMaterial(Player player) {
            return Material.DIAMOND_SWORD;
        }

        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                player.closeInventory();
                new ManageKitTypeMenu((KitType)kitType).openMenu(player);
            }), false, "Manage Kit Type...").openMenu(player);
        }
    }
}

