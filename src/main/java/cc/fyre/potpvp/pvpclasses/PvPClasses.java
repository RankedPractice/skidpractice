/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Material
 */
package cc.fyre.potpvp.pvpclasses;

import cc.fyre.potpvp.party.Party;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

public enum PvPClasses {
    DIAMOND(Material.DIAMOND_CHESTPLATE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
    BARD(Material.GOLD_CHESTPLATE, 1, 2, 2),
    ARCHER(Material.LEATHER_CHESTPLATE, 1, 2, 2);

    private final Material icon;
    private final int maxForFive;
    private final int maxForTen;
    private final int maxForTwenty;

    private PvPClasses(Material icon, int maxForFive, int maxForTen, int maxForTwenty) {
        this.icon = icon;
        this.maxForFive = maxForFive;
        this.maxForTen = maxForTen;
        this.maxForTwenty = maxForTwenty;
    }

    public boolean allowed(Party party) {
        int current = (int)party.getKits().values().stream().filter(pvPClasses -> pvPClasses == this).count();
        int size = party.getMembers().size();
        if (size < 10 && current >= this.maxForFive) {
            return false;
        }
        if (size < 20 && current >= this.maxForTen) {
            return false;
        }
        return current < this.maxForTwenty;
    }

    public String getName() {
        return StringUtils.capitalize((String)this.name().toLowerCase());
    }

    public Material getIcon() {
        return this.icon;
    }

    public int getMaxForFive() {
        return this.maxForFive;
    }

    public int getMaxForTen() {
        return this.maxForTen;
    }

    public int getMaxForTwenty() {
        return this.maxForTwenty;
    }
}

