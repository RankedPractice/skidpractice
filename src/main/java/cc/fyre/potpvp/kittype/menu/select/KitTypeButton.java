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
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.kittype.menu.select;

import cc.fyre.potpvp.kittype.KitType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.util.Callback;

final class KitTypeButton
extends Button {
    private final KitType kitType;
    private final Callback<KitType> callback;
    private final List<String> descriptionLines;
    private final int amount;

    KitTypeButton(KitType kitType, Callback<KitType> callback) {
        this(kitType, callback, ImmutableList.of(), 1);
    }

    KitTypeButton(KitType kitType, Callback<KitType> callback, List<String> descriptionLines, int amount) {
        this.kitType = (KitType)Preconditions.checkNotNull(kitType, "kitType");
        this.callback = (Callback)Preconditions.checkNotNull(callback, "callback");
        this.descriptionLines = ImmutableList.copyOf(descriptionLines);
        this.amount = amount;
    }

    public String getName(@NotNull Player player) {
        return ChatColor.GREEN.toString() + ChatColor.BOLD + this.kitType.getDisplayName();
    }

    public List<String> getDescription(@NotNull Player player) {
        ArrayList<String> description2 = new ArrayList<String>();
        if (!this.descriptionLines.isEmpty()) {
            description2.addAll(this.descriptionLines);
        }
        if (!description2.isEmpty()) {
            description2.add("");
        }
        description2.add(ChatColor.GREEN + "Click here to select " + ChatColor.LIGHT_PURPLE + this.kitType.getDisplayName() + ChatColor.GREEN + ".");
        if (this.kitType.isHidden()) {
            description2.add(" ");
            description2.add(ChatColor.LIGHT_PURPLE + "Hidden from normal players");
        }
        return description2;
    }

    public Material getMaterial(@NotNull Player player) {
        return this.kitType.getIcon().getItemType();
    }

    public int getAmount(@NotNull Player player) {
        return this.amount;
    }

    public byte getDamageValue(@NotNull Player player) {
        return this.kitType.getIcon().getData();
    }

    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        this.callback.callback(this.kitType);
    }
}

