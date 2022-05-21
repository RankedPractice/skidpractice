/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.game.menu.parameter;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.menu.parameter.HostParameterButton;
import cc.fyre.potpvp.game.menu.parameter.HostParametersMenu;
import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
import rip.bridge.qlib.menu.Menu;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u000fH\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0014H\u0016R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2={"Lcc/fyre/potpvp/game/menu/parameter/HostParametersMenu;", "Lrip/bridge/qlib/menu/Menu;", "event", "Lcc/fyre/potpvp/game/event/GameEvent;", "(Lcc/fyre/potpvp/game/event/GameEvent;)V", "buttonReferences", "Ljava/util/ArrayList;", "Lcc/fyre/potpvp/game/menu/parameter/HostParameterButton;", "getButtonReferences", "()Ljava/util/ArrayList;", "getButtons", "", "", "Lrip/bridge/qlib/menu/Button;", "player", "Lorg/bukkit/entity/Player;", "getTitle", "", "p0", "isPlaceholder", "", "isUpdateAfterClick", "potpvp-si"})
public final class HostParametersMenu
extends Menu {
    @NotNull
    private final GameEvent event;
    @NotNull
    private final ArrayList<HostParameterButton> buttonReferences;

    public HostParametersMenu(@NotNull GameEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.event = event;
        this.buttonReferences = new ArrayList();
        for (GameParameter parameter : this.event.getParameters()) {
            this.buttonReferences.add(new HostParameterButton(parameter));
        }
    }

    @NotNull
    public final ArrayList<HostParameterButton> getButtonReferences() {
        return this.buttonReferences;
    }

    public boolean isPlaceholder() {
        return true;
    }

    public boolean isUpdateAfterClick() {
        return true;
    }

    @NotNull
    public String getTitle(@Nullable Player p0) {
        return "&6" + this.event.getName() + " options";
    }

    @NotNull
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        Object object;
        Intrinsics.checkNotNullParameter(player, "player");
        HashMap toReturn = new HashMap();
        for (HostParameterButton button : this.buttonReferences) {
            object = toReturn;
            Integer n = toReturn.size();
            Intrinsics.checkNotNullExpressionValue((Object)button, "button");
            HostParameterButton hostParameterButton = button;
            boolean bl = false;
            object.put(n, hostParameterButton);
        }
        Map map = toReturn;
        Integer n = 8;
        object = new Button(this){
            final /* synthetic */ HostParametersMenu this$0;
            {
                this.this$0 = $receiver;
            }

            @Nullable
            public String getName(@NotNull Player player) {
                Intrinsics.checkNotNullParameter(player, "player");
                return Intrinsics.stringPlus("&aStart ", HostParametersMenu.access$getEvent$p(this.this$0).getName());
            }

            @Nullable
            public List<String> getDescription(@NotNull Player player) {
                Intrinsics.checkNotNullParameter(player, "player");
                String[] stringArray = new String[]{"&7Click to start the event."};
                return CollectionsKt.arrayListOf(stringArray);
            }

            @Nullable
            public Material getMaterial(@NotNull Player player) {
                Intrinsics.checkNotNullParameter(player, "player");
                return Material.EMERALD;
            }

            public void clicked(@NotNull Player player, int slot, @NotNull ClickType clickType) {
                Intrinsics.checkNotNullParameter(player, "player");
                Intrinsics.checkNotNullParameter(clickType, "clickType");
                if (PotPvP.getInstance().gameHandler.getOngoingGame() != null) {
                    player.sendMessage(ChatColor.RED + "You can't start an event if there is an active event.");
                    return;
                }
                int maxPlayers = player.hasPermission("potpvp.event.host.100") ? 100 : (player.hasPermission("potpvp.event.host.75") ? 75 : (player.hasPermission("potpvp.event.host.50") ? 50 : (player.hasPermission("potpvp.event.host.30") ? 30 : 0)));
                List<T> list = this.this$0.getButtonReferences().stream().map(getButtons.1::clicked$lambda-0).filter(Objects::nonNull).collect(Collectors.toList());
                if (list == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<cc.fyre.potpvp.game.parameter.GameParameterOption>");
                }
                List<T> options = list;
                PotPvP.getInstance().gameHandler.startGame(new Game(HostParametersMenu.access$getEvent$p(this.this$0), player, options, maxPlayers));
                player.closeInventory();
            }

            private static final GameParameterOption clicked$lambda-0(HostParameterButton button) {
                return button.getSelectedOption();
            }
        };
        boolean bl = false;
        map.put(n, object);
        return toReturn;
    }

    public static final /* synthetic */ GameEvent access$getEvent$p(HostParametersMenu $this) {
        return $this.event;
    }
}

