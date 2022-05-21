/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.elo.repository;

import cc.fyre.potpvp.kittype.KitType;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface EloRepository {
    public Map<KitType, Integer> loadElo(Set<UUID> var1) throws IOException;

    public void saveElo(Set<UUID> var1, Map<KitType, Integer> var2) throws IOException;

    public Map<String, Integer> topElo(KitType var1) throws IOException;

    public Map<KitType, Integer> loadPremiumElo(Set<UUID> var1) throws IOException;

    public void savePremiumElo(Set<UUID> var1, Map<KitType, Integer> var2) throws IOException;

    public Map<String, Integer> topPremiumElo(KitType var1) throws IOException;
}

