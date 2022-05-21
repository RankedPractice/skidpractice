/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.kit.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.KitItems;
import cc.fyre.potpvp.kit.menu.kits.KitsMenu;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.util.ItemListener;
import com.google.common.base.Predicate;
import org.bukkit.entity.Player;
import rip.bridge.qlib.util.Callback;

public final class KitItemListener
extends ItemListener {
    public KitItemListener() {
        this.addHandler(KitItems.OPEN_EDITOR_ITEM, player -> {
            LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
            if (lobbyHandler.isInLobby((Player)player)) {
                new SelectKitTypeMenu((Callback<KitType>)(kitType -> new KitsMenu((KitType)kitType).openMenu((Player)player)), "Select a kit to edit...").predicate((Predicate<KitType>)(KitType::isEditorSpawnAllowed)).openMenu((Player)player);
            }
        });
    }
}

