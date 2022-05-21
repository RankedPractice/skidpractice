/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PotionEffectExpireEvent
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package cc.fyre.potpvp.pvpclasses;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionEffectExpireEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class PvPClass
implements Listener {
    String name;
    String siteLink;
    int warmup;
    String armorContains;
    List<Material> consumables;
    private static final Table<UUID, PotionEffectType, PotionEffect> restores = HashBasedTable.create();

    public PvPClass(String name, int warmup, String armorContains, List<Material> consumables) {
        this.name = name;
        this.siteLink = "forums.lyra.gg/" + name.toLowerCase().replaceAll(" ", "-");
        this.warmup = warmup;
        this.armorContains = armorContains;
        this.consumables = consumables;
        this.warmup = 5;
    }

    public void apply(Player player) {
    }

    public void tick(Player player) {
    }

    public void remove(Player player) {
    }

    public boolean canApply(Player player) {
        return true;
    }

    public static void removeInfiniteEffects(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getDuration() <= 1000000) continue;
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public boolean itemConsumed(Player player, Material type2) {
        return true;
    }

    public boolean qualifies(PlayerInventory armor) {
        return armor.getHelmet() != null && armor.getChestplate() != null && armor.getLeggings() != null && armor.getBoots() != null && armor.getHelmet().getType().name().startsWith(this.armorContains) && armor.getChestplate().getType().name().startsWith(this.armorContains) && armor.getLeggings().getType().name().startsWith(this.armorContains) && armor.getBoots().getType().name().startsWith(this.armorContains);
    }

    public static void smartAddPotion(Player player, PotionEffect potionEffect, boolean persistOldValues, PvPClass pvpClass) {
        PvPClass.setRestoreEffect(player, potionEffect);
    }

    public static void setRestoreEffect(Player player, PotionEffect effect) {
        boolean shouldCancel = true;
        Collection activeList = player.getActivePotionEffects();
        for (PotionEffect active : activeList) {
            if (!active.getType().equals((Object)effect.getType())) continue;
            if (effect.getAmplifier() < active.getAmplifier()) {
                return;
            }
            if (effect.getAmplifier() == active.getAmplifier() && 0 < active.getDuration() && (effect.getDuration() <= active.getDuration() || effect.getDuration() - active.getDuration() < 10)) {
                return;
            }
            restores.put((Object)player.getUniqueId(), (Object)active.getType(), (Object)active);
            shouldCancel = false;
            break;
        }
        player.addPotionEffect(effect, true);
        if (shouldCancel && effect.getDuration() > 120 && effect.getDuration() < 9600) {
            restores.remove((Object)player.getUniqueId(), (Object)effect.getType());
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        Player player;
        PotionEffect previous;
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player && (previous = (PotionEffect)restores.remove((Object)(player = (Player)livingEntity).getUniqueId(), (Object)event.getEffect().getType())) != null && previous.getDuration() < 1000000) {
            event.setCancelled(true);
            player.addPotionEffect(previous, true);
            Bukkit.getLogger().info("Restored " + previous.getType().toString() + " for " + player.getName() + ". duration: " + previous.getDuration() + ". amp: " + previous.getAmplifier());
        }
    }

    public String getName() {
        return this.name;
    }

    public String getSiteLink() {
        return this.siteLink;
    }

    public int getWarmup() {
        return this.warmup;
    }

    public String getArmorContains() {
        return this.armorContains;
    }

    public List<Material> getConsumables() {
        return this.consumables;
    }

    public static class SavedPotion {
        PotionEffect potionEffect;
        long time;
        private boolean perm;

        public SavedPotion(PotionEffect potionEffect, long time, boolean perm) {
            this.potionEffect = potionEffect;
            this.time = time;
            this.perm = perm;
        }

        public PotionEffect getPotionEffect() {
            return this.potionEffect;
        }

        public long getTime() {
            return this.time;
        }

        public boolean isPerm() {
            return this.perm;
        }
    }
}

