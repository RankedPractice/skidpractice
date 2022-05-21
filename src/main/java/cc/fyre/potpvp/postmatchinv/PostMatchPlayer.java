/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  rip.bridge.qlib.util.PlayerUtils
 */
package cc.fyre.potpvp.postmatchinv;

import cc.fyre.potpvp.kittype.HealingMethod;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import rip.bridge.qlib.util.PlayerUtils;

public final class PostMatchPlayer {
    private final UUID playerUuid;
    private final String lastUsername;
    private final ItemStack[] armor;
    private final ItemStack[] inventory;
    private final List<PotionEffect> potionEffects;
    private final int hunger;
    private final int health;
    private final transient HealingMethod healingMethodUsed;
    private final int totalHits;
    private final int longestCombo;
    private final int missedPots;
    private final int ping;

    public PostMatchPlayer(Player player, HealingMethod healingMethodUsed, int totalHits, int longestCombo, int missedPots) {
        this.playerUuid = player.getUniqueId();
        this.lastUsername = player.getName();
        this.armor = player.getInventory().getArmorContents();
        this.inventory = player.getInventory().getContents();
        this.potionEffects = ImmutableList.copyOf((Collection)player.getActivePotionEffects());
        this.hunger = player.getFoodLevel();
        this.health = (int)player.getHealth();
        this.healingMethodUsed = healingMethodUsed;
        this.totalHits = totalHits;
        this.longestCombo = longestCombo;
        this.missedPots = missedPots;
        this.ping = PlayerUtils.getPing((Player)player);
    }

    public UUID getPlayerUuid() {
        return this.playerUuid;
    }

    public String getLastUsername() {
        return this.lastUsername;
    }

    public ItemStack[] getArmor() {
        return this.armor;
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getHealth() {
        return this.health;
    }

    public HealingMethod getHealingMethodUsed() {
        return this.healingMethodUsed;
    }

    public int getTotalHits() {
        return this.totalHits;
    }

    public int getLongestCombo() {
        return this.longestCombo;
    }

    public int getMissedPots() {
        return this.missedPots;
    }

    public int getPing() {
        return this.ping;
    }
}

