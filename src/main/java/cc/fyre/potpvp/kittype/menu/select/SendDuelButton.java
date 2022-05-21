/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.kittype.menu.select;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.util.Callback;

public class SendDuelButton
extends Button {
    private Set<String> maps;
    private Callback<Set<String>> mapsCallback;

    public List<String> getDescription(Player arg0) {
        return ImmutableList.of();
    }

    public Material getMaterial(Player arg0) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player arg0) {
        return DyeColor.LIME.getWoolData();
    }

    public String getName(Player player) {
        return ChatColor.GREEN + "Send duel";
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        if (this.maps.size() < 2) {
            player.sendMessage(ChatColor.RED + "You must select at least two maps.");
            return;
        }
        this.mapsCallback.callback(this.maps);
    }

    public SendDuelButton(Set<String> maps, Callback<Set<String>> mapsCallback) {
        this.maps = maps;
        this.mapsCallback = mapsCallback;
    }
}

