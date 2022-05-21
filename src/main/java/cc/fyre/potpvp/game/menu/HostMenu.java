/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.game.menu;

import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.menu.HostEventButton;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.Menu;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\bH\u0016\u00a8\u0006\f"}, d2={"Lcc/fyre/potpvp/game/menu/HostMenu;", "Lrip/bridge/qlib/menu/Menu;", "()V", "getButtons", "", "", "Lrip/bridge/qlib/menu/Button;", "player", "Lorg/bukkit/entity/Player;", "getTitle", "", "p0", "potpvp-si"})
public final class HostMenu
extends Menu {
    @NotNull
    public String getTitle(@Nullable Player p0) {
        String string = ChatColor.translateAlternateColorCodes((char)'&', (String)"&3&lHost an Event");
        Intrinsics.checkNotNullExpressionValue(string, "translateAlternateColorC\u2026'&', \"&3&lHost an Event\")");
        return string;
    }

    @NotNull
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        HashMap map = new HashMap();
        Map map2 = map;
        Integer n = 0;
        Object object = GameEvent.Companion.getEvents().get(0);
        Intrinsics.checkNotNullExpressionValue(object, "GameEvent.events[0]");
        object = new GameTeamSizeParameter.Singles[]{GameTeamSizeParameter.Singles.INSTANCE};
        object = new HostEventButton((GameEvent)object, (List<? extends GameParameterOption>)CollectionsKt.arrayListOf(object));
        boolean bl = false;
        map2.put(n, object);
        map2 = map;
        n = 1;
        object = GameEvent.Companion.getEvents().get(0);
        Intrinsics.checkNotNullExpressionValue(object, "GameEvent.events[0]");
        GameEvent gameEvent = (GameEvent)object;
        object = new GameTeamSizeParameter.Duos[]{GameTeamSizeParameter.Duos.INSTANCE};
        object = new HostEventButton(gameEvent, (List<? extends GameParameterOption>)CollectionsKt.arrayListOf(object));
        bl = false;
        map2.put(n, object);
        return map;
    }
}

