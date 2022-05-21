/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.postmatchinv.menu;

import cc.fyre.potpvp.kittype.HealingMethod;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class PostMatchHealsLeftButton
extends Button {
    private final UUID player;
    private final HealingMethod healingMethod;
    private final int healsRemaining;
    private final int missedHeals;

    PostMatchHealsLeftButton(UUID player, HealingMethod healingMethod, int healsRemaining, int missedHeals) {
        this.player = player;
        this.healingMethod = healingMethod;
        this.healsRemaining = healsRemaining;
        this.missedHeals = missedHeals;
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + this.healsRemaining + " " + (this.healsRemaining == 1 ? this.healingMethod.getLongSingular() : this.healingMethod.getLongPlural());
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((ChatColor.YELLOW + FrozenUUIDCache.name((UUID)this.player) + " had " + this.healsRemaining + " " + (this.healsRemaining == 1 ? this.healingMethod.getLongSingular() : this.healingMethod.getLongPlural()) + " left."), (ChatColor.YELLOW + FrozenUUIDCache.name((UUID)this.player) + " missed " + this.missedHeals + " health potion" + (this.missedHeals == 1 ? "." : "s.")));
    }

    public Material getMaterial(Player player) {
        return this.healingMethod.getIconType();
    }

    public ItemStack getButtonItem(Player player) {
        ItemStack item = super.getButtonItem(player);
        item.setDurability(this.healingMethod.getIconDurability());
        return item;
    }

    public int getAmount(Player player) {
        return this.healsRemaining;
    }
}

