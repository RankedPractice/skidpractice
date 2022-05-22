/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockFormEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.cuboid.Cuboid
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.cuboid.Cuboid;

public final class MatchBuildListener
implements Listener {
    private static final int SEARCH_RADIUS = 3;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        if (!match.getKitType().isBuildingAllowed() || match.getState() != MatchState.IN_PROGRESS) {
            event.setCancelled(true);
        } else {
            MatchTeam team;
            if (match.getKitType().getId().equalsIgnoreCase("BedFight") && (team = match.getTeam(player.getUniqueId())) != null && team.getSpawnLoc().distance(event.getBlock().getLocation()) <= 7.0 && event.getBlock().getLocation().getBlockY() >= team.getSpawnLoc().getBlockY()) {
                return;
            }
            if (!match.canBeBroken(event.getBlock())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        if (!match.getKitType().isBuildingAllowed()) {
            event.setCancelled(true);
            return;
        }
        if (match.getState() != MatchState.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("PearlFight")) {
            new BukkitRunnable(){

                public void run() {
                    event.getBlockPlaced().setType(Material.AIR);
                }
            }.runTaskLater((Plugin)PotPvP.getInstance(), 100L);
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("Bridges") || match.getKitType().getId().equalsIgnoreCase("BedFight")) {
            if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
                int i;
                if (event.getBlock().getType() == Material.STATIONARY_LAVA || event.getBlock().getType() == Material.STATIONARY_LAVA) {
                    event.setCancelled(true);
                    return;
                }
                if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.STATIONARY_WATER) {
                    event.setCancelled(true);
                    return;
                }
                for (i = 0; i < 5; ++i) {
                    if (!event.getBlockPlaced().getLocation().subtract(0.0, (double)(1 + i), 0.0).getBlock().getType().name().contains("LAVA")) continue;
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou cannot place blocks near portals."));
                    break;
                }
                for (i = 0; i < 5; ++i) {
                    if (!event.getBlockPlaced().getLocation().subtract(0.0, (double)(1 + i), 0.0).getBlock().getType().name().contains("WATER")) continue;
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou cannot place blocks near portals."));
                    break;
                }
                if (match.getArena().getTeam1Spawn().distance(event.getBlockPlaced().getLocation()) < 10.0) {
                    event.setCancelled(true);
                    return;
                }
                if (match.getArena().getTeam2Spawn().distance(event.getBlockPlaced().getLocation()) < 10.0) {
                    event.setCancelled(true);
                    return;
                }
                if ((double)event.getBlockPlaced().getLocation().getBlockY() >= match.getArena().getSpectatorSpawn().getY() + 10.0) {
                    event.setCancelled(true);
                    return;
                }
                return;
            }
            if ((double)event.getBlockPlaced().getLocation().getBlockY() >= match.getArena().getSpectatorSpawn().getY() + 10.0) {
                event.setCancelled(true);
                return;
            }
        }
        if (!this.canBePlaced(event.getBlock(), match)) {
            player.sendMessage(ChatColor.RED + "You can't build here.");
            event.setCancelled(true);
            player.teleport(player.getLocation());
            return;
        }
        if (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL && event.getBlockAgainst().getType() == Material.GLASS) {
            event.setCancelled(true);
            return;
        }
        match.recordPlacedBlock(event.getBlock());
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        if (!match.getKitType().isBuildingAllowed() || match.getState() != MatchState.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }
        if (!this.canBePlaced(event.getBlockClicked(), match)) {
            player.sendMessage(ChatColor.RED + "You can't build here.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        for (Match match : matchHandler.getHostedMatches()) {
            if (!match.getArena().getBounds().contains(event.getBlock()) || !match.getKitType().isBuildingAllowed()) continue;
            match.recordPlacedBlock(event.getBlock());
            break;
        }
    }

    private boolean canBePlaced(Block placedBlock, Match match) {
        if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            return true;
        }
        if (match.getKitType().getId().equalsIgnoreCase("BedFight")) {
            return true;
        }
        for (int x = -3; x <= 3; ++x) {
            for (int y = -3; y <= 3; ++y) {
                for (int z = -3; z <= 3; ++z) {
                    Block current;
                    if (x == 0 && y == 0 && z == 0 || (current = placedBlock.getRelative(x, y, z)).isEmpty() || this.isBlacklistedBlock(current) || this.isBorderGlass(current, match) || match.canBeBroken(current)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBlacklistedBlock(Block block) {
        return block.isLiquid() || block.getType().name().contains("LOG") || block.getType().name().contains("LEAVES");
    }

    private boolean isBorderGlass(Block block, Match match) {
        if (block.getType() != Material.GLASS) {
            return false;
        }
        Cuboid cuboid = match.getArena().getBounds();
        return this.getDistanceBetween(block.getX(), cuboid.getLowerX()) <= 3 || this.getDistanceBetween(block.getX(), cuboid.getUpperX()) <= 3 || this.getDistanceBetween(block.getZ(), cuboid.getLowerZ()) <= 3 || this.getDistanceBetween(block.getZ(), cuboid.getUpperZ()) <= 3;
    }

    private int getDistanceBetween(int x, int z) {
        return Math.abs(x - z);
    }
}

