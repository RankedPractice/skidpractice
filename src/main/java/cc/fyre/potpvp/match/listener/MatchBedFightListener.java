/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.lunarclient.bukkitapi.LunarClientAPI
 *  com.lunarclient.bukkitapi.object.TitleType
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.util.CC;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.TitleType;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class MatchBedFightListener
implements Listener {
    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Block block = event.getBlock();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        MatchTeam team = match.getTeam(player.getUniqueId());
        if (team == null) {
            return;
        }
        MatchTeam oppositeTeam = match.getTeam(team.getId() == 0 ? 1 : 0);
        if (oppositeTeam == null) {
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("BEDFIGHT") && block.getType() == Material.BED_BLOCK) {
            int bedBrokenTeam = this.getMatchTeam(match, block);
            if (bedBrokenTeam == oppositeTeam.getId()) {
                oppositeTeam.setBedBroken(true);
                for (UUID allMember : oppositeTeam.getAllMembers()) {
                    Player member = Bukkit.getPlayer(allMember);
                    if (member == null) continue;
                    LunarClientAPI.getInstance().sendTitle(member, TitleType.TITLE, CC.translate("&cBed Broken"), Duration.of(2L, ChronoUnit.SECONDS));
                    LunarClientAPI.getInstance().sendTitle(member, TitleType.SUBTITLE, CC.translate("&7You will no longer respawn"), Duration.of(2L, ChronoUnit.SECONDS));
                    member.playSound(member.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 2.0f);
                }
                event.getBlock().setType(Material.AIR);
            } else {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou can't break that bed."));
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (event.getEntity().getKiller() != null) {
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            Match match = matchHandler.getMatchPlaying(player);
            if (match == null) {
                return;
            }
            if (!match.getKitType().getId().equalsIgnoreCase("bedfight")) {
                return;
            }
            match.getKills().put(player.getKiller().getUniqueId(), match.getKills().get(player.getKiller().getUniqueId()) + 1);
            match.markDead(player);
            event.getDrops().clear();
            player.spigot().respawn();
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasMetadata("respawning")) {
            event.setCancelled(true);
        }
    }

    private int getMatchTeam(Match match, Block bedBlock) {
        Location bedLocation = bedBlock.getLocation();
        Location team0Spawn = match.getArena().getBlueSpawnSpawn();
        Location team1Spawn = match.getArena().getRedSpawn();
        if (team1Spawn.distanceSquared(bedLocation) < team0Spawn.distanceSquared(bedLocation)) {
            return 1;
        }
        return 0;
    }
}

