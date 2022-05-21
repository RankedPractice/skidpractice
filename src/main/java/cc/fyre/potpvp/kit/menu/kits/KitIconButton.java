/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.kits;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kit.KitHandler;
import cc.fyre.potpvp.kit.menu.editkit.EditKitMenu;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class KitIconButton
extends Button {
    private final Optional<Kit> kitOpt;
    private final KitType kitType;
    private final int slot;

    KitIconButton(Optional<Kit> kitOpt, KitType kitType, int slot) {
        this.kitOpt = (Optional)Preconditions.checkNotNull(kitOpt, (Object)"kitOpt");
        this.kitType = (KitType)Preconditions.checkNotNull((Object)kitType, (Object)"kitType");
        this.slot = slot;
    }

    public String getName(Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + this.kitOpt.map(Kit::getName).orElse("Create Kit");
    }

    public List<String> getDescription(Player player) {
        return (List)this.kitOpt.map(kit -> ImmutableList.of((Object)"", (Object)(ChatColor.GREEN + "Heals: " + ChatColor.WHITE + kit.countHeals()), (Object)(ChatColor.RED + "Debuffs: " + ChatColor.WHITE + kit.countDebuffs()))).orElse(ImmutableList.of());
    }

    public Material getMaterial(Player player) {
        return this.kitOpt.isPresent() ? Material.DIAMOND_SWORD : Material.STONE_SWORD;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        Kit resolvedKit = this.kitOpt.orElseGet(() -> {
            KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
            return kitHandler.saveDefaultKit(player, this.kitType, this.slot);
        });
        new EditKitMenu(resolvedKit).openMenu(player);
    }
}

