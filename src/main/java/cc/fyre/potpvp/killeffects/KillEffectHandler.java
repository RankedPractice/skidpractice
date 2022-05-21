/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.killeffects;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.killeffects.effects.KillEffect;
import cc.fyre.potpvp.profile.Profile;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class KillEffectHandler {
    private List<KillEffect> effects = new ArrayList<KillEffect>();

    public KillEffectHandler() {
        this.getEffects().add(KillEffect.firework);
        this.getEffects().add(KillEffect.flame);
        this.getEffects().add(KillEffect.splash);
        this.getEffects().add(KillEffect.explosion);
        this.getEffects().add(KillEffect.ender);
        this.getEffects().add(KillEffect.blood);
        this.getEffects().add(KillEffect.smoke);
        this.getEffects().add(KillEffect.cloud);
    }

    public void playEffect(Player killer, Player dead) {
        Profile p = PotPvP.getInstance().getProfileManager().getProfile(killer);
        for (int i = 0; i < 5; ++i) {
            if (p.getKillEffect().isUseMaterial()) {
                dead.getLocation().getWorld().playEffect(dead.getLocation().add(0.0, (double)i, 0.0), p.getKillEffect().getEffect(), (Object)p.getKillEffect().getMaterial());
                continue;
            }
            dead.getLocation().getWorld().playEffect(dead.getLocation().add(0.0, (double)i, 0.0), p.getKillEffect().getEffect(), p.getKillEffect().getEffectData());
        }
    }

    public List<KillEffect> getEffects() {
        return this.effects;
    }
}

