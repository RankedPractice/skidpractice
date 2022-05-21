/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  rip.bridge.qlib.util.ItemBuilder
 */
package cc.fyre.potpvp.match.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.bridge.qlib.util.ItemBuilder;

public final class GoldenHeadListener
implements Listener {
    private static final int HEALING_POINTS = 8;
    private static final ItemStack GOLDEN_HEAD = ItemBuilder.of((Material)Material.GOLDEN_APPLE).name("&6&lGolden Head").build();

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (this.matches(item)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
        }
    }

    private boolean matches(ItemStack item) {
        return GOLDEN_HEAD.isSimilar(item);
    }
}

