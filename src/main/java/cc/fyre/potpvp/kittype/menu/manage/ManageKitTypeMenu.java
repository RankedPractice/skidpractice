/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.kittype.menu.manage;

import cc.fyre.potpvp.command.ManageCommand;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.manage.CancelKitTypeEditButton;
import cc.fyre.potpvp.kittype.menu.manage.SaveKitTypeButton;
import cc.fyre.potpvp.util.menu.BooleanTraitButton;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public class ManageKitTypeMenu
extends Menu {
    private final KitType type;

    public ManageKitTypeMenu(KitType type2) {
        super("Editing " + type2.getDisplayName());
        this.setNoncancellingInventory(true);
        this.setUpdateAfterClick(false);
        this.type = type2;
    }

    public Map<Integer, Button> getButtons(Player player) {
        int i;
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (i = 1; i <= 5; ++i) {
            buttons.put(this.getSlot(1, i), Button.placeholder((Material)Material.OBSIDIAN));
        }
        for (i = 1; i <= 8; ++i) {
            buttons.put(this.getSlot(i, 1), Button.placeholder((Material)Material.OBSIDIAN));
        }
        buttons.put(this.getSlot(0, 1), new BooleanTraitButton<KitType>(this.type, "Hidden", KitType::setHidden, KitType::isHidden, KitType::saveAsync));
        buttons.put(this.getSlot(0, 2), new BooleanTraitButton<KitType>(this.type, "Editor Item Spawn", KitType::setEditorSpawnAllowed, KitType::isEditorSpawnAllowed, KitType::saveAsync));
        buttons.put(this.getSlot(0, 3), new BooleanTraitButton<KitType>(this.type, "Health Shown", KitType::setHealthShown, KitType::isHealthShown, KitType::saveAsync));
        buttons.put(this.getSlot(0, 4), new BooleanTraitButton<KitType>(this.type, "Building Allowed", KitType::setBuildingAllowed, KitType::isBuildingAllowed, KitType::saveAsync));
        buttons.put(this.getSlot(0, 5), new BooleanTraitButton<KitType>(this.type, "Hardcore Healing", KitType::setHardcoreHealing, KitType::isHardcoreHealing, KitType::saveAsync));
        buttons.put(this.getSlot(0, 6), new BooleanTraitButton<KitType>(this.type, "Pearl Damage", KitType::setPearlDamage, KitType::isPearlDamage, KitType::saveAsync));
        buttons.put(this.getSlot(0, 7), new BooleanTraitButton<KitType>(this.type, "Supports Ranked", KitType::setSupportsRanked, KitType::isSupportsRanked, KitType::saveAsync));
        buttons.put(this.getSlot(0, 8), new BooleanTraitButton<KitType>(this.type, "Supports Premium", KitType::setSupportsPremium, KitType::isSupportsPremium, KitType::saveAsync));
        buttons.put(this.getSlot(1, 0), new SaveKitTypeButton(this.type));
        buttons.put(this.getSlot(2, 0), new CancelKitTypeEditButton());
        buttons.put(this.getSlot(8, 0), new MenuBackButton(p -> new ManageCommand.ManageMenu().openMenu((Player)p)));
        ItemStack[] kit = this.type.getEditorItems();
        int x = 0;
        int y = 0;
        for (ItemStack editorItem : kit) {
            if (editorItem != null && editorItem.getType() != Material.AIR) {
                buttons.put(this.getSlot(x + 2, y + 2), this.nonCancellingItem(editorItem));
            }
            if (++x <= 6) continue;
            x = 0;
            if (++y >= 4) break;
        }
        return buttons;
    }

    private Button nonCancellingItem(final ItemStack stack) {
        return new Button(){

            public ItemStack getButtonItem(Player player) {
                return stack;
            }

            public String getName(Player player) {
                return stack.getItemMeta().getDisplayName();
            }

            public List<String> getDescription(Player player) {
                return stack.getItemMeta().getLore();
            }

            public Material getMaterial(Player player) {
                return stack.getType();
            }

            public boolean shouldCancel(Player player, int slot, ClickType clickType) {
                return false;
            }
        };
    }
}

