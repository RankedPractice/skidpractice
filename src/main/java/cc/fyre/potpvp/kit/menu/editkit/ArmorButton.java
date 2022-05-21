/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import rip.bridge.qlib.menu.Button;

final class ArmorButton
extends Button {
    private final ItemStack item;

    ArmorButton(ItemStack item) {
        this.item = (ItemStack)Preconditions.checkNotNull((Object)item, (Object)"item");
    }

    public ItemStack getButtonItem(Player player) {
        ItemStack newItem = this.item.clone();
        ItemMeta itemMeta = newItem.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore((List)ImmutableList.of((Object)"", (Object)(ChatColor.YELLOW + "This is automatically equipped.")));
            newItem.setItemMeta(itemMeta);
        }
        return newItem;
    }

    public String getName(Player player) {
        return null;
    }

    public List<String> getDescription(Player player) {
        return null;
    }

    public Material getMaterial(Player player) {
        return null;
    }
}

