/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MatchMLGListener
implements Listener {
    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        int bedBrokenTeam;
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
        if (match.getKitType().getId().equals("MLGRush") && block.getType() == Material.BED_BLOCK && (bedBrokenTeam = this.getMatchTeam(match, block)) == oppositeTeam.getId()) {
            match.getArena().restore();
            team.incrementPoints();
            if (team.getPoints() >= 5) {
                oppositeTeam.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(teamPlayer -> match.markDead((Player)teamPlayer, false));
            } else {
                match.getTeams().forEach(matchTeam -> matchTeam.getAliveMembers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(teamPlayer -> match.markDead((Player)teamPlayer, true)));
            }
        }
    }

    private int getMatchTeam(Match match, Block bedBlock) {
        Location bedLocation = bedBlock.getLocation();
        Location team0Spawn = match.getArena().getTeam1Spawn();
        Location team1Spawn = match.getArena().getTeam2Spawn();
        if (team1Spawn.distanceSquared(bedLocation) < team0Spawn.distanceSquared(bedLocation)) {
            return 1;
        }
        return 0;
    }
}

