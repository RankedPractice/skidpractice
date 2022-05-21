/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.match.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import me.jumper251.replay.replaysystem.replaying.ReplayHelper;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class LeaveCommand {
    @Command(names={"spawn", "leave"}, permission="")
    public static void leave(Player sender) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isPlayingMatch(sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot do this while playing in a match.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn...");
        Match spectating = matchHandler.getMatchSpectating(sender);
        if (spectating == null) {
            PotPvP.getInstance().getLobbyHandler().returnToLobby(sender);
        } else {
            spectating.removeSpectator(sender);
        }
        Replayer replay = ReplayHelper.replaySessions.get(sender.getName());
        if (replay == null) {
            return;
        }
        replay.stop();
    }
}

