/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  mkremins.fanciful.FancyMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.tournament;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.command.Command;

public class Tournament {
    private int currentRound = -1;
    private int requiredPartiesToStart;
    private List<Party> activeParties = Lists.newArrayList();
    private List<Party> lost = Lists.newArrayList();
    private int requiredPartySize;
    private KitType type;
    private List<Match> matches = Lists.newArrayList();
    private int beginNextRoundIn = 31;
    private Map<UUID, Party> partyMap = Maps.newHashMap();
    private TournamentStage stage = TournamentStage.WAITING_FOR_TEAMS;
    private long roundStartedAt;

    public Tournament(KitType type2, int partySize, int requiredPartiesToStart) {
        this.type = type2;
        this.requiredPartySize = partySize;
        this.requiredPartiesToStart = requiredPartiesToStart;
    }

    public void addParty(Party party) {
        this.activeParties.add(party);
        this.checkActiveParties();
        this.joinedTournament(party);
        this.checkStart();
    }

    public boolean isInTournament(Party party) {
        return this.activeParties.contains(party);
    }

    public void check() {
        this.checkActiveParties();
        this.populatePartyMap();
        this.checkMatches();
        if (this.matches.stream().anyMatch(s -> s != null && s.getState() != MatchState.TERMINATED)) {
            return;
        }
        this.matches.clear();
        if (this.currentRound == -1) {
            return;
        }
        if (this.activeParties.isEmpty()) {
            if (this.lost.isEmpty()) {
                this.stage = TournamentStage.FINISHED;
                PotPvP.getInstance().getTournamentHandler().setTournament(null);
                return;
            }
            Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&cThe tournament's last two teams forfeited. Winner by default: " + PatchedPlayerUtils.getFormattedName(this.lost.get(this.lost.size() - 1).getLeader()) + "'s team!")));
            PotPvP.getInstance().getTournamentHandler().setTournament(null);
            this.stage = TournamentStage.FINISHED;
            return;
        }
        if (this.activeParties.size() == 1) {
            Party party = this.activeParties.get(0);
            if (party.getMembers().size() == 1) {
                this.repeatMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6&l" + PatchedPlayerUtils.getFormattedName(party.getLeader()) + " &7won the tournament!")), 4, 2);
            } else if (party.getMembers().size() == 2) {
                Iterator<UUID> membersIterator = party.getMembers().iterator();
                UUID[] members2 = new UUID[]{membersIterator.next(), membersIterator.next()};
                this.repeatMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6&l" + PatchedPlayerUtils.getFormattedName(members2[0]) + " &7and &6&l" + PatchedPlayerUtils.getFormattedName(members2[1]) + " &7won the tournament!")), 4, 2);
            } else {
                this.repeatMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6&l" + PatchedPlayerUtils.getFormattedName(party.getLeader()) + "&7's team won the tournament!")), 4, 2);
            }
            this.activeParties.clear();
            PotPvP.getInstance().getTournamentHandler().setTournament(null);
            this.stage = TournamentStage.FINISHED;
            return;
        }
        if (--this.beginNextRoundIn >= 1) {
            switch (this.beginNextRoundIn) {
                case 1: 
                case 2: 
                case 3: 
                case 4: 
                case 5: 
                case 10: 
                case 15: 
                case 30: {
                    if (this.currentRound == 0) {
                        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&7The &6&ltournament &7will begin in &6" + this.beginNextRoundIn + " &7second" + (this.beginNextRoundIn == 1 ? "" : "s") + ".")));
                        break;
                    }
                    Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&6&lRound " + (this.currentRound + 1) + " &7will begin in &6" + this.beginNextRoundIn + " &7second" + (this.beginNextRoundIn == 1 ? "" : "s") + ".")));
                }
            }
            this.stage = TournamentStage.COUNTDOWN;
            return;
        }
        this.startRound();
    }

    private void checkActiveParties() {
        Set realParties = PotPvP.getInstance().getPartyHandler().getParties().stream().map(p -> p.getPartyId()).collect(Collectors.toSet());
        Iterator<Party> activePartyIterator = this.activeParties.iterator();
        while (activePartyIterator.hasNext()) {
            Party activeParty = activePartyIterator.next();
            if (realParties.contains(activeParty.getPartyId())) continue;
            activePartyIterator.remove();
            if (this.lost.contains(activeParty)) continue;
            this.lost.add(activeParty);
        }
    }

