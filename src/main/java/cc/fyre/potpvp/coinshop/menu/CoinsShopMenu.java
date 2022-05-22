/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.coinshop.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.coinshop.menu.CoinsMenu;
import cc.fyre.potpvp.coinshop.menu.type.CoinsShopItem;
import cc.fyre.potpvp.profile.Profile;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.ItemBuilder;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public class CoinsShopMenu extends Menu {

    public String getTitle(Player player) {
        return CC.translate("&b&lCoin Shop");
    }

    @Override
    public void setAutoUpdate(boolean autoUpdate) {
        super.setAutoUpdate(true);
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(4, new CoinsButton());
        buttons.put(8, new MenuBackButton(p -> new CoinsMenu().openMenu(player)));
        int i = 9;
        for (CoinsShopItem value : CoinsShopItem.values()) {
            buttons.put(i, new ItemButton(value));
            ++i;
        }
        return buttons;
    }

    public boolean isPlaceholder() {
        return true;
    }

    public int size(Map<Integer, Button> buttons) {
        return this.actualSize(buttons) + 9;
    }

    public int actualSize(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue <= highest) continue;
            highest = buttonValue;
        }
        return (int)(Math.ceil((double)(highest + 1) / 9.0) * 9.0);
    }

    public static class CoinsButton
    extends Button {
        public String getName(Player player) {
            return CC.translate("&b&lYour Coins");
        }

        public List<String> getDescription(Player player) {
            ArrayList<String> toReturn = new ArrayList<String>();
            Profile p = PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId());
            toReturn.add(CC.translate(" &fCurrent coins: &b" + p.getCoins()));
            toReturn.add(CC.translate("&7"));
            toReturn.add(CC.translate("&7Claim your daily coins or vote us on NameMC!"));
            return toReturn;
        }

        public Material getMaterial(Player player) {
            return Material.DOUBLE_PLANT;
        }
    }

    public static class ItemButton
    extends Button {
        private final CoinsShopItem item;

        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.item.getMaterial()).amount(this.item.getAmount()).name(CC.translate(this.item.getDisplayName())).durability(this.item.getData()).lore(CC.translate(Arrays.asList("&fPrice&7: &b" + this.item.getPrice() + "\u20ac", "&7Click to purchase this item"))).build();
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

        public void clicked(Player player, int slot, ClickType clickType) {
            int stars = PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId()).getCoins();
            if (stars < this.item.getPrice()) {
                player.sendMessage(CC.translate("&cYou cannot afford this."));
                return;
            }
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)this.item.getCommand().replaceAll("%player%", player.getName()));
            player.sendMessage(CC.translate("&fYou have just purchased &b" + this.item.getDisplayName() + " &ffor &b" + this.item.getPrice() + "\u20ac"));
            PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId()).setCoins(stars - this.item.getPrice());
            PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId()).save();
        }

        public ItemButton(CoinsShopItem item) {
            this.item = item;
        }
    }
}

