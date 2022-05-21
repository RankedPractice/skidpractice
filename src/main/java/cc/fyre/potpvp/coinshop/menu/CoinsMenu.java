/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.util.gnu.trove.map.TObjectLongMap
 *  net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap
 *  net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.coinshop.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.coinshop.menu.CoinsShopMenu;
import cc.fyre.potpvp.killeffects.menu.KillEffectMenu;
import cc.fyre.potpvp.profile.Profile;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.DateTimeFormats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.gnu.trove.map.TObjectLongMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectLongHashMap;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public class CoinsMenu
extends Menu {
    public String getTitle(Player player) {
        return CC.translate("&6Star Shop");
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(10, new CoinsButton());
        buttons.put(12, new ClaimCoinsButton());
        buttons.put(14, new CoinShopButton());
        buttons.put(16, new KillEffectsButton());
        return buttons;
    }

    public boolean isPlaceholder() {
        return true;
    }

    public int size(Map<Integer, Button> buttons) {
        return 27;
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

    public static class ClaimCoinsButton
    extends Button {
        public static TObjectLongMap playerCooldowns = new TObjectLongHashMap();
        private static long MINUTE = TimeUnit.MINUTES.toMillis(1L);
        private static long HOUR = TimeUnit.HOURS.toMillis(1L);
        private static long MULTI_HOUR = TimeUnit.HOURS.toMillis(10L);

        public String getName(Player player) {
            return CC.translate("&b&lClaim daily coins");
        }

        public List<String> getDescription(Player player) {
            ArrayList<String> toReturn = new ArrayList<String>();
            long timestamp = playerCooldowns.get((Object)player);
            long millis = System.currentTimeMillis();
            long remaining = timestamp == playerCooldowns.getNoEntryValue() ? -1L : timestamp - millis;
            toReturn.add(CC.translate("&f Get a coins reward every 24 hours"));
            toReturn.add(CC.translate("&7"));
            if (remaining > 0L) {
                toReturn.add(CC.translate("&fYou can claim again your reward in &b" + ClaimCoinsButton.getRemaining(remaining, true) + "&f."));
            } else {
                toReturn.add(CC.translate("&aYou can claim the bonus."));
            }
            return toReturn;
        }

        public void clicked(Player player, int slot, ClickType clickType) {
            if (player != null) {
                long remaining;
                long timestamp = playerCooldowns.get((Object)player);
                long millis = System.currentTimeMillis();
                long l = remaining = timestamp == playerCooldowns.getNoEntryValue() ? -1L : timestamp - millis;
                if (remaining > 0L) {
                    player.sendMessage(CC.translate("&fYou can claim again your reward in &b" + ClaimCoinsButton.getRemaining(remaining, true) + "h&f."));
                } else {
                    int toAdd = PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId()).getCoins() + 100;
                    PotPvP.getInstance().getProfileManager().getProfile(player.getUniqueId()).setCoins(toAdd);
                    player.sendMessage(CC.translate("&aYou redeemed your daily coins!"));
                    playerCooldowns.put((Object)player, millis + TimeUnit.HOURS.toMillis(24L));
                }
            }
        }

        public static String getRemaining(long millis, boolean milliseconds) {
            return ClaimCoinsButton.getRemaining(millis, milliseconds, true);
        }

        public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
            if (milliseconds && duration < MINUTE) {
                return (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format((double)duration * 0.001) + 's';
            }
            return DurationFormatUtils.formatDuration((long)duration, (String)((duration >= HOUR ? (duration >= MULTI_HOUR ? "H" : "") + "H:" : "") + "mm:ss"));
        }

        public Material getMaterial(Player player) {
            return Material.CHEST;
        }
    }

    public static class CoinShopButton
    extends Button {
        public String getName(Player player) {
            return CC.translate("&b&lCoin Shop");
        }

        public List<String> getDescription(Player player) {
            ArrayList<String> toReturn = new ArrayList<String>();
            toReturn.add(CC.translate("&f Open the coin shop!"));
            return toReturn;
        }

        public void clicked(Player player, int slot, ClickType clickType) {
            new CoinsShopMenu().openMenu(player);
        }

        public Material getMaterial(Player player) {
            return Material.ENCHANTED_BOOK;
        }
    }

    public static class KillEffectsButton
    extends Button {
        public String getName(Player player) {
            return CC.translate("&b&lKill Effects");
        }

        public List<String> getDescription(Player player) {
            ArrayList<String> toReturn = new ArrayList<String>();
            toReturn.add(CC.translate("&f Open the kill effects menu!"));
            return toReturn;
        }

        public void clicked(Player player, int slot, ClickType clickType) {
            new KillEffectMenu().openMenu(player);
        }

        public Material getMaterial(Player player) {
            return Material.ITEM_FRAME;
        }
    }
}