    private void repeatMessage(final String message, final int times, int interval) {
        new BukkitRunnable(){
            private int runs;
            {
                this.runs = times;
            }

            public void run() {
                if (0 <= --this.runs) {
                    Bukkit.broadcastMessage((String)message);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 0L, (long)(interval * 20));
    }

    public void checkStart() {
        if (this.activeParties.size() == this.requiredPartiesToStart) {
            this.start();
        }
    }

    public void start() {
        if (this.currentRound == -1) {
            this.currentRound = 0;
        }
    }

    private void joinedTournament(Party party) {
        this.broadcastJoinMessage(party);
    }

    private void populatePartyMap() {
        this.activeParties.forEach(p -> p.getMembers().forEach(u -> this.partyMap.put((UUID)u, (Party)p)));
    }

    private void startRound() {
        this.beginNextRoundIn = 31;
        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&6&lRound " + ++this.currentRound + " &7has begun. Good luck!")));
        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7Use &6/status &7to see who is fighting."));
        ArrayList oldPartyList = Lists.newArrayList(this.activeParties);
        while (1 < oldPartyList.size()) {
            Party firstParty = (Party)oldPartyList.remove(0);
            Party secondParty = (Party)oldPartyList.remove(0);
            this.matches.add(PotPvP.getInstance().getMatchHandler().startMatch((List<MatchTeam>)ImmutableList.of((Object)new MatchTeam(0, firstParty.getMembers()), (Object)new MatchTeam(1, secondParty.getMembers())), this.type, null, QueueType.UNRANKED, false));
        }
        if (oldPartyList.size() == 1) {
            ((Party)oldPartyList.get(0)).message(ChatColor.RED + "There were an odd number of teams in this round - so your team has advanced to the next round.");
        }
        this.stage = TournamentStage.IN_PROGRESS;
        this.roundStartedAt = System.currentTimeMillis();
    }

    private void checkMatches() {
        Iterator<Match> matchIterator = this.matches.iterator();
        while (matchIterator.hasNext()) {
            Match match = matchIterator.next();
            if (match == null) {
                matchIterator.remove();
                continue;
            }
            if (match.getState() != MatchState.TERMINATED) continue;
            MatchTeam winner = match.getWinner();
            ArrayList losers = Lists.newArrayList(match.getTeams());
            losers.remove(winner);
            MatchTeam loser = (MatchTeam)losers.get(0);
            Party loserParty = this.partyMap.get(loser.getFirstMember());
            if (loserParty == null) continue;
            this.activeParties.remove(loserParty);
            this.broadcastEliminationMessage(loserParty);
            this.lost.add(loserParty);
            matchIterator.remove();
        }
    }

    public void broadcastJoinMessage() {
        int multiplier;
        int teamSize = this.getRequiredPartySize();
        int requiredTeams = this.getRequiredPartiesToStart();
        int n = multiplier = teamSize < 3 ? teamSize : 1;
        if (this.getCurrentRound() != -1) {
            return;
        }
        Bukkit.broadcastMessage((String)"");
        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)("&7A &6&ltournament&7 has started. Type &6/join&7 to play. (" + this.activeParties.size() * multiplier + "/" + requiredTeams * multiplier + ")")));
        Bukkit.broadcastMessage((String)"");
    }

    private void broadcastJoinMessage(Party joiningParty) {
        FancyMessage message;
        if (this.getCurrentRound() != -1) {
            FancyMessage message2;
            if (joiningParty.getMembers().size() == 1) {
                message2 = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(PatchedPlayerUtils.getFormattedName(joiningParty.getLeader()) + " &7has &7joined &7the &6tournament&7. &7(" + this.activeParties.size() + "/" + this.requiredPartiesToStart + "&7)")));
            } else if (joiningParty.getMembers().size() == 2) {
                Iterator<UUID> membersIterator = joiningParty.getMembers().iterator();
                message2 = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(PatchedPlayerUtils.getFormattedName(membersIterator.next()) + " &7and &6" + PatchedPlayerUtils.getFormattedName(membersIterator.next()) + "&7 have joined the &6tournament&7. &7(" + this.activeParties.size() * 2 + "/" + this.requiredPartiesToStart * 2 + "&7)")));
            } else {
                message2 = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(PatchedPlayerUtils.getFormattedName(joiningParty.getLeader()) + "&7's team has joined the &6tournament&7. &7(" + this.activeParties.size() + "/" + this.requiredPartiesToStart + "&7)")));
            }
            Bukkit.getOnlinePlayers().forEach(arg_0 -> ((FancyMessage)message2).send(arg_0));
            return;
        }
        if (joiningParty.getMembers().size() == 1) {
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(joiningParty.getLeader()) + "&7 has joined the &6tournament&7. &7(" + this.activeParties.size() + "/" + this.requiredPartiesToStart + "&7)")));
        } else if (joiningParty.getMembers().size() == 2) {
            Iterator<UUID> membersIterator = joiningParty.getMembers().iterator();
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(membersIterator.next()) + "&7 and &6" + PatchedPlayerUtils.getFormattedName(membersIterator.next()) + "&7 have joined the &6tournament&7. &7(" + this.activeParties.size() * 2 + "/" + this.requiredPartiesToStart * 2 + "&7)")));
        } else {
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(joiningParty.getLeader()) + "&7's team has joined the &6tournament&7. &7(" + this.activeParties.size() + "/" + this.requiredPartiesToStart + "&7)")));
        }
        message.command("/djm");
        message.tooltip(ChatColor.translateAlternateColorCodes((char)'&', (String)"&6&lCLICK &7to hide this message."));
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!joiningParty.isMember(player.getUniqueId()) && !settingHandler.getSetting(player, Setting.SEE_TOURNAMENT_JOIN_MESSAGE)) continue;
            message.send(player);
        }
    }

    private void broadcastEliminationMessage(Party loserParty) {
        FancyMessage message;
        int multiplier;
        int n = multiplier = this.requiredPartySize < 3 ? this.requiredPartySize : 1;
        if (loserParty.getMembers().size() == 1) {
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(loserParty.getLeader()) + "&7 has been eliminated. &7(" + this.activeParties.size() * multiplier + "/" + this.requiredPartiesToStart * multiplier + "&7)")));
        } else if (loserParty.getMembers().size() == 2) {
            Iterator<UUID> membersIterator = loserParty.getMembers().iterator();
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(membersIterator.next()) + "&7 and &6" + PatchedPlayerUtils.getFormattedName(membersIterator.next()) + " &7were eliminated. &7(" + this.activeParties.size() * multiplier + "/" + this.requiredPartiesToStart * multiplier + "&7)")));
        } else {
            message = new FancyMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&6" + PatchedPlayerUtils.getFormattedName(loserParty.getLeader()) + "&7's team has been eliminated. &7(" + this.activeParties.size() * multiplier + "/" + this.requiredPartiesToStart * multiplier + "&7)")));
        }
        message.command("/dem");
        message.tooltip(ChatColor.translateAlternateColorCodes((char)'&', (String)"&6&lCLICK &7to hide this message."));
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!loserParty.isMember(player.getUniqueId()) && !settingHandler.getSetting(player, Setting.SEE_TOURNAMENT_ELIMINATION_MESSAGES)) continue;
            message.send(player);
        }
    }

    @Command(names={"djm"}, permission="")
    public static void joinMessages(Player sender) {
        boolean oldValue = PotPvP.getInstance().getSettingHandler().getSetting(sender, Setting.SEE_TOURNAMENT_JOIN_MESSAGE);
        if (!oldValue) {
            sender.sendMessage(ChatColor.RED + "You have already disabled tournament join messages.");
            return;
        }
        PotPvP.getInstance().getSettingHandler().updateSetting(sender, Setting.SEE_TOURNAMENT_JOIN_MESSAGE, false);
        sender.sendMessage(ChatColor.GREEN + "Disabled tournament join messages.");
    }

    @Command(names={"dem"}, permission="")
    public static void eliminationMessages(Player sender) {
        boolean oldValue = PotPvP.getInstance().getSettingHandler().getSetting(sender, Setting.SEE_TOURNAMENT_ELIMINATION_MESSAGES);
        if (!oldValue) {
            sender.sendMessage(ChatColor.RED + "You have already disabled tournament elimination messages.");
            return;
        }
        PotPvP.getInstance().getSettingHandler().updateSetting(sender, Setting.SEE_TOURNAMENT_ELIMINATION_MESSAGES, false);
        sender.sendMessage(ChatColor.GREEN + "Disabled tournament elimination messages.");
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public int getRequiredPartiesToStart() {
        return this.requiredPartiesToStart;
    }

    public List<Party> getActiveParties() {
        return this.activeParties;
    }

    public int getRequiredPartySize() {
        return this.requiredPartySize;
    }

    public KitType getType() {
        return this.type;
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    public int getBeginNextRoundIn() {
        return this.beginNextRoundIn;
    }

    public TournamentStage getStage() {
        return this.stage;
    }

    public long getRoundStartedAt() {
        return this.roundStartedAt;
    }

    public static enum TournamentStage {
        WAITING_FOR_TEAMS,
        COUNTDOWN,
        IN_PROGRESS,
        FINISHED;

    }
}

