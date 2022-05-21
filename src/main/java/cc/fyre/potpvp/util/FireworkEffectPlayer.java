/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.FireworkEffect
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Firework
 *  org.bukkit.inventory.meta.FireworkMeta
 */
package cc.fyre.potpvp.util;

import java.lang.reflect.Method;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkEffectPlayer {
    private Method world_getHandle = null;
    private Method nms_world_broadcastEntityEffect = null;
    private Method firework_getHandle = null;

    private static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (!m.getName().equals(method)) continue;
            return m;
        }
        return null;
    }

    public void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {
        Firework fw = (Firework)world.spawn(loc, Firework.class);
        Object nms_world = null;
        Object nms_firework = null;
        if (this.world_getHandle == null) {
            this.world_getHandle = FireworkEffectPlayer.getMethod(world.getClass(), "getHandle");
            this.firework_getHandle = FireworkEffectPlayer.getMethod(fw.getClass(), "getHandle");
        }
        nms_world = this.world_getHandle.invoke(world, null);
        nms_firework = this.firework_getHandle.invoke(fw, null);
        if (this.nms_world_broadcastEntityEffect == null) {
            this.nms_world_broadcastEntityEffect = FireworkEffectPlayer.getMethod(nms_world.getClass(), "broadcastEntityEffect");
        }
        FireworkMeta data2 = fw.getFireworkMeta();
        data2.clearEffects();
        data2.setPower(1);
        data2.addEffect(fe);
        fw.setFireworkMeta(data2);
        this.nms_world_broadcastEntityEffect.invoke(nms_world, nms_firework, (byte)17);
        fw.remove();
    }
}

