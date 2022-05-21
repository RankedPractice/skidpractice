/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.scoreboard.ScoreGetter
 *  rip.bridge.qlib.util.LinkedList
 */
package cc.fyre.potpvp.scoreboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import java.util.function.BiConsumer;
import org.bukkit.entity.Player;
import rip.bridge.qlib.scoreboard.ScoreGetter;
import rip.bridge.qlib.util.LinkedList;

final class MultiplexingScoreGetter
implements ScoreGetter {
    private final BiConsumer<Player, LinkedList<String>> matchScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> lobbyScoreGetter;
    private final BiConsumer<Player, LinkedList<String>> gameScoreGetter;

    MultiplexingScoreGetter(BiConsumer<Player, LinkedList<String>> matchScoreGetter, BiConsumer<Player, LinkedList<String>> lobbyScoreGetter, BiConsumer<Player, LinkedList<String>> gameScoreGetter) {
        this.matchScoreGetter = matchScoreGetter;
        this.lobbyScoreGetter = lobbyScoreGetter;
        this.gameScoreGetter = gameScoreGetter;
    }

    public void getScores(LinkedList<String> scores, Player player) {
        if (PotPvP.getInstance() == null) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        if (settingHandler.getSetting(player, Setting.SHOW_SCOREBOARD)) {
            if (matchHandler.isPlayingOrSpectatingMatch(player)) {
                this.matchScoreGetter.accept(player, scores);
            } else {
                Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
                if (game != null && game.getPlayers().contains(player) && game.getState() != GameState.ENDED) {
                    this.gameScoreGetter.accept(player, scores);
                } else {
                    this.lobbyScoreGetter.accept(player, scores);
                }
            }
        }
        if (!scores.isEmpty()) {
            scores.addFirst((Object)"&a&7&m--------------------");
            scores.add((Object)"");
            scores.add((Object)"&balphadevs.xyz");
            scores.add((Object)"&f&7&m--------------------");
        }
    }
}

