/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.util.menu;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

public final class MenuBackButton
extends Button {
    private final Consumer<Player> openPreviousMenuConsumer;

    public MenuBackButton(Consumer<Player> openPreviousMenuConsumer) {
        this.openPreviousMenuConsumer = (Consumer)Preconditions.checkNotNull(openPreviousMenuConsumer, "openPreviousMenuConsumer");
    }

    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Back";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.RED + "Click here to return to"), (ChatColor.RED + "the previous menu."));
    }

    public Material getMaterial(Player player) {
        return Material.REDSTONE;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        this.openPreviousMenuConsumer.accept(player);
    }
}

