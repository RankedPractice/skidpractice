/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.ItemBuilder
 */
package cc.fyre.potpvp.lobby.menu;

import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.lobby.menu.statistics.GlobalEloButton;
import cc.fyre.potpvp.lobby.menu.statistics.KitButton;
import cc.fyre.potpvp.lobby.menu.statistics.PlayerButton;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.ItemBuilder;

public final class StatisticsMenu
extends Menu {
    private static final Button BLACK_PANE = Button.fromItem((ItemStack)ItemBuilder.of((Material)Material.STAINED_GLASS_PANE).data((short)DyeColor.BLACK.getData()).name(" ").build());

    public StatisticsMenu() {
        this.setAutoUpdate(true);
    }

    public String getTitle(Player player) {
        return "Statistics";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(this.getSlot(3, 1), new PlayerButton());
        buttons.put(this.getSlot(5, 1), new GlobalEloButton());
        int y = 3;
        int x = 1;
        for (KitType kitType : KitType.getAllTypes()) {
            if (!kitType.isSupportsRanked()) continue;
            buttons.put(this.getSlot(x++, y), new KitButton(kitType));
            if (x != 8) continue;
            ++y;
            x = 1;
        }
        for (int i = 0; i < 54; ++i) {
            buttons.putIfAbsent(i, BLACK_PANE);
        }
        return buttons;
    }
}

