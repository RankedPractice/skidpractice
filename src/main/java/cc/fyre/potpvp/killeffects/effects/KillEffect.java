/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Effect
 *  org.bukkit.Material
 */
package cc.fyre.potpvp.killeffects.effects;

import org.bukkit.Effect;
import org.bukkit.Material;

public enum KillEffect {
    none(Material.AIR, "none", "&7&lNone", Effect.EXTINGUISH, 0, false, Material.AIR),
    splash(Material.WATER_BUCKET, "splash", "&b&lSplash", Effect.SPLASH, 0, false, Material.AIR),
    flame(Material.FLINT_AND_STEEL, "splash", "&b&lFlame", Effect.FLAME, 0, false, Material.AIR),
    explosion(Material.TNT, "explosion", "&b&lExplosion", Effect.EXPLOSION, 0, false, Material.AIR),
    ender(Material.ENDER_PEARL, "ender", "&b&lEnder", Effect.ENDER_SIGNAL, 0, false, Material.AIR),
    blood(Material.REDSTONE, "blood", "&b&lBlood", Effect.STEP_SOUND, 0, true, Material.REDSTONE_BLOCK),
    firework(Material.FIREWORK, "firework", "&b&lFirework Spark", Effect.FIREWORKS_SPARK, 0, false, Material.AIR),
    cloud(Material.WEB, "cloud", "&b&lCloud", Effect.CLOUD, 0, false, Material.AIR),
    smoke(Material.BONE, "smoke", "&b&lSmoke", Effect.LARGE_SMOKE, 0, false, Material.AIR);

    private final Material icon;
    private final String name;
    private final String displayName;
    private final Effect effect;
    private final int effectData;
    private final boolean useMaterial;
    private final Material material;

    private KillEffect(Material icon, String name, String displayName, Effect effect, int effectData, boolean useMaterial, Material material) {
        this.icon = icon;
        this.name = name;
        this.displayName = displayName;
        this.effect = effect;
        this.effectData = effectData;
        this.useMaterial = useMaterial;
        this.material = material;
    }

    public Material getIcon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getEffectData() {
        return this.effectData;
    }

    public boolean isUseMaterial() {
        return this.useMaterial;
    }

    public Material getMaterial() {
        return this.material;
    }
}

