/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class MatchStatsListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        Player damager = (Player)event.getDamager();
        Player damaged = (Player)event.getEntity();
        Match damagerMatch = PotPvP.getInstance().getMatchHandler().getMatchPlaying(damager);
        if (damagerMatch == null) {
            return;
        }
        Map<UUID, UUID> lastHitMap = damagerMatch.getLastHit();
        Map<UUID, Integer> combos = damagerMatch.getCombos();
        Map<UUID, Integer> totalHits = damagerMatch.getTotalHits();
        Map<UUID, Integer> longestCombo = damagerMatch.getLongestCombo();
        UUID lastHit = lastHitMap.put(damager.getUniqueId(), damaged.getUniqueId());
        if (lastHit != null) {
            if (lastHit.equals(damaged.getUniqueId())) {
                combos.put(damager.getUniqueId(), combos.getOrDefault(damager.getUniqueId(), 0) + 1);
            } else {
                combos.put(damager.getUniqueId(), 1);
            }
            longestCombo.put(damager.getUniqueId(), Math.max(combos.get(damager.getUniqueId()), longestCombo.getOrDefault(damager.getUniqueId(), 1)));
        } else {
            combos.put(damager.getUniqueId(), 0);
        }
        totalHits.put(damager.getUniqueId(), totalHits.getOrDefault(damager.getUniqueId(), 0) + 1);
        while (lastHitMap.values().remove(damager.getUniqueId())) {
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPotionLaunch(ProjectileLaunchEvent event) {
        Projectile thrownEntity = event.getEntity();
        if (!(thrownEntity instanceof ThrownPotion)) {
            return;
        }
        ThrownPotion thrownPotion = (ThrownPotion)thrownEntity;
        ProjectileSource projectileSource = thrownPotion.getShooter();
        if (!(projectileSource instanceof Player)) {
            return;
        }
        Player player = (Player)projectileSource;
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        if (match == null) {
            return;
        }
        match.getMissedPots().put(player.getUniqueId(), match.getMissedPots().getOrDefault(player.getUniqueId(), 0) + 1);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onSplash(PotionSplashEvent event) {
        ThrownPotion thrownPotion = event.getEntity();
        if (thrownPotion.getItem().getDurability() != 16421) {
            return;
        }
        ProjectileSource projectileSource = thrownPotion.getShooter();
        if (!(projectileSource instanceof Player)) {
            return;
        }
        Player player = (Player)projectileSource;
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        if (match == null) {
            return;
        }
        for (LivingEntity affectedEntity : event.getAffectedEntities()) {
            if (!affectedEntity.getUniqueId().equals(player.getUniqueId()) || !(event.getIntensity(affectedEntity) >= 0.5)) continue;
            match.getMissedPots().put(player.getUniqueId(), Math.max(match.getMissedPots().getOrDefault(player.getUniqueId(), 1) - 1, 0));
        }
    }
}

