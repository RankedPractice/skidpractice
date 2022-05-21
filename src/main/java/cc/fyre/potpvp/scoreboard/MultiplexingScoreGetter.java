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

    MultiplexingScoreGetter(BiConsumer<Player, LinkedList<String>> matchScoreGetter, BiConsumer<Player, LinkedList<String>> lobbyScoreGetter) {
        this.matchScoreGetter = matchScoreGetter;
        this.lobbyScoreGetter = lobbyScoreGetter;
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
                /*Game game = GameQueue.INSTANCE.getCurrentGame(player);
                if (game != null && game.getPlayers().contains(player) && game.getState() != GameState.ENDED) {
                    this.gameScoreGetter.accept(player, scores);
                } else {
                    this.lobbyScoreGetter.accept(player, scores);
                }*/
            }
        }
        if (!scores.isEmpty()) {
            scores.addFirst("&a&7&m--------------------");
            scores.add("");
            scores.add("&balphadevs.xyz");
            scores.add("&f&7&m--------------------");
        }
    }
}

