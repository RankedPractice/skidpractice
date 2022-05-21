/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.kit.menu.kits;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kit.KitHandler;
import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

final class KitDeleteButton
extends Button {
    private final KitType kitType;
    private final int slot;

    KitDeleteButton(KitType kitType, int slot) {
        this.kitType = (KitType)Preconditions.checkNotNull(kitType, "kitType");
        this.slot = slot;
    }

    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Delete";
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of("", (ChatColor.RED + "Click here to delete this kit"), (ChatColor.RED + "You will " + ChatColor.BOLD + "NOT" + ChatColor.RED + " be able to"), (ChatColor.RED + "recover this kit."));
    }

    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    public byte getDamageValue(Player player) {
        return DyeColor.RED.getWoolData();
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        KitHandler kitHandler = PotPvP.getInstance().getKitHandler();
        kitHandler.removeKit(player, this.kitType, this.slot);
    }
}

