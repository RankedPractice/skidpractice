/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.LinkedList
 */
package cc.fyre.potpvp.scoreboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import java.util.function.BiConsumer;
import org.bukkit.entity.Player;
import rip.bridge.qlib.util.LinkedList;

final class GameScoreGetter
implements BiConsumer<Player, LinkedList<String>> {
    GameScoreGetter() {
    }

    @Override
    public void accept(Player player, LinkedList<String> scores) {
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null || !game.getPlayers().contains(player)) {
            return;
        }
        scores.addAll(game.getEvent().getScoreboardScores(player, game));
    }
}

