/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  rip.bridge.qlib.scoreboard.ScoreGetter
 *  rip.bridge.qlib.scoreboard.ScoreboardConfiguration
 *  rip.bridge.qlib.scoreboard.TitleGetter
 */
package cc.fyre.potpvp.scoreboard;

import rip.bridge.qlib.scoreboard.ScoreGetter;
import rip.bridge.qlib.scoreboard.ScoreboardConfiguration;
import rip.bridge.qlib.scoreboard.TitleGetter;

public final class PotPvPScoreboardConfiguration {
    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();
        configuration.setTitleGetter(new TitleGetter("&b&lEast Practice"));
        configuration.setScoreGetter((ScoreGetter)new MultiplexingScoreGetter(new MatchScoreGetter(), new LobbyScoreGetter()));
        return configuration;
    }
}

