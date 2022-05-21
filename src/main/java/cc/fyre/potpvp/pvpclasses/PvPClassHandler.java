/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemDamageEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.pvpclasses;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.pvpclasses.PvPClass;
import cc.fyre.potpvp.pvpclasses.event.BardRestoreEvent;
import cc.fyre.potpvp.pvpclasses.pvpclasses.ArcherClass;
import cc.fyre.potpvp.pvpclasses.pvpclasses.BardClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class PvPClassHandler
extends BukkitRunnable
implements Listener {
    private static Map<String, PvPClass> equippedKits = new HashMap<String, PvPClass>();
    private static Map<UUID, PvPClass.SavedPotion> savedPotions = new HashMap<UUID, PvPClass.SavedPotion>();
    List<PvPClass> pvpClasses = new ArrayList<PvPClass>();

    public PvPClassHandler() {
        this.pvpClasses.add(new ArcherClass());
        this.pvpClasses.add(new BardClass());
        for (PvPClass pvpClass : this.pvpClasses) {
            PotPvP.getInstance().getServer().getPluginManager().registerEvents((Listener)pvpClass, (Plugin)PotPvP.getInstance());
        }
        PotPvP.getInstance().getServer().getScheduler().runTaskTimer((Plugin)PotPvP.getInstance(), (BukkitRunnable)this, 2L, 2L);
        PotPvP.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)PotPvP.getInstance());
    }

    public void run() {
        block0: for (Player player : PotPvP.getInstance().getServer().getOnlinePlayers()) {
            if (equippedKits.containsKey(player.getName())) {
                PvPClass equippedPvPClass = equippedKits.get(player.getName());
                if (!equippedPvPClass.qualifies(player.getInventory())) {
                    equippedKits.remove(player.getName());
                    player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.BOLD + equippedPvPClass.getName() + ChatColor.GRAY + " --> " + ChatColor.RED + "Disabled!");
                    equippedPvPClass.remove(player);
                    PvPClass.removeInfiniteEffects(player);
                    continue;
                }
                if (player.hasMetadata("frozen")) continue;
                equippedPvPClass.tick(player);
                continue;
            }
            Match match = PotPvP.getInstance().getMatchHandler().getMatchPlayingOrSpectating(player);
            if (match == null || !match.getKitType().equals(KitType.teamFight)) continue;
            for (PvPClass pvpClass : this.pvpClasses) {
                if (!pvpClass.qualifies(player.getInventory()) || !pvpClass.canApply(player) || player.hasMetadata("frozen")) continue;
                pvpClass.apply(player);
                PvPClassHandler.getEquippedKits().put(player.getName(), pvpClass);
                player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.BOLD + pvpClass.getName() + ChatColor.GRAY + " --> " + ChatColor.GREEN + "Enabled!");
                continue block0;
            }
        }
        this.checkSavedPotions();
    }

    public void checkSavedPotions() {
        Iterator<Map.Entry<UUID, PvPClass.SavedPotion>> idIterator = savedPotions.entrySet().iterator();
        while (idIterator.hasNext()) {
            Map.Entry<UUID, PvPClass.SavedPotion> id = idIterator.next();
            Player player = Bukkit.getPlayer((UUID)id.getKey());
            if (player != null && player.isOnline()) {
                Bukkit.getPluginManager().callEvent((Event)new BardRestoreEvent(player, id.getValue()));
                if (id.getValue().getTime() >= System.currentTimeMillis() || id.getValue().isPerm()) continue;
                if (player.hasPotionEffect(id.getValue().getPotionEffect().getType())) {
                    player.getActivePotionEffects().forEach(potion -> {
                        PotionEffect restore = ((PvPClass.SavedPotion)id.getValue()).getPotionEffect();
                        if (potion.getType() == restore.getType() && potion.getDuration() < restore.getDuration() && potion.getAmplifier() <= restore.getAmplifier()) {
                            player.removePotionEffect(restore.getType());
                        }
                    });
                }
                if (!player.addPotionEffect(id.getValue().getPotionEffect(), true)) continue;
                Bukkit.getLogger().info(id.getValue().getPotionEffect().getType() + ", " + id.getValue().getPotionEffect().getDuration() + ", " + id.getValue().getPotionEffect().getAmplifier());
                idIterator.remove();
                continue;
            }
            idIterator.remove();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() == null || event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        for (PvPClass pvPClass : this.pvpClasses) {
            if (!PvPClassHandler.hasKitOn(event.getPlayer(), pvPClass) || pvPClass.getConsumables() == null || !pvPClass.getConsumables().contains(event.getPlayer().getItemInHand().getType()) || !pvPClass.itemConsumed(event.getPlayer(), event.getItem().getType())) continue;
            if (event.getPlayer().getItemInHand().getAmount() > 1) {
                event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                continue;
            }
            event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
        }
    }

    public static PvPClass getPvPClass(Player player) {
        return equippedKits.containsKey(player.getName()) ? equippedKits.get(player.getName()) : null;
    }

    public static boolean hasKitOn(Player player, PvPClass pvpClass) {
        return equippedKits.containsKey(player.getName()) && equippedKits.get(player.getName()) == pvpClass;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (equippedKits.containsKey(event.getPlayer().getName())) {
            equippedKits.get(event.getPlayer().getName()).remove(event.getPlayer());
            equippedKits.remove(event.getPlayer().getName());
        }
        for (PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
            if (potionEffect.getDuration() <= 1000000) continue;
            event.getPlayer().removePotionEffect(potionEffect.getType());
        }
    }

    @EventHandler
    public void onPlayerDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        PvPClass kit = equippedKits.get(player.getName());
        if (kit != null && kit.getName().equalsIgnoreCase("bard") && Arrays.asList(player.getInventory().getArmorContents()).contains(event.getItem()) && new Random().nextBoolean()) {
            event.setCancelled(true);
        }
    }

    public static Map<String, PvPClass> getEquippedKits() {
        return equippedKits;
    }

    public static Map<UUID, PvPClass.SavedPotion> getSavedPotions() {
        return savedPotions;
    }

    public List<PvPClass> getPvpClasses() {
        return this.pvpClasses;
    }
}

