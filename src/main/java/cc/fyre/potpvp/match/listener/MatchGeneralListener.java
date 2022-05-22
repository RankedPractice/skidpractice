/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Snowball
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.cuboid.Cuboid
 *  rip.bridge.qlib.util.PlayerUtils
 *  rip.bridge.util.BlockUtil
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.BridgeEnterLavaPortalEvent;
import cc.fyre.potpvp.match.event.BridgeEnterWaterPortalEvent;
import cc.fyre.potpvp.match.event.MatchEndEvent;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import cc.fyre.potpvp.nametag.PotPvPNametagProvider;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.cuboid.Cuboid;
import rip.bridge.qlib.util.PlayerUtils;
import rip.bridge.util.BlockUtil;

public final class MatchGeneralListener
implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player;
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(player = event.getEntity());
        if (match == null) {
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("BedFight")) {
            return;
        }
        match.markDead(player);
        player.teleport(player.getLocation().add(0.0, 2.0, 0.0));
        if (match.getState() == MatchState.ENDING) {
            event.getDrops().removeIf(i -> i.getType() == Material.POTION || i.getType() == Material.GLASS_BOTTLE || i.getType() == Material.MUSHROOM_SOUP || i.getType() == Material.BOWL);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player;
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(player = event.getPlayer());
        if (match == null) {
            return;
        }
        MatchState state = match.getState();
        if (state == MatchState.COUNTDOWN || state == MatchState.IN_PROGRESS) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                UUID onlinePlayerUuid = onlinePlayer.getUniqueId();
                if (match.getTeam(onlinePlayerUuid) == null && !match.isSpectator(onlinePlayerUuid)) continue;
                String playerColor = PotPvPNametagProvider.getNameColor(player, onlinePlayer);
                String playerFormatted = playerColor + player.getName();
                onlinePlayer.sendMessage(playerFormatted + ChatColor.GRAY + " disconnected.");
            }
        }
        match.markDead(player);
        MatchTeam team = match.getTeam(player.getUniqueId());
        if (team != null) {
            team.markDead(player.getUniqueId());
        }
        match.checkEnded();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        switch (event.getCause()) {
            case PLUGIN: 
            case COMMAND: 
            case UNKNOWN: {
                return;
            }
        }
        this.onPlayerMove((PlayerMoveEvent)event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        final Match match = matchHandler.getMatchPlayingOrSpectating(player);
        if (match == null) {
            return;
        }
        Arena arena = match.getArena();
        Cuboid bounds = arena.getBounds();
        if (match.getState() == MatchState.IN_PROGRESS && !match.isSpectator(player.getUniqueId()) && (match.getKitType().getId().equals("Sumo") || match.getKitType().getId().equals("Spleef")) && BlockUtil.isOnLiquid((Location)to, (int)0)) {
            player.damage(player.getHealth() + 20.0);
        }
        if (event.getTo().getY() < match.getArena().getTeam1Spawn().getY() - 18.0 && match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            match.markDead(player);
            return;
        }
        if ((event.getTo().getBlock().getType() == Material.LAVA || event.getTo().getBlock().getType() == Material.STATIONARY_LAVA) && match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            BridgeEnterWaterPortalEvent bridgeEnterWaterPortalEvent = new BridgeEnterWaterPortalEvent(player, match);
            Bukkit.getPluginManager().callEvent((Event)bridgeEnterWaterPortalEvent);
            return;
        }
        if ((event.getTo().getBlock().getType() == Material.WATER || event.getTo().getBlock().getType() == Material.STATIONARY_WATER) && match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            BridgeEnterLavaPortalEvent bridgeEnterLavaPortalEvent = new BridgeEnterLavaPortalEvent(player, match);
            Bukkit.getPluginManager().callEvent((Event)bridgeEnterLavaPortalEvent);
            return;
        }
        if (match.getKitType().getId().equalsIgnoreCase("PearlFight")) {
            if (player.getLocation().getY() < match.getArena().getTeam1Spawn().getY() - 10.0) {
                player.teleport(match.getArena().getSpectatorSpawn());
                new BukkitRunnable(){

                    public void run() {
                        match.markDead(player);
                    }
                }.runTaskLater((Plugin)PotPvP.getInstance(), 10L);
            }
            return;
        }
        if (!bounds.contains(to) || !bounds.contains(to.getBlockX(), to.getBlockY() + 2, to.getBlockZ())) {
            if (match.isSpectator(player.getUniqueId())) {
                player.teleport(arena.getSpectatorSpawn());
            } else if (match.getKitType().getId().equals("MLGRush")) {
                match.markDead(player, true);
            } else if (match.getKitType().getId().equals("BedFight")) {
                match.markDead(player);
            } else if (to.getBlockY() >= bounds.getUpperY() || to.getBlockY() <= bounds.getLowerY()) {
                player.teleport(arena.getSpectatorSpawn());
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        boolean isSumo;
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Player victim = (Player)event.getEntity();
        Player damager = PlayerUtils.getDamageSource((Entity)event.getDamager());
        if (damager == null) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(damager);
        boolean isSpleef = match != null && match.getKitType().getId().equals("Spleef");
        boolean bl = isSumo = match != null && match.getKitType().getId().equals("Sumo");
        if (match != null) {
            MatchTeam victimTeam = match.getTeam(victim.getUniqueId());
            MatchTeam damagerTeam = match.getTeam(damager.getUniqueId());
            if (isSpleef && event.getDamager() instanceof Snowball) {
                return;
            }
            if (isSumo && victimTeam != null && victimTeam != damagerTeam) {
                event.setDamage(0.0);
                return;
            }
            if (victimTeam != null && victimTeam != damagerTeam && !isSpleef) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player;
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player = event.getPlayer())) {
            return;
        }
        ItemStack itemStack = event.getItemDrop().getItemStack();
        Material itemType = itemStack.getType();
        String itemTypeName = itemType.name().toLowerCase();
        int heldSlot = player.getInventory().getHeldItemSlot();
        if (!PlayerUtils.hasOtherInventoryOpen((Player)player) && heldSlot == 0 && (itemTypeName.contains("sword") || itemTypeName.contains("axe") || itemType == Material.BOW)) {
            player.sendMessage(ChatColor.RED + "You can't drop that while you're holding it in slot 1.");
            event.setCancelled(true);
        }
        if (itemType == Material.GLASS_BOTTLE || itemType == Material.BOWL) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player;
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player = event.getPlayer())) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(event.getPlayer());
        if (match == null) {
            return;
        }
        if (match.getState() == MatchState.ENDING || match.getState() == MatchState.TERMINATED) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack stack = event.getItem();
        if (stack == null || stack.getType() != Material.POTION) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> {}, 1L);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {

    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
    }
}

