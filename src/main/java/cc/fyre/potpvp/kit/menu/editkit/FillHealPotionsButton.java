/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.editkit;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;

final class FillHealPotionsButton
extends Button {
    private final short durability;

    FillHealPotionsButton(short durability) {
        this.durability = durability;
    }

    public String getName(Player player) {
        return ChatColor.BLUE + "Fill Empty Space";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.LIGHT_PURPLE + "Fill your empty inventory space"), (ChatColor.LIGHT_PURPLE + "with Splash Health Potions."));
    }

    public Material getMaterial(Player player) {
        return Material.POTION;
    }

    public ItemStack getButtonItem(Player player) {
        ItemStack superItem = super.getButtonItem(player);
        superItem.setDurability(this.durability);
        return superItem;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        ItemStack potion = new ItemStack(Material.POTION);
        potion.setDurability(this.durability);
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            player.getInventory().addItem(new ItemStack[]{potion});
        }
    }
}

