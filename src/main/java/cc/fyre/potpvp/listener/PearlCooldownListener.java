/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.EnderPearl
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PearlCooldownListener
implements Listener {
    private static final long PEARL_COOLDOWN_MILLIS = TimeUnit.SECONDS.toMillis(16L);
    private final Map<UUID, Long> pearlCooldown = new ConcurrentHashMap<UUID, Long>();

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ENDER_PEARL) {
            return;
        }
        EnderPearl pearl = (EnderPearl)event.getEntity();
        final Player shooter = (Player)pearl.getShooter();
        this.pearlCooldown.put(shooter.getUniqueId(), System.currentTimeMillis() + PEARL_COOLDOWN_MILLIS);
        new BukkitRunnable(){

            public void run() {
                long cooldownExpires = PearlCooldownListener.this.pearlCooldown.getOrDefault(shooter.getUniqueId(), 0L);
                if (cooldownExpires < System.currentTimeMillis()) {
                    this.cancel();
                    return;
                }
                int millisLeft = (int)(cooldownExpires - System.currentTimeMillis());
                float percentLeft = (float)millisLeft / (float)PEARL_COOLDOWN_MILLIS;
                shooter.setExp(percentLeft);
                shooter.setLevel(millisLeft / 1000);
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 1L, 1L);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem().getType() != Material.ENDER_PEARL || !event.getAction().name().contains("RIGHT_")) {
            return;
        }
        Player player = event.getPlayer();
        long cooldownExpires = this.pearlCooldown.getOrDefault(player.getUniqueId(), 0L);
        if (cooldownExpires < System.currentTimeMillis()) {
            return;
        }
        int millisLeft = (int)(cooldownExpires - System.currentTimeMillis());
        double secondsLeft = (double)millisLeft / 1000.0;
        secondsLeft = (double)Math.round(10.0 * secondsLeft) / 10.0;
        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + secondsLeft + ChatColor.RED + " seconds!");
        player.updateInventory();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.pearlCooldown.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (EnderPearl pearl : player.getWorld().getEntitiesByClass(EnderPearl.class)) {
            if (pearl.getShooter() != player) continue;
            pearl.remove();
        }
        this.pearlCooldown.remove(player.getUniqueId());
    }

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        for (MatchTeam team : event.getMatch().getTeams()) {
            team.getAliveMembers().forEach(this.pearlCooldown::remove);
        }
    }

    @EventHandler
    public void onMatchCountdownStart(MatchCountdownStartEvent event) {
        for (MatchTeam team : event.getMatch().getTeams()) {
            team.getAllMembers().forEach(this.pearlCooldown::remove);
        }
    }
}

