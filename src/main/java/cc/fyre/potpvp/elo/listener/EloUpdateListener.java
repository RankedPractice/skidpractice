/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  rip.bridge.qlib.util.UUIDUtils
 */
package cc.fyre.potpvp.elo.listener;

import cc.fyre.potpvp.elo.EloCalculator;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchEndEvent;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.base.Joiner;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.bridge.qlib.util.UUIDUtils;

public final class EloUpdateListener
implements Listener {
    private static final String ELO_CHANGE_MESSAGE = ChatColor.translateAlternateColorCodes((char)'&', (String)"&eElo Changes: &a%s +%d (%d) &c%s -%d (%d)");
    private static final String PREMIUM_ELO_CHANGE_MESSAGE = ChatColor.translateAlternateColorCodes((char)'&', (String)"&ePremium Elo Changes: &a%s +%d (%d) &c%s -%d (%d)");
    private final EloHandler eloHandler;
    private final EloCalculator eloCalculator;

    public EloUpdateListener(EloHandler eloHandler, EloCalculator eloCalculator) {
        this.eloHandler = eloHandler;
        this.eloCalculator = eloCalculator;
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        Match match = event.getMatch();
        KitType kitType = match.getKitType();
        List<MatchTeam> teams = match.getTeams();
        if (!match.getQueueType().isRanked() || teams.size() != 2 || match.getWinner() == null) {
            return;
        }
        MatchTeam winnerTeam = match.getWinner();
        MatchTeam loserTeam = teams.get(0) == winnerTeam ? teams.get(1) : teams.get(0);
        EloCalculator.Result result2 = this.eloCalculator.calculate(this.eloHandler.getElo(winnerTeam.getAllMembers(), kitType), this.eloHandler.getElo(loserTeam.getAllMembers(), kitType));
        this.eloHandler.setElo(winnerTeam.getAllMembers(), kitType, result2.getWinnerNew());
        this.eloHandler.setElo(loserTeam.getAllMembers(), kitType, result2.getLoserNew());
        match.setEloChange(result2);
    }

    @EventHandler
    public void onMatchEndPremium(MatchEndEvent event) {
        Match match = event.getMatch();
        KitType kitType = match.getKitType();
        List<MatchTeam> teams = match.getTeams();
        if (!match.getQueueType().isPremium() || match.getWinner() == null) {
            return;
        }
        MatchTeam winnerTeam = match.getWinner();
        MatchTeam loserTeam = teams.get(0) == winnerTeam ? teams.get(1) : teams.get(0);
        EloCalculator.Result result2 = this.eloCalculator.calculate(this.eloHandler.getPremiumElo(winnerTeam.getAllMembers(), kitType), this.eloHandler.getPremiumElo(loserTeam.getAllMembers(), kitType));
        this.eloHandler.setPremiumElo(winnerTeam.getAllMembers(), kitType, result2.getWinnerNew());
        this.eloHandler.setPremiumElo(loserTeam.getAllMembers(), kitType, result2.getLoserNew());
        match.setEloChange(result2);
    }

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        String loserStr;
        String winnerStr;
        MatchTeam loserTeam;
        Match match = event.getMatch();
        EloCalculator.Result result2 = match.getEloChange();
        if (result2 == null) {
            return;
        }
        List<MatchTeam> teams = match.getTeams();
        MatchTeam winnerTeam = match.getWinner();
        MatchTeam matchTeam = loserTeam = teams.get(0) == winnerTeam ? teams.get(1) : teams.get(0);
        if (winnerTeam.getAllMembers().size() == 1 && loserTeam.getAllMembers().size() == 1) {
            winnerStr = UUIDUtils.name((UUID)winnerTeam.getFirstMember());
            loserStr = UUIDUtils.name((UUID)loserTeam.getFirstMember());
        } else {
            winnerStr = Joiner.on((String)", ").join(PatchedPlayerUtils.mapToNames(winnerTeam.getAllMembers()));
            loserStr = Joiner.on((String)", ").join(PatchedPlayerUtils.mapToNames(loserTeam.getAllMembers()));
        }
        if (match.getQueueType().isRanked()) {
            match.messageAll(String.format(ELO_CHANGE_MESSAGE, winnerStr, result2.getWinnerGain(), result2.getWinnerNew(), loserStr, -result2.getLoserGain(), result2.getLoserNew()));
        } else if (match.getQueueType().isPremium()) {
            match.messageAll(String.format(PREMIUM_ELO_CHANGE_MESSAGE, winnerStr, result2.getWinnerGain(), result2.getWinnerNew(), loserStr, -result2.getLoserGain(), result2.getLoserNew()));
        }
    }
}

