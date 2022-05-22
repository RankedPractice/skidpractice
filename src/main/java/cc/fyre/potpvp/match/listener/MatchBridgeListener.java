/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBurnEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerItemDamageEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.BridgeEnterLavaPortalEvent;
import cc.fyre.potpvp.match.event.BridgeEnterWaterPortalEvent;
import cc.fyre.potpvp.util.BridgeUtil;
import cc.fyre.potpvp.util.CC;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchBridgeListener
implements Listener {
    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(player);
        if (match.getKitType().getId().equalsIgnoreCase("Bridges") && event.getItem() != null && event.getItem().getType() == Material.GOLDEN_APPLE) {
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
        }
    }

    @EventHandler
    public void onItemDmg(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlaying(player);
        if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            event.setCancelled(true);
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
            if (!match.getKitType().getId().equalsIgnoreCase("Bridges")) {
                return;
            }
            match.getKills().put(player.getKiller().getUniqueId(), match.getKills().get(player.getKiller().getUniqueId()) + 1);
            player.spigot().respawn();
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        event.setRadius(0.0f);
    }

    @EventHandler
    public void onBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPortalEnter(BridgeEnterLavaPortalEvent event) {
        final Match match = event.getMatch();
        final Player player = event.getPlayer();
        if (match.getWinner() != null) {
            return;
        }
        MatchTeam team = match.getTeams().get(1);
        if (team != null) {
            if (team.getAllMembers().contains(player.getUniqueId())) {
                player.sendMessage(CC.translate("&cYou cannot enter your own portal."));
                player.teleport(match.getArena().getTeam2Spawn());
                return;
            }
            match.getUsedKit().getOrDefault(player.getUniqueId(), Kit.ofDefaultKit(match.getKitType())).apply(player);
            player.teleport(match.getArena().getTeam1Spawn());
            new BukkitRunnable(){

                public void run() {
                    match.getWins().put(match.getTeam(player.getUniqueId()), match.getWins().get(match.getTeam(player.getUniqueId())) + 1);
                    if (match.getWins().get(match.getTeam(player.getUniqueId())) < 3) {
                        for (MatchTeam matchTeam : match.getTeams()) {
                            for (UUID allMember : matchTeam.getAllMembers()) {
                                final Player p = Bukkit.getPlayer((UUID)allMember);
                                Location location = match.getTeams().get(0).getAllMembers().contains(p.getUniqueId()) ? match.getArena().getTeam1Spawn() : match.getArena().getTeam2Spawn();
                                p.teleport(location);
                                p.sendMessage(CC.translate("&9" + player.getName() + " &fhas just scored."));
                                p.setMetadata("waiting", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
                                if (match.getUsedKit().get(p.getUniqueId()) != null) {
                                    match.getUsedKit().get(p.getUniqueId()).apply(p);
                                }
                                p.setHealth(p.getMaxHealth());
                                p.updateInventory();
                                new BukkitRunnable(){

                                    public void run() {
                                        match.playSoundAll(Sound.NOTE_PLING, 1.5f);
                                        p.removeMetadata("waiting", (Plugin)PotPvP.getInstance());
                                    }
                                }.runTaskLater((Plugin)PotPvP.getInstance(), 60L);
                            }
                        }
                    }
                    match.checkEnded();
                }
            }.runTaskLater((Plugin)PotPvP.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            Match match = matchHandler.getMatchPlayingOrSpectating(player);
            if (match == null) {
                return;
            }
            if (!match.getKitType().getId().equalsIgnoreCase("Bridges")) {
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSaturationLose(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            List<String> modes;
            Player player = (Player)event.getEntity();
            Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
            if (match != null && (modes = Arrays.asList("Boxing", "Sumo", "wizard", "PearlFight", "Bridges", "Archer", "skywars", "combo")).contains(match.getKitType().getId())) {
                player.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(event.getPlayer());
        if (match != null && match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            for (MatchTeam team : match.getTeams()) {
                for (UUID member : team.getAllMembers()) {
                    if (member == event.getPlayer().getUniqueId()) continue;
                    match.getWins().put(team, 5);
                    match.checkEnded();
                }
            }
        }
    }

    @EventHandler
    public void onPortalEnter(BridgeEnterWaterPortalEvent event) {
        final Match match = event.getMatch();
        final Player player = event.getPlayer();
        if (match.getWinner() != null) {
            return;
        }
        MatchTeam team = match.getTeams().get(0);
        if (team != null) {
            if (team.getAllMembers().contains(player.getUniqueId())) {
                player.sendMessage(CC.translate("&cYou cannot enter your own portal."));
                player.teleport(match.getArena().getTeam1Spawn());
                return;
            }
            match.getUsedKit().getOrDefault(player.getUniqueId(), Kit.ofDefaultKit(match.getKitType())).apply(player);
            player.teleport(match.getArena().getTeam2Spawn());
            new BukkitRunnable(){

                public void run() {
                    match.getWins().put(match.getTeam(player.getUniqueId()), match.getWins().get(match.getTeam(player.getUniqueId())) + 1);
                    if (match.getWins().get(match.getTeam(player.getUniqueId())) < 5) {
                        for (MatchTeam matchTeam : match.getTeams()) {
                            for (UUID allMember : matchTeam.getAllMembers()) {
                                final Player p = Bukkit.getPlayer((UUID)allMember);
                                Location location = match.getTeams().get(0).getAllMembers().contains(p.getUniqueId()) ? match.getArena().getTeam1Spawn() : match.getArena().getTeam2Spawn();
                                p.teleport(location);
                                p.sendMessage(CC.translate("&7&m------------------"));
                                p.sendMessage(CC.translate("&6" + player.getName() + " &fhas just scored."));
                                p.sendMessage(CC.translate(" "));
                                p.sendMessage(CC.translate(BridgeUtil.barBuilder(match.getWins().get(match.getTeams().get(0)), "&6")));
                                p.sendMessage(CC.translate(BridgeUtil.barBuilder(match.getWins().get(match.getTeams().get(1)), "&6")));
                                p.sendMessage(CC.translate("&7&m------------------"));
                                p.setMetadata("waiting", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
                                match.getUsedKit().getOrDefault(p.getUniqueId(), Kit.ofDefaultKit(match.getKitType())).apply(p);
                                p.setHealth(p.getMaxHealth());
                                p.updateInventory();
                                new BukkitRunnable(){

                                    public void run() {
                                        match.playSoundAll(Sound.NOTE_PLING, 1.5f);
                                        p.removeMetadata("waiting", (Plugin)PotPvP.getInstance());
                                    }
                                }.runTaskLater((Plugin)PotPvP.getInstance(), 60L);
                            }
                        }
                    }
                    match.checkEnded();
                }
            }.runTaskLater((Plugin)PotPvP.getInstance(), 10L);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().hasMetadata("waiting")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            Match match = matchHandler.getMatchPlaying(player);
            if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
                new BukkitRunnable(){

                    public void run() {
                        if (!PotPvP.getInstance().getMatchHandler().isPlayingMatch(player)) {
                            return;
                        }
                        player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.ARROW)});
                        player.sendMessage(CC.translate("&aYou can now use your bow again."));
                    }
                }.runTaskLater((Plugin)PotPvP.getInstance(), 100L);
            }
        }
    }
}

