/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.qlib.util.ItemBuilder
 */
package cc.fyre.potpvp.game.event;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.util.ItemBuilder;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u00048FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R$\u0010\b\u001a\n \t*\u0004\u0018\u00010\u00040\u00048\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\n\u0010\u0002\u001a\u0004\b\u000b\u0010\u0007\u00a8\u0006\f"}, d2={"Lcc/fyre/potpvp/game/event/GameEventItems;", "", "()V", "eventItem", "Lorg/bukkit/inventory/ItemStack;", "getEventItem$annotations", "getEventItem", "()Lorg/bukkit/inventory/ItemStack;", "voteItem", "kotlin.jvm.PlatformType", "getVoteItem$annotations", "getVoteItem", "potpvp-si"})
public final class GameEventItems {
    @NotNull
    public static final GameEventItems INSTANCE = new GameEventItems();
    private static final ItemStack voteItem = ItemBuilder.of((Material)Material.PAPER).name(ChatColor.GRAY + "\u00bb " + ChatColor.GOLD + ChatColor.BOLD + "%MAP%" + ChatColor.GRAY + " \u00ab").build();

    private GameEventItems() {
    }

    @Nullable
    public static final ItemStack getEventItem() {
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        return game != null ? ItemBuilder.of((Material)Material.EMERALD).name(Intrinsics.stringPlus(ChatColor.DARK_AQUA.toString(), "Join Event")).build() : null;
    }

    @JvmStatic
    public static /* synthetic */ void getEventItem$annotations() {
    }

    public static final ItemStack getVoteItem() {
        return voteItem;
    }

    @JvmStatic
    public static /* synthetic */ void getVoteItem$annotations() {
    }
}

