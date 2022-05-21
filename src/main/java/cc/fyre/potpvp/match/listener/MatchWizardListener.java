/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.FireworkEffect$Type
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Snowball
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.util.FireworkEffectPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class MatchWizardListener
implements Listener {
    private final FireworkEffectPlayer fireworkEffectPlayer = new FireworkEffectPlayer();

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player;
        if (!event.hasItem() || event.getItem().getType() != Material.DIAMOND_HOE || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        final Match match = matchHandler.getMatchPlaying(player = event.getPlayer());
        if (match == null || !match.getKitType().getId().contains("WIZARD")) {
            return;
        }
        final FireworkEffect effect = FireworkEffect.builder().withColor(Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).build();
        final Snowball snowball = (Snowball)player.launchProjectile(Snowball.class);
        snowball.setVelocity(snowball.getVelocity().multiply(2));
        new BukkitRunnable(){
            int ticks = 0;

            public void run() {
                if (this.ticks++ >= 100) {
                    this.cancel();
                    return;
                }
                if (snowball.isDead() || snowball.isOnGround()) {
                    for (Entity entity : snowball.getNearbyEntities(4.0, 4.0, 4.0)) {
                        MatchTeam entityTeam = match.getTeam(entity.getUniqueId());
                        if (entityTeam == null || entityTeam.getAllMembers().contains(player.getUniqueId())) continue;
                        entity.setVelocity(entity.getLocation().toVector().subtract(snowball.getLocation().toVector()).normalize().add(new Vector(0.0, 0.7, 0.0)));
                    }
                    snowball.remove();
                    this.cancel();
                } else {
                    try {
                        MatchWizardListener.this.fireworkEffectPlayer.playFirework(snowball.getWorld(), snowball.getLocation(), effect);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 1L, 1L);
    }
}

