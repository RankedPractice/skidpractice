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
package cc.fyre.potpvp.game.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.game.util.TextSplitter;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import cc.fyre.potpvp.util.CC;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.menu.Button;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J \u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00142\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2={"Lcc/fyre/potpvp/game/menu/HostEventButton;", "Lrip/bridge/qlib/menu/Button;", "event", "Lcc/fyre/potpvp/game/event/GameEvent;", "options", "", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "(Lcc/fyre/potpvp/game/event/GameEvent;Ljava/util/List;)V", "clicked", "", "player", "Lorg/bukkit/entity/Player;", "slot", "", "clickType", "Lorg/bukkit/event/inventory/ClickType;", "getAmount", "getDamageValue", "", "getDescription", "", "getMaterial", "Lorg/bukkit/Material;", "getName", "potpvp-si"})
public final class HostEventButton
extends Button {
    @NotNull
    private final GameEvent event;
    @NotNull
    private final List<GameParameterOption> options;

    public HostEventButton(@NotNull GameEvent event, @NotNull List<? extends GameParameterOption> options) {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(options, "options");
        this.event = event;
        this.options = options;
    }

    @Nullable
    public String getName(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        String teamSize = this.options.contains(GameTeamSizeParameter.Duos.INSTANCE) ? " &7(Duos)" : "";
        return CC.translate("&6&l" + this.event.getName() + teamSize);
    }

    @Nullable
    public List<String> getDescription(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        ArrayList<String> toReturn = new ArrayList<String>();
        String string = CC.translate(this.event.getDescription());
        Intrinsics.checkNotNullExpressionValue(string, "translate(event.getDescription())");
        toReturn.addAll((Collection)TextSplitter.split(34, string, "&e", " "));
        toReturn.add(" ");
        if (this.options.contains(GameTeamSizeParameter.Duos.INSTANCE)) {
            toReturn.add(CC.translate("&dPlay with a friend!"));
            toReturn.add(CC.translate("&eIn this event you can choose"));
            toReturn.add(CC.translate("&ea team-mate to fight with you."));
            toReturn.add(" ");
        }
        if (player.hasPermission(this.event.getPermission())) {
            toReturn.add(CC.translate("&aYou can host this event."));
        } else {
            toReturn.add(CC.translate("&cYou can't host this event."));
            toReturn.add(CC.translate("&cPurchase a rank that can on our store."));
        }
        return toReturn;
    }

    @Nullable
    public Material getMaterial(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        return this.event.getIcon().getType();
    }

    public byte getDamageValue(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        return (byte)this.event.getIcon().getDurability();
    }

    public int getAmount(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        return this.options.contains(GameTeamSizeParameter.Duos.INSTANCE) ? 2 : 1;
    }

    public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(clickType, "clickType");
        if (player.hasPermission(this.event.getPermission())) {
            int maxPlayers;
            if (PotPvP.getInstance().gameHandler.getOngoingGame() != null) {
                player.sendMessage(ChatColor.RED + "You can't start an event if there is an active event.");
                return;
            }
            int n = player.hasPermission("potpvp.event.host.100") ? 100 : (player.hasPermission("potpvp.event.host.75") ? 75 : (player.hasPermission("potpvp.event.host.50") ? 50 : (maxPlayers = player.hasPermission("potpvp.event.host.30") ? 30 : 20)));
            if (!PotPvP.getInstance().gameHandler.getHostEnabled()) {
                player.sendMessage(ChatColor.RED + "Event hosting is currently disabled. Try again later.");
                return;
            }
            PotPvP.getInstance().gameHandler.startGame(new Game(this.event, player, this.options, maxPlayers));
            player.closeInventory();
        }
    }
}

