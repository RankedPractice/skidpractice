/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MatchBoxingListener
implements Listener {
    @EventHandler
    public void onStartBoxing(MatchStartEvent event) {
        Match match = event.getMatch();
        if (match.getKitType().getId().equals("Boxing")) {
            match.getTeams().forEach(team -> team.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200000, 1), true)));
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        Player damager = (Player)event.getDamager();
        Player player = (Player)event.getEntity();
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        if (match == null || match.getTeam(damager.getUniqueId()) == null) {
            return;
        }
        if (match.getKitType().getId().equals("Boxing") && (match.getState() != MatchState.ENDING || !match.isSpectator(damager.getUniqueId()))) {
            event.setDamage(0.0);
            match.getBoxingHits().put(damager.getUniqueId(), match.getBoxingHits().getOrDefault(damager.getUniqueId(), 0) + 1);
            match.checkBoxingHits();
        }
    }
}

