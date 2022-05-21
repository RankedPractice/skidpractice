/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/command/SpectateCommand;", "", "()V", "execute", "", "player", "Lorg/bukkit/entity/Player;", "potpvp-si"})
public final class SpectateCommand {
    @NotNull
    public static final SpectateCommand INSTANCE = new SpectateCommand();

    private SpectateCommand() {
    }

    @Command(names={"event spec", "event spectate"}, permission="", description="Spectate the ongoing event")
    @JvmStatic
    public static final void execute(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (PotPvP.getInstance().gameHandler.getOngoingGame() == null) {
            player.sendMessage(ChatColor.RED + "There is no ongoing event.");
            return;
        }
        if (!PotPvP.getInstance().lobbyHandler.isInLobby(player)) {
            player.sendMessage(ChatColor.RED + "You must start spectating from the lobby.");
            return;
        }
        if (PotPvP.getInstance().partyHandler.hasParty(player)) {
            player.sendMessage(ChatColor.RED + "You can't spectate the event whilst in a party.");
            return;
        }
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        Intrinsics.checkNotNull(game);
        Game game2 = game;
        if (game2.getPlayers().contains(player)) {
            return;
        }
        if (game2.getState() != GameState.RUNNING) {
            player.sendMessage(ChatColor.RED + "You can't spectate an event that is starting or ending.");
            return;
        }
        game2.addSpectator(player);
    }
}

