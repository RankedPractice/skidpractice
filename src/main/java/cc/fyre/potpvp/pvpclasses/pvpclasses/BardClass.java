/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.pvpclasses.pvpclasses;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.pvpclasses.PvPClass;
import cc.fyre.potpvp.pvpclasses.PvPClassHandler;
import cc.fyre.potpvp.pvpclasses.pvpclasses.bard.BardEffect;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BardClass
extends PvPClass
implements Listener {
    public final Map<Material, BardEffect> BARD_CLICK_EFFECTS = new HashMap<Material, BardEffect>();
    public final Map<Material, BardEffect> BARD_PASSIVE_EFFECTS = new HashMap<Material, BardEffect>();
    private static Map<String, Long> lastEffectUsage = new ConcurrentHashMap<String, Long>();
    private static Map<String, Float> energy = new ConcurrentHashMap<String, Float>();
    private static final Set<PotionEffectType> DEBUFFS = ImmutableSet.of((Object)PotionEffectType.POISON, (Object)PotionEffectType.SLOW, (Object)PotionEffectType.WEAKNESS, (Object)PotionEffectType.HARM, (Object)PotionEffectType.WITHER);
    public static final int BARD_RANGE = 20;
    public static final int EFFECT_COOLDOWN = 10000;
    public static final float MAX_ENERGY = 100.0f;
    public static final float ENERGY_REGEN_PER_SECOND = 1.0f;

    public BardClass() {
        super("Bard", 15, "GOLD_", null);
        this.BARD_CLICK_EFFECTS.put(Material.BLAZE_POWDER, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1), 45));
        this.BARD_CLICK_EFFECTS.put(Material.SUGAR, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.SPEED, 120, 2), 20));
        this.BARD_CLICK_EFFECTS.put(Material.FEATHER, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.JUMP, 100, 6), 25));
        this.BARD_CLICK_EFFECTS.put(Material.IRON_INGOT, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 2), 40));
        this.BARD_CLICK_EFFECTS.put(Material.GHAST_TEAR, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.REGENERATION, 100, 2), 40));
        this.BARD_CLICK_EFFECTS.put(Material.MAGMA_CREAM, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 900, 0), 40));
        this.BARD_CLICK_EFFECTS.put(Material.WHEAT, BardEffect.fromEnergy(25));
        this.BARD_CLICK_EFFECTS.put(Material.SPIDER_EYE, BardEffect.fromPotionAndEnergy(new PotionEffect(PotionEffectType.WITHER, 100, 1), 35));
        this.BARD_PASSIVE_EFFECTS.put(Material.BLAZE_POWDER, BardEffect.fromPotion(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0)));
        this.BARD_PASSIVE_EFFECTS.put(Material.SUGAR, BardEffect.fromPotion(new PotionEffect(PotionEffectType.SPEED, 120, 1)));
        this.BARD_PASSIVE_EFFECTS.put(Material.FEATHER, BardEffect.fromPotion(new PotionEffect(PotionEffectType.JUMP, 120, 1)));
        this.BARD_PASSIVE_EFFECTS.put(Material.IRON_INGOT, BardEffect.fromPotion(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 0)));
        this.BARD_PASSIVE_EFFECTS.put(Material.GHAST_TEAR, BardEffect.fromPotion(new PotionEffect(PotionEffectType.REGENERATION, 120, 0)));
        this.BARD_PASSIVE_EFFECTS.put(Material.MAGMA_CREAM, BardEffect.fromPotion(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0)));
        new BukkitRunnable(){

            public void run() {
                for (Player player : PotPvP.getInstance().getServer().getOnlinePlayers()) {
                    int manaInt;
                    if (!PvPClassHandler.hasKitOn(player, BardClass.this)) continue;
                    if (energy.containsKey(player.getName())) {
                        if (((Float)energy.get(player.getName())).floatValue() == 100.0f) continue;
                        energy.put(player.getName(), Float.valueOf(Math.min(100.0f, ((Float)energy.get(player.getName())).floatValue() + 1.0f)));
                    } else {
                        energy.put(player.getName(), Float.valueOf(0.0f));
                    }
                    if ((manaInt = ((Float)energy.get(player.getName())).intValue()) % 10 != 0) continue;
                    player.sendMessage(ChatColor.AQUA + "Bard Energy: " + ChatColor.GREEN + manaInt);
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 15L, 20L);
    }

    @Override
    public void apply(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0), true);
    }

    @Override
    public void tick(Player player) {
        if (!this.qualifies(player.getInventory())) {
            super.tick(player);
            return;
        }
        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }
        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        }
        if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        }
        if (player.getItemInHand() != null && this.BARD_PASSIVE_EFFECTS.containsKey(player.getItemInHand().getType())) {
            if (player.getItemInHand().getType() == Material.FERMENTED_SPIDER_EYE && BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) > System.currentTimeMillis()) {
                return;
            }
            this.giveBardEffect(player, this.BARD_PASSIVE_EFFECTS.get(player.getItemInHand().getType()), true, false);
        }
        super.tick(player);
    }

    @Override
    public void remove(Player player) {
        energy.remove(player.getName());
        for (BardEffect bardEffect : this.BARD_CLICK_EFFECTS.values()) {
            bardEffect.getLastMessageSent().remove(player.getName());
        }
        for (BardEffect bardEffect : this.BARD_CLICK_EFFECTS.values()) {
            bardEffect.getLastMessageSent().remove(player.getName());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!(event.getAction().name().contains("RIGHT_") && event.hasItem() && this.BARD_CLICK_EFFECTS.containsKey(event.getItem().getType()) && PvPClassHandler.hasKitOn(event.getPlayer(), this) && energy.containsKey(event.getPlayer().getName()))) {
            return;
        }
        if (BardClass.getLastEffectUsage().containsKey(event.getPlayer().getName()) && BardClass.getLastEffectUsage().get(event.getPlayer().getName()) > System.currentTimeMillis() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            long millisLeft = BardClass.getLastEffectUsage().get(event.getPlayer().getName()) - System.currentTimeMillis();
            double value = (double)millisLeft / 1000.0;
            double sec = (double)Math.round(10.0 * value) / 10.0;
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
            return;
        }
        BardEffect bardEffect = this.BARD_CLICK_EFFECTS.get(event.getItem().getType());
        if ((float)bardEffect.getEnergy() > energy.get(event.getPlayer().getName()).floatValue()) {
            event.getPlayer().sendMessage(ChatColor.RED + "You do not have enough energy for this! You need " + bardEffect.getEnergy() + " energy, but you only have " + energy.get(event.getPlayer().getName()).intValue());
            return;
        }
        energy.put(event.getPlayer().getName(), Float.valueOf(energy.get(event.getPlayer().getName()).floatValue() - (float)bardEffect.getEnergy()));
        boolean negative = bardEffect.getPotionEffect() != null && DEBUFFS.contains(bardEffect.getPotionEffect().getType());
        BardClass.getLastEffectUsage().put(event.getPlayer().getName(), System.currentTimeMillis() + 10000L);
        this.giveBardEffect(event.getPlayer(), bardEffect, !negative, true);
        if (event.getPlayer().getItemInHand().getAmount() == 1) {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.getPlayer().updateInventory();
        } else {
            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
        }
    }

    public void giveBardEffect(Player source, BardEffect bardEffect, boolean friendly, boolean persistOldValues) {
        for (Player player : this.getNearbyPlayers(source, friendly)) {
            if (PvPClassHandler.hasKitOn(player, this) && bardEffect.getPotionEffect() != null && bardEffect.getPotionEffect().getType().equals((Object)PotionEffectType.INCREASE_DAMAGE)) continue;
            if (bardEffect.getPotionEffect() != null) {
                BardClass.smartAddPotion(player, bardEffect.getPotionEffect(), persistOldValues, this);
                continue;
            }
            Material material = source.getItemInHand().getType();
            this.giveCustomBardEffect(player, material);
        }
    }

    public void giveCustomBardEffect(Player player, Material material) {
        switch (material) {
            case WHEAT: {
                for (Player nearbyPlayer : this.getNearbyPlayers(player, true)) {
                    nearbyPlayer.setFoodLevel(20);
                    nearbyPlayer.setSaturation(10.0f);
                }
                break;
            }
            case FERMENTED_SPIDER_EYE: {
                break;
            }
            default: {
                PotPvP.getInstance().getLogger().warning("No custom Bard effect defined for " + material + ".");
            }
        }
    }

    public List<Player> getNearbyPlayers(Player player, boolean friendly) {
        ArrayList<Player> valid = new ArrayList<Player>();
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        MatchTeam sourceTeam = match.getTeam(player.getUniqueId());
        for (Entity entity : player.getNearbyEntities(20.0, 10.0, 20.0)) {
            if (!(entity instanceof Player)) continue;
            Player nearbyPlayer = (Player)entity;
            if (sourceTeam == null) {
                if (friendly) continue;
                valid.add(nearbyPlayer);
                continue;
            }
            boolean isFriendly = sourceTeam.getAliveMembers().contains(nearbyPlayer.getUniqueId());
            if (friendly && isFriendly) {
                valid.add(nearbyPlayer);
                continue;
            }
            if (friendly || isFriendly) continue;
            valid.add(nearbyPlayer);
        }
        valid.add(player);
        return valid;
    }

    public static Map<String, Long> getLastEffectUsage() {
        return lastEffectUsage;
    }

    public static Map<String, Float> getEnergy() {
        return energy;
    }
}

