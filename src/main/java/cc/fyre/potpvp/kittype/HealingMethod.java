/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.kittype;

import cc.fyre.potpvp.util.ItemUtils;
import java.util.function.Function;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum HealingMethod {
    POTIONS("pot", "pots", "health potion", "health potions", Material.POTION, 16421, i -> ItemUtils.countStacksMatching(i, ItemUtils.INSTANT_HEAL_POTION_PREDICATE)),
    GOLDEN_APPLE("gap", "gaps", "golden apple", "golden apples", Material.GOLDEN_APPLE, 1, items -> {
        int count = 0;
        for (ItemStack item : items) {
            if (item == null || item.getType() != Material.GOLDEN_APPLE || item.getData().getData() != 1) continue;
            count += Math.max(1, item.getAmount());
        }
        return count;
    }),
    SOUP("soup", "soup", "soup", "soup", Material.MUSHROOM_SOUP, 0, i -> ItemUtils.countStacksMatching(i, ItemUtils.SOUP_PREDICATE));

    private final String shortSingular;
    private final String shortPlural;
    private final String longSingular;
    private final String longPlural;
    private final Material iconType;
    private final short iconDurability;
    private final Function<ItemStack[], Integer> countFunction;

    private HealingMethod(String shortSingular, String shortPlural, String longSingular, String longPlural, Material iconType, short iconDurability, Function<ItemStack[], Integer> countFunction) {
        this.shortSingular = shortSingular;
        this.shortPlural = shortPlural;
        this.longSingular = longSingular;
        this.longPlural = longPlural;
        this.iconType = iconType;
        this.iconDurability = iconDurability;
        this.countFunction = countFunction;
    }

    public int count(ItemStack[] items) {
        return this.countFunction.apply(items);
    }

    public String getShortSingular() {
        return this.shortSingular;
    }

    public String getShortPlural() {
        return this.shortPlural;
    }

    public String getLongSingular() {
        return this.longSingular;
    }

    public String getLongPlural() {
        return this.longPlural;
    }

    public Material getIconType() {
        return this.iconType;
    }

    public short getIconDurability() {
        return this.iconDurability;
    }
}

