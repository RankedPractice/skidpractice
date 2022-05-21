/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.util.TimeUtils
 */
package cc.fyre.potpvp.postmatchinv.menu;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.util.TimeUtils;

final class PostMatchPotionEffectsButton
extends Button {
    private final List<PotionEffect> effects;

    PostMatchPotionEffectsButton(List<PotionEffect> effects) {
        this.effects = ImmutableList.copyOf(effects);
    }

    public String getName(Player player) {
        return ChatColor.GREEN + "Potion Effects";
    }

    public List<String> getDescription(Player player) {
        if (!this.effects.isEmpty()) {
            return this.effects.stream().map(effect -> ChatColor.AQUA + this.formatEffectType(effect.getType()) + " " + (effect.getAmplifier() + 1) + ChatColor.GRAY + " - " + TimeUtils.formatIntoMMSS((int)(effect.getDuration() / 20))).collect(Collectors.toList());
        }
        return ImmutableList.of("", (ChatColor.GRAY + "No potion effects."));
    }

    public Material getMaterial(Player player) {
        return Material.POTION;
    }

    private String formatEffectType(PotionEffectType type2) {
        switch (type2.getName().toLowerCase()) {
            case "fire_resistance": {
                return "Fire Resistance";
            }
            case "increase_damage": {
                return "Strength";
            }
            case "damage_resistance": {
                return "Resistance";
            }
        }
        return StringUtils.capitalize((String)type2.getName().toLowerCase());
    }
}

