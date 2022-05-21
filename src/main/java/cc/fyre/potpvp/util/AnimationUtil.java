/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationUtil {
    public static String loading;

    public static void init() {
        List<String> loadings = Arrays.asList("Loading.", "Loading..", "Loading...");
        int[] b = new int[]{0};
        AnimationUtil.runTimer(() -> {
            if (b[0] == loadings.size()) {
                b[0] = 0;
            }
            int n = b[0];
            b[0] = n + 1;
            loading = (String)loadings.get(n);
        }, 0L, 40L);
    }

    public static String get() {
        return loading;
    }

    public static void run(Runnable runnable) {
        PotPvP.getInstance().getServer().getScheduler().runTask((Plugin)PotPvP.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        PotPvP.getInstance().getServer().getScheduler().runTaskTimer((Plugin)PotPvP.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer((Plugin)PotPvP.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        PotPvP.getInstance().getServer().getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), runnable);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        PotPvP.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), runnable, delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        PotPvP.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), runnable, delay, timer);
    }
}

