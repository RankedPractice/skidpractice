/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.base.Predicate
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.kittype.menu.select;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.KitTypeButton;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public final class SelectKitTypeMenu
extends Menu {
    private final boolean reset;
    private final Callback<KitType> callback;
    private Predicate<KitType> predicate;
    private final String title;

    public SelectKitTypeMenu(Callback<KitType> callback, String title) {
        this(callback, true, title);
    }

    public SelectKitTypeMenu(Callback<KitType> callback, boolean reset, String title) {
        this.title = title;
        this.callback = (Callback)Preconditions.checkNotNull(callback, "callback");
        this.reset = reset;
    }

    public SelectKitTypeMenu predicate(Predicate<KitType> predicate) {
        this.predicate = predicate;
        return this;
    }

    public String getTitle(Player player) {
        return ChatColor.BLUE.toString() + ChatColor.BOLD + this.title;
    }

    public void onClose(Player player) {
        if (this.reset) {
            InventoryUtils.resetInventoryDelayed(player);
        }
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int index = 0;
        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden() || this.predicate != null && !this.predicate.apply(kitType)) continue;
            buttons.put(index++, new KitTypeButton(kitType, this.callback));
        }
        Party party = PotPvP.getInstance().getPartyHandler().getParty(player);
        if (party != null) {
            buttons.put(8, new KitTypeButton(KitType.teamFight, this.callback));
        }
        return buttons;
    }
}

