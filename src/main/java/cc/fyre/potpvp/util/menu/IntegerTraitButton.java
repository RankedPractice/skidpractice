/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.util.menu;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import rip.bridge.qlib.menu.Button;

public final class IntegerTraitButton<T>
extends Button {
    private final T target;
    private final String trait;
    private final BiConsumer<T, Integer> writeFunction;
    private final Function<T, Integer> readFunction;
    private final Consumer<T> saveFunction;

    public IntegerTraitButton(T target, String trait, BiConsumer<T, Integer> writeFunction, Function<T, Integer> readFunction) {
        this(target, trait, writeFunction, readFunction, i -> {});
    }

    public IntegerTraitButton(T target, String trait, BiConsumer<T, Integer> writeFunction, Function<T, Integer> readFunction, Consumer<T> saveFunction) {
        this.target = target;
        this.trait = trait;
        this.writeFunction = writeFunction;
        this.readFunction = readFunction;
        this.saveFunction = saveFunction;
    }

    public String getName(Player player) {
        return ChatColor.GOLD + "Edit " + this.trait;
    }

    public List<String> getDescription(Player player) {
        return ImmutableList.of((ChatColor.YELLOW + "Current: " + ChatColor.WHITE + this.readFunction.apply(this.target)), "", (ChatColor.GREEN.toString() + ChatColor.BOLD + "LEFT-CLICK " + ChatColor.GREEN + "to increase by 1"), (ChatColor.GREEN.toString() + ChatColor.BOLD + "SHIFT LEFT-CLICK " + ChatColor.GREEN + "to increase by 10"), "", (ChatColor.RED.toString() + ChatColor.BOLD + "RIGHT-CLICK " + ChatColor.GREEN + "to decrease by 1"), (ChatColor.RED.toString() + ChatColor.BOLD + "SHIFT RIGHT-CLICK " + ChatColor.GREEN + "to decrease by 10"));
    }

    public Material getMaterial(Player player) {
        return Material.GHAST_TEAR;
    }

    public int getAmount(Player player) {
        return this.readFunction.apply(this.target);
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        int change;
        int current = this.readFunction.apply(this.target);
        int n = change = clickType.isShiftClick() ? 10 : 1;
        if (clickType.isRightClick()) {
            change = -change;
        }
        this.writeFunction.accept(this.target, current + change);
        this.saveFunction.accept(this.target);
        player.sendMessage(ChatColor.GREEN + "Set " + this.trait + " trait to " + (current + change));
    }
}

