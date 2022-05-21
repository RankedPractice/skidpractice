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
package cc.fyre.potpvp.kittype.menu.select;

import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.KitTypeButton;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.util.InventoryUtils;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;
import rip.bridge.qlib.util.Callback;

public final class CustomSelectKitTypeMenu
extends Menu {
    private final Callback<KitType> callback;
    private final Function<KitType, CustomKitTypeMeta> metaFunc;
    private final QueueType queueType;
    private final String title;

    public CustomSelectKitTypeMenu(Callback<KitType> callback, Function<KitType, CustomKitTypeMeta> metaFunc, String title, QueueType queueType) {
        this.title = title;
        this.setAutoUpdate(true);
        this.callback = (Callback)Preconditions.checkNotNull(callback, (Object)"callback");
        this.metaFunc = (Function)Preconditions.checkNotNull(metaFunc, (Object)"metaFunc");
        this.queueType = queueType;
    }

    public String getTitle(Player player) {
        return this.title;
    }

    public void onClose(Player player) {
        InventoryUtils.resetInventoryDelayed(player);
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int index = 0;
        for (KitType kitType : KitType.getAllTypes()) {
            if (!player.isOp() && kitType.isHidden() || this.queueType.isRanked() && !kitType.isSupportsRanked() || this.queueType.isPremium() && !kitType.isSupportsPremium()) continue;
            CustomKitTypeMeta meta = this.metaFunc.apply(kitType);
            buttons.put(index++, new KitTypeButton(kitType, this.callback, meta.getDescription(), meta.getQuantity()));
        }
        return buttons;
    }

    public static final class CustomKitTypeMeta {
        private int quantity;
        private List<String> description;

        public CustomKitTypeMeta(int quantity, List<String> description2) {
            this.quantity = quantity;
            this.description = description2;
        }

        public int getQuantity() {
            return this.quantity;
        }

        public List<String> getDescription() {
            return this.description;
        }
    }
}

