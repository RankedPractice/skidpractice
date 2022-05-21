/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package cc.fyre.potpvp.game.parameter;

import kotlin.Metadata;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2={"Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "", "getDisplayName", "", "getIcon", "Lorg/bukkit/inventory/ItemStack;", "potpvp-si"})
public interface GameParameterOption {
    @NotNull
    public String getDisplayName();

    @NotNull
    public ItemStack getIcon();
}

