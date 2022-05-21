/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  org.bukkit.ChatColor
 */
package cc.fyre.potpvp.queue;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.queue.MatchQueueEntry;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bukkit.ChatColor;

public final class MatchQueue {
    private final KitType kitType;
    private final QueueType queueType;
    private final List<MatchQueueEntry> entries = new CopyOnWriteArrayList<MatchQueueEntry>();

    MatchQueue(KitType kitType, QueueType queueType) {
        this.kitType = (KitType)Preconditions.checkNotNull(kitType, "kitType");
        this.queueType = queueType;
    }

    void tick() {
        ArrayList<MatchQueueEntry> entriesCopy = new ArrayList<MatchQueueEntry>(this.entries);
        EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
        if (this.queueType.isRanked()) {
            entriesCopy.sort(Comparator.comparing(e -> eloHandler.getElo(e.getMembers(), this.kitType)));
        }
        while (entriesCopy.size() >= 2) {
            int bEloWindow;
            int aEloWindow;
            int bElo;
            int aElo;
            MatchQueueEntry a = (MatchQueueEntry)entriesCopy.remove(0);
            MatchQueueEntry b = (MatchQueueEntry)entriesCopy.remove(0);
            if (this.queueType.isRanked()) {
                aElo = eloHandler.getElo(a.getMembers(), this.kitType);
                bElo = eloHandler.getElo(b.getMembers(), this.kitType);
                aEloWindow = a.getWaitSeconds() * 5;
                bEloWindow = b.getWaitSeconds() * 5;
                if (Math.abs(aElo - bElo) > Math.max(aEloWindow, bEloWindow)) {
                    continue;
                }
            } else if (this.queueType.isPremium()) {
                aElo = eloHandler.getPremiumElo(a.getMembers(), this.kitType);
                bElo = eloHandler.getPremiumElo(b.getMembers(), this.kitType);
                aEloWindow = a.getWaitSeconds() * 5;
                bEloWindow = b.getWaitSeconds() * 5;
                if (Math.abs(aElo - bElo) > Math.max(aEloWindow, bEloWindow)) continue;
            }
            this.createMatchAndRemoveEntries(a, b);
        }
    }

    public int countPlayersQueued() {
        int count = 0;
        for (MatchQueueEntry entry : this.entries) {
            count += entry.getMembers().size();
        }
        return count;
    }

    void addToQueue(MatchQueueEntry entry) {
        this.entries.add(entry);
    }

    void removeFromQueue(MatchQueueEntry entry) {
        this.entries.remove(entry);
    }

    private void createMatchAndRemoveEntries(MatchQueueEntry entryA, MatchQueueEntry entryB) {
        MatchTeam teamB;
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        MatchTeam teamA = new MatchTeam(0, entryA.getMembers());
        Match match = matchHandler.startMatch(ImmutableList.of(teamA, (teamB = new MatchTeam(1, entryB.getMembers()))), this.kitType, null, this.queueType, this.queueType.isUnranked());
        if (match != null) {
            EloHandler eloHandler;
            queueHandler.removeFromQueueCache(entryA);
            queueHandler.removeFromQueueCache(entryB);
            String teamAElo = "";
            String teamBElo = "";
            if (this.queueType.isRanked()) {
                eloHandler = PotPvP.getInstance().getEloHandler();
                teamAElo = " (" + eloHandler.getElo(teamA.getAliveMembers(), this.kitType) + " Elo)";
                teamBElo = " (" + eloHandler.getElo(teamB.getAliveMembers(), this.kitType) + " Elo)";
            } else if (this.queueType.isPremium()) {
                eloHandler = PotPvP.getInstance().getEloHandler();
                teamAElo = " (" + eloHandler.getPremiumElo(teamA.getAliveMembers(), this.kitType) + " Elo)";
                teamBElo = " (" + eloHandler.getPremiumElo(teamB.getAliveMembers(), this.kitType) + " Elo)";
            }
            String foundStart = ChatColor.YELLOW + "Starting match against " + ChatColor.GREEN;
            teamA.messageAlive(foundStart + Joiner.on((String)", ").join(PatchedPlayerUtils.mapToNames(teamB.getAllMembers())) + teamBElo);
            teamB.messageAlive(foundStart + Joiner.on((String)", ").join(PatchedPlayerUtils.mapToNames(teamA.getAllMembers())) + teamAElo);
            this.entries.remove(entryA);
            this.entries.remove(entryB);
        }
    }

    public KitType getKitType() {
        return this.kitType;
    }

    public QueueType getQueueType() {
        return this.queueType;
    }
}

