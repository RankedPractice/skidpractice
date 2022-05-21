/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.GameHandler;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/command/SetLobbyCommand;", "", "()V", "execute", "", "player", "Lorg/bukkit/entity/Player;", "potpvp-si"})
public final class SetLobbyCommand {
    @NotNull
    public static final SetLobbyCommand INSTANCE = new SetLobbyCommand();

    private SetLobbyCommand() {
    }

    @Command(names={"event setlobby"}, permission="potpvp.event.admin", async=true, description="Sets the event lobby location")
    @JvmStatic
    public static final void execute(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        GameHandler gameHandler = PotPvP.getInstance().gameHandler;
        Location location = player.getLocation();
        Intrinsics.checkNotNullExpressionValue(location, "player.location");
        gameHandler.setLobbyLocation(location);
        player.sendMessage(Intrinsics.stringPlus(ChatColor.GREEN.toString(), "You've updated the event lobby location."));
    }
}

