/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.kit.menu.kits;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.KitHandler;
import cc.fyre.potpvp.kit.menu.kits.KitDeleteButton;
import cc.fyre.potpvp.kit.menu.kits.KitEditButton;
import cc.fyre.potpvp.kit.menu.kits.KitIconButton;
import cc.fyre.potpvp.kit.menu.kits.KitRenameButton;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public final class KitsMenu
extends Menu {
    private final KitType kitType;

    public KitsMenu(KitType kitType) {
        super("Viewing " + kitType.getDisplayName() + " kits");
        this.setPlaceholder(true);
        this.setAutoUpdate(true);
        this.kitType = kitType;
    }

    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    public Map<Integer, Button> getButtons(Player player) {
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (int kitSlot = 1; kitSlot <= 4; ++kitSlot) {
            Optional<Kit> kitOpt = kitHandler.getKit(player, this.kitType, kitSlot);
            int column = kitSlot * 2 - 1;
            buttons.put(this.getSlot(column, 0), new KitIconButton(kitOpt, this.kitType, kitSlot));
            buttons.put(this.getSlot(column, 2), new KitEditButton(kitOpt, this.kitType, kitSlot));
            if (kitOpt.isPresent()) {
                buttons.put(this.getSlot(column, 3), new KitRenameButton(kitOpt.get()));
                buttons.put(this.getSlot(column, 4), new KitDeleteButton(this.kitType, kitSlot));
                continue;
            }
            buttons.put(this.getSlot(column, 3), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)DyeColor.GRAY.getWoolData(), (String)""));
            buttons.put(this.getSlot(column, 4), Button.placeholder((Material)Material.STAINED_GLASS_PANE, (byte)DyeColor.GRAY.getWoolData(), (String)""));
        }
        buttons.put(this.getSlot(0, 4), new MenuBackButton(p -> new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> new KitsMenu((KitType)kitType).openMenu((Player)p)), "Select a kit type...").openMenu((Player)p)));
        return buttons;
    }
}

