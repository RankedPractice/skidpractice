/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.kit.listener;

import cc.fyre.potpvp.kit.menu.editkit.EditKitMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import rip.bridge.qlib.menu.Menu;

public final class KitEditorListener
implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) {
            return;
        }
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }
        if (Menu.currentlyOpenedMenus.get(player.getName()) instanceof EditKitMenu) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (Menu.currentlyOpenedMenus.get(player.getName()) instanceof EditKitMenu) {
            event.setCancelled(true);
        }
    }
}

