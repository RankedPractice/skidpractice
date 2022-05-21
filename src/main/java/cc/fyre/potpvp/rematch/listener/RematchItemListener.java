/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.rematch.listener;

import cc.fyre.potpvp.duel.command.AcceptCommand;
import cc.fyre.potpvp.duel.command.DuelCommand;
import cc.fyre.potpvp.rematch.RematchData;
import cc.fyre.potpvp.rematch.RematchHandler;
import cc.fyre.potpvp.rematch.RematchItems;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.ItemListener;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class RematchItemListener
extends ItemListener {
    public RematchItemListener(RematchHandler rematchHandler) {
        this.addHandler(RematchItems.REQUEST_REMATCH_ITEM, player -> {
            RematchData rematchData = rematchHandler.getRematchData((Player)player);
            if (rematchData != null) {
                Player target = Bukkit.getPlayer((UUID)rematchData.getTarget());
                DuelCommand.duel(player, target, rematchData.getKitType(), null, true);
                InventoryUtils.resetInventoryDelayed(player);
                InventoryUtils.resetInventoryDelayed(target);
            }
        });
        this.addHandler(RematchItems.SENT_REMATCH_ITEM, p -> p.sendMessage(ChatColor.RED + "You have already sent a rematch request."));
        this.addHandler(RematchItems.ACCEPT_REMATCH_ITEM, player -> {
            RematchData rematchData = rematchHandler.getRematchData((Player)player);
            if (rematchData != null) {
                Player target = Bukkit.getPlayer((UUID)rematchData.getTarget());
                AcceptCommand.accept(player, target);
            }
        });
    }
}

