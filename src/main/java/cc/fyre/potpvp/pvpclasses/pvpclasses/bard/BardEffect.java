/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.potion.PotionEffect
 */
package cc.fyre.potpvp.pvpclasses.pvpclasses.bard;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.potion.PotionEffect;

public class BardEffect {
    private PotionEffect potionEffect;
    private int energy;
    private Map<String, Long> lastMessageSent = new HashMap<String, Long>();

    public static BardEffect fromPotion(PotionEffect potionEffect) {
        return new BardEffect(potionEffect, -1);
    }

    public static BardEffect fromPotionAndEnergy(PotionEffect potionEffect, int energy) {
        return new BardEffect(potionEffect, energy);
    }

    public static BardEffect fromEnergy(int energy) {
        return new BardEffect(null, energy);
    }

    private BardEffect(PotionEffect potionEffect, int energy) {
        this.potionEffect = potionEffect;
        this.energy = energy;
    }

    public PotionEffect getPotionEffect() {
        return this.potionEffect;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Map<String, Long> getLastMessageSent() {
        return this.lastMessageSent;
    }
}

