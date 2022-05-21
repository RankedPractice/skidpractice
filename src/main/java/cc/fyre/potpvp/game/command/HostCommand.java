/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.game.menu.HostMenu;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/command/HostCommand;", "", "()V", "host", "", "player", "Lorg/bukkit/entity/Player;", "potpvp-si"})
public final class HostCommand {
    @NotNull
    public static final HostCommand INSTANCE = new HostCommand();

    private HostCommand() {
    }

    @Command(names={"host"}, permission="", description="Host an event")
    @JvmStatic
    public static final void host(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        new HostMenu().openMenu(player);
    }
}

