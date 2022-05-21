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

public final class BooleanTraitButton<T>
extends Button {
    private final T target;
    private final String trait;
    private final BiConsumer<T, Boolean> writeFunction;
    private final Function<T, Boolean> readFunction;
    private final Consumer<T> saveFunction;

    public BooleanTraitButton(T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction) {
        this(target, trait, writeFunction, readFunction, i -> {});
    }

    public BooleanTraitButton(T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction, Consumer<T> saveFunction) {
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
        return ImmutableList.of((Object)(ChatColor.YELLOW + "Current: " + ChatColor.WHITE + (this.readFunction.apply(this.target) != false ? "On" : "Off")), (Object)"", (Object)(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to toggle"));
    }

    public Material getMaterial(Player player) {
        return this.readFunction.apply(this.target) != false ? Material.REDSTONE_TORCH_ON : Material.LEVER;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
        boolean current = this.readFunction.apply(this.target);
        this.writeFunction.accept(this.target, !current);
        this.saveFunction.accept(this.target);
        player.sendMessage(ChatColor.GREEN + "Set " + this.trait + " trait to " + (current ? "off" : "on"));
    }
}

