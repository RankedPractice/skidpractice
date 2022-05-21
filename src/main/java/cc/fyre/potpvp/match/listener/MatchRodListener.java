/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.FishHook
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerItemDamageEvent
 */
package cc.fyre.potpvp.match.listener;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public final class MatchRodListener
implements Listener {
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if (!Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(event.getItem())) {
            return;
        }
        if (player.getLastDamageCause() != null && player.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)player.getLastDamageCause()).getDamager() instanceof FishHook) {
            event.setCancelled(true);
        }
    }
}

