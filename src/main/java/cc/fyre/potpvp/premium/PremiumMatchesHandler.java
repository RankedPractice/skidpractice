/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  redis.clients.jedis.Jedis
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.premium;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import redis.clients.jedis.Jedis;
import rip.bridge.qlib.qLib;

public class PremiumMatchesHandler {
    private static final String KEY_PREFIX = "potpvp:premiumMatches:";
    private static final Map<UUID, Integer> premiumMatchesCache = new HashMap<UUID, Integer>();

    public int increasePremiumMatches(UUID target) {
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            int amount = jedis.incr(KEY_PREFIX + target).intValue();
            premiumMatchesCache.put(target, amount);
            int n = amount;
            return n;
        }
    }

    public int setPremiumMatches(UUID target, int amount) {
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            premiumMatchesCache.put(target, amount);
            jedis.set(KEY_PREFIX + target, String.valueOf(amount));
            int n = amount;
            return n;
        }
    }

    public int removePremiumMatches(UUID target, int amount) {
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            int old = premiumMatchesCache.getOrDefault(target, 0);
            if (old <= 0) {
                int n = 0;
                return n;
            }
            premiumMatchesCache.put(target, old - amount);
            jedis.set(KEY_PREFIX + target, String.valueOf(old - amount));
            int n = amount;
            return n;
        }
    }

    public int getPremiumMatches(UUID target) {
        if (premiumMatchesCache.containsKey(target)) {
            return premiumMatchesCache.getOrDefault(target, 0);
        }
        try (Jedis jedis = qLib.getInstance().getLocalJedisPool().getResource();){
            String existing = jedis.get(KEY_PREFIX + target);
            if (existing != null && !existing.isEmpty()) {
                int amount = Integer.parseInt(existing);
                premiumMatchesCache.put(target, Integer.parseInt(existing));
                int n = amount;
                return n;
            }
            int n = 0;
            return n;
        }
    }
}

