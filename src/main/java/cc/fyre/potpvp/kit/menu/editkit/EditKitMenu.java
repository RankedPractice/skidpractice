/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.kit.menu.editkit;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.menu.editkit.ArmorButton;
import cc.fyre.potpvp.kit.menu.editkit.CancelKitEditButton;
import cc.fyre.potpvp.kit.menu.editkit.ClearInventoryButton;
import cc.fyre.potpvp.kit.menu.editkit.FillHealPotionsButton;
import cc.fyre.potpvp.kit.menu.editkit.KitInfoButton;
import cc.fyre.potpvp.kit.menu.editkit.LoadDefaultKitButton;
import cc.fyre.potpvp.kit.menu.editkit.SaveButton;
import cc.fyre.potpvp.kit.menu.editkit.TakeItemButton;
import cc.fyre.potpvp.kit.menu.editkit.UnselectableItemButton;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public final class EditKitMenu
extends Menu {
    private static final int EDITOR_X_OFFSET = 2;
    private static final int EDITOR_Y_OFFSET = 2;
    private final Kit kit;

    public EditKitMenu(Kit kit) {
        super("Editing " + kit.getName());
        this.setNoncancellingInventory(true);
        this.setUpdateAfterClick(false);
        this.kit = (Kit)Preconditions.checkNotNull((Object)kit, (Object)"kit");
    }

    public void onOpen(Player player) {
        player.getInventory().setContents(this.kit.getInventoryContents());
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }

    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    public Map<Integer, Button> getButtons(Player player) {
        int i;
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (i = 0; i <= 5; ++i) {
            buttons.put(this.getSlot(1, i), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)8, (String[])new String[0]));
        }
        for (i = 0; i <= 8; ++i) {
            buttons.put(this.getSlot(i, 1), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)8, (String[])new String[0]));
        }
        buttons.put(this.getSlot(3, 0), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)8, (String[])new String[0]));
        buttons.put(this.getSlot(4, 0), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)8, (String[])new String[0]));
        buttons.put(this.getSlot(5, 0), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)8, (String[])new String[0]));
        buttons.put(this.getSlot(0, 0), new KitInfoButton(this.kit));
        buttons.put(this.getSlot(2, 0), new SaveButton(this.kit));
        buttons.put(this.getSlot(6, 0), new LoadDefaultKitButton(this.kit.getType()));
        buttons.put(this.getSlot(7, 0), new ClearInventoryButton());
        buttons.put(this.getSlot(8, 0), new CancelKitEditButton(this.kit.getType()));
        for (ItemStack armorItem : this.kit.getType().getDefaultArmor()) {
            int armorYOffset = 2;
            int armorSlot = -1;
            if (armorItem.getType().name().contains("HELMET")) {
                armorSlot = 0;
            } else if (armorItem.getType().name().contains("CHESTPLATE")) {
                armorSlot = 1;
            } else if (armorItem.getType().name().contains("LEGGINGS")) {
                armorSlot = 2;
            } else if (armorItem.getType().name().contains("BOOTS")) {
                armorSlot = 3;
            }
            buttons.put(this.getSlot(0, armorSlot + armorYOffset), new ArmorButton(armorItem));
        }
        if (this.kit.getType().isEditorSpawnAllowed()) {
            short splashHealPotionDura = -1;
            int x = 0;
            int y = 0;
            for (ItemStack editorItem : this.kit.getType().getEditorItems()) {
                if (editorItem != null) {
                    Potion potion;
                    if (editorItem.getType() == Material.POTION && (potion = Potion.fromItemStack((ItemStack)editorItem)).isSplash() && potion.getType() == PotionType.INSTANT_HEAL) {
                        splashHealPotionDura = editorItem.getDurability();
                    }
                    if (editorItem.getType() != Material.AIR) {
                        buttons.put(this.getSlot(x + 2, y + 2), new TakeItemButton(editorItem));
                    }
                }
                if (++x <= 6) continue;
                x = 0;
                if (++y >= 4) break;
            }
            if (splashHealPotionDura > 0) {
                buttons.put(this.getSlot(8, 5), new FillHealPotionsButton(splashHealPotionDura));
            }
        } else {
            for (int x = 0; x < 7; ++x) {
                for (int y = 0; y < 4; ++y) {
                    buttons.put(this.getSlot(x + 2, y + 2), new UnselectableItemButton());
                }
            }
        }
        return buttons;
    }
}

