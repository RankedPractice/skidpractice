/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\t"}, d2={"Lcc/fyre/potpvp/game/command/SetMaxPlayersCommand;", "", "()V", "execute", "", "sender", "Lorg/bukkit/command/CommandSender;", "amount", "", "potpvp-si"})
public final class SetMaxPlayersCommand {
    @NotNull
    public static final SetMaxPlayersCommand INSTANCE = new SetMaxPlayersCommand();

    private SetMaxPlayersCommand() {
    }

    @Command(names={"event setslots"}, permission="potpvp.event.admin", description="Sets the max players of the current event")
    @JvmStatic
    public static final void execute(@NotNull CommandSender sender, @Param(name="amount") int amount) {
        Intrinsics.checkNotNullParameter(sender, "sender");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null) {
            return;
        }
        Game game2 = game;
        int pre = game2.getMaxPlayers();
        game2.setMaxPlayers(amount);
        String[] stringArray = new String[]{ChatColor.DARK_RED + "An administrator has " + (pre > game2.getMaxPlayers() ? "downgraded" : "upgraded") + " the event slots to " + amount + '.'};
        game2.sendMessage(stringArray);
    }
}

