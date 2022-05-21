/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.party.menu.oddmanout;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.util.Callback;

final class OddManOutButton
extends Button {
    private final boolean oddManOut;
    private final Callback<Boolean> callback;

    OddManOutButton(boolean oddManOut, Callback<Boolean> callback) {
        this.oddManOut = oddManOut;
        this.callback = (Callback)Preconditions.checkNotNull(callback, (Object)"callback");
    }

    public String getName(Player player) {
        if (this.oddManOut) {
            return ChatColor.GREEN.toString() + ChatColor.BOLD + "Have a random player sit out";
        }
        return ChatColor.RED.toString() + ChatColor.BOLD + "Continue with uneven teams";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of();
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return (this.oddManOut ? DyeColor.GREEN : DyeColor.RED).getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        this.callback.callback((Object)this.oddManOut);
    }
}

