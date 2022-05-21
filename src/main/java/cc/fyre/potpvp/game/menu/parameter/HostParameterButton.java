/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 */
package cc.fyre.potpvp.game.menu.parameter;

import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.menu.Button;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\f\u001a\u00020\rH\u0016J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u001a\u001a\u00020\u00172\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u001b"}, d2={"Lcc/fyre/potpvp/game/menu/parameter/HostParameterButton;", "Lrip/bridge/qlib/menu/Button;", "parameter", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "(Lcc/fyre/potpvp/game/parameter/GameParameter;)V", "<set-?>", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "selectedOption", "getSelectedOption", "()Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "clicked", "", "player", "Lorg/bukkit/entity/Player;", "slot", "", "clickType", "Lorg/bukkit/event/inventory/ClickType;", "getAmount", "getDamageValue", "", "getDescription", "", "", "getMaterial", "Lorg/bukkit/Material;", "getName", "potpvp-si"})
public final class HostParameterButton
extends Button {
    @NotNull
    private final GameParameter parameter;
    @Nullable
    private GameParameterOption selectedOption;

    public HostParameterButton(@NotNull GameParameter parameter) {
        Intrinsics.checkNotNullParameter(parameter, "parameter");
        this.parameter = parameter;
        this.selectedOption = this.parameter.getOptions().get(0);
    }

    @Nullable
    public final GameParameterOption getSelectedOption() {
        return this.selectedOption;
    }

    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(clickType, "clickType");
        int index = CollectionsKt.indexOf(this.parameter.getOptions(), this.selectedOption);
        if (index + 1 == this.parameter.getOptions().size()) {
            index = 0;
        } else {
            int n = index;
            index = n + 1;
        }
        this.selectedOption = this.parameter.getOptions().get(index);
    }

    @NotNull
    public String getName(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        return Intrinsics.stringPlus(ChatColor.DARK_PURPLE.toString(), this.parameter.getDisplayName());
    }

    @NotNull
    public List<String> getDescription(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        ArrayList<String> toReturn = new ArrayList<String>();
        for (GameParameterOption option : this.parameter.getOptions()) {
            if (Intrinsics.areEqual(option, this.selectedOption)) {
                toReturn.add(ChatColor.GREEN.toString() + "\u00bb " + ChatColor.GRAY + option.getDisplayName());
                continue;
            }
            toReturn.add(Intrinsics.stringPlus(ChatColor.GRAY.toString(), option.getDisplayName()));
        }
        return toReturn;
    }

    @NotNull
    public Material getMaterial(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        GameParameterOption gameParameterOption = this.selectedOption;
        Intrinsics.checkNotNull(gameParameterOption);
        Material material = gameParameterOption.getIcon().getType();
        Intrinsics.checkNotNullExpressionValue(material, "selectedOption!!.getIcon().type");
        return material;
    }

    public int getAmount(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        GameParameterOption gameParameterOption = this.selectedOption;
        Intrinsics.checkNotNull(gameParameterOption);
        return gameParameterOption.getIcon().getAmount();
    }

    public byte getDamageValue(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        GameParameterOption gameParameterOption = this.selectedOption;
        Intrinsics.checkNotNull(gameParameterOption);
        return (byte)gameParameterOption.getIcon().getDurability();
    }
}

