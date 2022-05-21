/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.killeffects.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.coinshop.menu.CoinsMenu;
import cc.fyre.potpvp.killeffects.effects.KillEffect;
import cc.fyre.potpvp.profile.Profile;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.menu.MenuBackButton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

public class KillEffectMenu
extends Menu {
    public String getTitle(Player player) {
        return "Kill Effects";
    }

    public boolean isPlaceholder() {
        return true;
    }

    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(8, new MenuBackButton(p -> new CoinsMenu().openMenu(player)));
        int i = 0;
        final Profile p2 = PotPvP.getInstance().getProfileManager().getProfile(player);
        for (final KillEffect killEffect : PotPvP.getInstance().getKillEffectHandler().getEffects()) {
            if (i <= 7) {
                buttons.put(this.getSlot(0 + i, 1), new Button(){

                    public String getName(Player player) {
                        return CC.translate(killEffect.getDisplayName());
                    }

                    public List<String> getDescription(Player player) {
                        KillEffect effect = p2.getKillEffect();
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            return CC.translate(Arrays.asList("&cYou lack permission to use this kill effect.", "&b" + CC.BLACK + " &fYou can purchase access to this on &cstore.aikar.cc&f."));
                        }
                        if (effect == null) {
                            return CC.translate(Arrays.asList("&fClick to &aactivate&7 the " + killEffect.getDisplayName() + " &fKill Effect"));
                        }
                        return CC.translate(Arrays.asList("&7Click to " + (effect == killEffect ? "&cdeactivate" : "&aactivate") + " &fthe " + killEffect.getDisplayName() + " &fKill Effect."));
                    }

                    public Material getMaterial(Player player) {
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            return Material.REDSTONE_BLOCK;
                        }
                        return killEffect.getIcon();
                    }

                    public void clicked(Player player, int slot, ClickType clickType) {
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            player.sendMessage(CC.translate("&cYou lack permission for this kill effect."));
                            player.sendMessage(CC.translate("&b" + CC.BLACK + " &fYou can purchase access to this on &cstore.aikar.cc&f."));
                            return;
                        }
                        if (p2.getKillEffect() == killEffect) {
                            player.sendMessage(CC.translate("&fYou have just &cde-activated &fthe " + killEffect.getDisplayName() + "&f."));
                            p2.setKillEffect(KillEffect.none);
                            p2.save();
                            return;
                        }
                        player.sendMessage(CC.translate("&fYou have just &aactivated &fthe " + killEffect.getDisplayName() + "&f."));
                        p2.setKillEffect(killEffect);
                        p2.save();
                    }
                });
            } else if (i <= 14) {
                buttons.put(this.getSlot(1 + i - 7, 2), new Button(){

                    public String getName(Player player) {
                        return CC.translate(killEffect.getDisplayName());
                    }

                    public List<String> getDescription(Player player) {
                        KillEffect effect = p2.getKillEffect();
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            return CC.translate(Arrays.asList("&cYou lack permission to use this kill effect.", "&b" + CC.BLACK + " &fYou can purchase access to this on &cstore.aikar.cc&f."));
                        }
                        if (effect == null) {
                            return CC.translate(Arrays.asList("&fClick to &aactivate&7 the " + killEffect.getDisplayName() + " &fKill Effect"));
                        }
                        return CC.translate(Arrays.asList("&7Click to " + (effect == killEffect ? "&cdeactivate" : "&aactivate") + " &fthe " + killEffect.getDisplayName() + " &fKill Effect."));
                    }

                    public Material getMaterial(Player player) {
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            return Material.REDSTONE_BLOCK;
                        }
                        return killEffect.getIcon();
                    }

                    public void clicked(Player player, int slot, ClickType clickType) {
                        if (!player.hasPermission("potpvp.killeffect." + killEffect.getName())) {
                            player.sendMessage(CC.translate("&cYou lack permission for this kill effect."));
                            player.sendMessage(CC.translate("&b" + CC.BLACK + " &fYou can purchase access to this on &cstore.aikar.cc&f."));
                            return;
                        }
                        if (p2.getKillEffect() == killEffect) {
                            p2.setKillEffect(null);
                            p2.save();
                            return;
                        }
                        player.sendMessage(CC.translate("&fYou have just &aactivated &fthe " + killEffect.getDisplayName() + "&f."));
                        p2.setKillEffect(killEffect);
                        p2.save();
                    }
                });
            }
            ++i;
        }
        return buttons;
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
}

