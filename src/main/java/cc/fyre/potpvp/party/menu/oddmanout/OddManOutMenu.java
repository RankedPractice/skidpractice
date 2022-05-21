/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.party.menu.oddmanout;

import cc.fyre.potpvp.party.menu.oddmanout.OddManOutButton;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public final class OddManOutMenu
extends Menu {
    private final Callback<Boolean> callback;

    public OddManOutMenu(Callback<Boolean> callback) {
        this.callback = (Callback)Preconditions.checkNotNull(callback, (Object)"callback");
    }

    public String getTitle(Player player) {
        return "Continue with unbalanced teams?";
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(this.getSlot(2, 0), new OddManOutButton(true, this.callback));
        buttons.put(this.getSlot(6, 0), new OddManOutButton(false, this.callback));
        return buttons;
    }
}

