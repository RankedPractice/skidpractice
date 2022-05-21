/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionType
 */
package cc.fyre.potpvp.util;

import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public final class ItemUtils {
    public static final Predicate<ItemStack> INSTANT_HEAL_POTION_PREDICATE = item -> {
        if (item.getType() != Material.POTION) {
            return false;
        }
        PotionType potionType = Potion.fromItemStack((ItemStack)item).getType();
        return potionType == PotionType.INSTANT_HEAL;
    };
    public static final Predicate<ItemStack> SOUP_PREDICATE = item -> item.getType() == Material.MUSHROOM_SOUP;
    public static final Predicate<ItemStack> DEBUFF_POTION_PREDICATE = item -> {
        if (item.getType() == Material.POTION) {
            PotionType type2 = Potion.fromItemStack((ItemStack)item).getType();
            return type2 == PotionType.WEAKNESS || type2 == PotionType.SLOWNESS || type2 == PotionType.POISON || type2 == PotionType.INSTANT_DAMAGE;
        }
        return false;
    };
    public static final Predicate<ItemStack> EDIBLE_PREDICATE = item -> item.getType().isEdible();

    public static void setDisplayName(ItemStack stack, String name) {
        ItemMeta dick = stack.getItemMeta();
        dick.setDisplayName(name);
        stack.setItemMeta(dick);
    }

    public static int countStacksMatching(ItemStack[] items, Predicate<ItemStack> predicate) {
        if (items == null) {
            return 0;
        }
        int amountMatching = 0;
        for (ItemStack item : items) {
            if (item == null || !predicate.test(item)) continue;
            ++amountMatching;
        }
        return amountMatching;
    }

    private ItemUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

