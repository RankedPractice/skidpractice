/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.scoreboard.ScoreFunction
 *  rip.bridge.qlib.util.LinkedList
 *  rip.bridge.qlib.util.PlayerUtils
 *  rip.bridge.qlib.util.TimeUtils
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.scoreboard;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.kittype.HealingMethod;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.pvpclasses.pvpclasses.ArcherClass;
import cc.fyre.potpvp.pvpclasses.pvpclasses.BardClass;
import cc.fyre.potpvp.util.BridgeUtil;
import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.scoreboard.ScoreFunction;
import rip.bridge.qlib.util.LinkedList;
import rip.bridge.qlib.util.PlayerUtils;
import rip.bridge.qlib.util.TimeUtils;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class MatchScoreGetter
implements BiConsumer<Player, LinkedList<String>> {
    private Map<UUID, Integer> healsLeft = ImmutableMap.of();

    MatchScoreGetter() {
        Bukkit.getScheduler().runTaskTimer((Plugin)PotPvP.getInstance(), () -> {
            MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
            HashMap<UUID, Integer> newHealsLeft = new HashMap<UUID, Integer>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                HealingMethod healingMethod;
                Match playing = matchHandler.getMatchPlaying(player);
                if (playing == null || (healingMethod = playing.getKitType().getHealingMethod()) == null) continue;
                int count = healingMethod.count(player.getInventory().getContents());
                newHealsLeft.put(player.getUniqueId(), count);
            }
            this.healsLeft = newHealsLeft;
        }, 10L, 10L);
    }

    @Override
    public void accept(Player player, LinkedList<String> scores) {
        Optional<UUID> followingOpt = PotPvP.getInstance().getFollowHandler().getFollowing(player);
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(player);
        if (match == null) {
            if (followingOpt.isPresent()) {
                scores.add(("&fFollowing: &6" + FrozenUUIDCache.name((UUID)followingOpt.get())));
            }
            if (player.hasMetadata("ModMode")) {
                scores.add("&6Silent Mode Enabled");
            }
            return;
        }
        boolean participant = match.getTeam(player.getUniqueId()) != null;
        boolean renderPing = false;
        if (participant) {
            renderPing = this.renderParticipantLines((List<String>)scores, match, player);
        } else {
            MatchTeam previousTeam = match.getPreviousTeam(player.getUniqueId());
            this.renderSpectatorLines((List<String>)scores, match, previousTeam);
        }
        if (renderPing) {
            this.renderPingLines((List<String>)scores, match, player);
        }
        if (followingOpt.isPresent()) {
            scores.add(("&fFollowing: &6" + FrozenUUIDCache.name((UUID)followingOpt.get())));
        }
        if (player.hasMetadata("ModMode")) {
            scores.add("&6Silent Mode Enabled");
        }
    }

    private boolean renderParticipantLines(List<String> scores, Match match, Player player) {
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() != 2) {
            return false;
        }
        MatchTeam ourTeam = match.getTeam(player.getUniqueId());
        MatchTeam otherTeam = teams.get(0) == ourTeam ? teams.get(1) : teams.get(0);
        int ourTeamSize = ourTeam.getAllMembers().size();
        int otherTeamSize = otherTeam.getAllMembers().size();
        if (ourTeamSize == 1 && otherTeamSize == 1) {
            if (!match.getKitType().getId().equals("Bridges")) {
                this.render1v1MatchLines(scores, otherTeam);
            }
        } else if (ourTeamSize <= 2 && otherTeamSize <= 2) {
            this.render2v2MatchLines(scores, ourTeam, otherTeam, player, match.getKitType().getHealingMethod());
        } else if (ourTeamSize <= 4 && otherTeamSize <= 4) {
            this.render4v4MatchLines(scores, ourTeam, otherTeam);
        } else if (ourTeam.getAllMembers().size() <= 9) {
            this.renderLargeMatchLines(scores, ourTeam, otherTeam);
        } else {
            this.renderJumboMatchLines(scores, ourTeam, otherTeam);
        }
        String archerMarkScore = this.getArcherMarkScore(player);
        String bardEffectScore = this.getBardEffectScore(player);
        String bardEnergyScore = this.getBardEnergyScore(player);
        if (archerMarkScore != null) {
            scores.add("&6&lArcher Mark&7: &c" + archerMarkScore);
        }
        if (bardEffectScore != null) {
            scores.add("&a&lBard Effect&7: &c" + bardEffectScore);
        }
        if (bardEnergyScore != null) {
            scores.add("&b&lBard Energy&7: &c" + bardEnergyScore);
        }
        if (match.getKitType().getId().equalsIgnoreCase("Boxing")) {
            this.renderBoxingLines(scores, ourTeam, otherTeam);
        }
        if (match.getKitType().getId().equalsIgnoreCase("MLGRUSH")) {
            this.renderMlgLines(scores, ourTeam, otherTeam);
        }
        if (match.getKitType().getId().equalsIgnoreCase("Bridges")) {
            this.renderBridgesLines(scores, ourTeam, otherTeam, player);
        }
        if (match.getKitType().getId().equalsIgnoreCase("bedfight")) {
            this.renderBedFightLines(scores, ourTeam, otherTeam, player);
        }
        Player other = Bukkit.getPlayer((UUID)otherTeam.getFirstMember());
        scores.add("");
        scores.add("&aYour Ping: &f" + PlayerUtils.getPing((Player)player) + "ms");
        if (other != null) {
            scores.add("&cTheir Ping: &f" + PlayerUtils.getPing((Player)other) + "ms");
        }
        return false;
    }

    private void renderBoxingLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(Bukkit.getPlayer((UUID)ourTeam.getFirstMember()));
        int[] ourHits = new int[]{0};
        int[] theirHits = new int[]{0};
        ourTeam.getAllMembers().forEach(u -> {
            ourHits[0] = match.getBoxingHits().getOrDefault(u, 0);
        });
        otherTeam.getAllMembers().forEach(u -> {
            theirHits[0] = match.getBoxingHits().getOrDefault(u, 0);
        });
        scores.add("&bHits: " + (ourHits[0] >= theirHits[0] ? "&a(+" : "&c(") + (ourHits[0] - theirHits[0]) + ")");
        if (match.getAllPlayers().size() > 2) {
            scores.add(" &aYour team: &f" + ourHits[0]);
            scores.add(" &cTheir team: &f" + theirHits[0]);
        } else {
            scores.add(" &aYou: &f" + ourHits[0] + (match.getCombos().getOrDefault(ourTeam.getFirstMember(), 0) > 0 && match.getState() != MatchState.ENDING ? " &e(" + match.getCombos().get(ourTeam.getFirstMember()) + " Combo)" : ""));
            scores.add(" &cThem: &f" + theirHits[0] + (match.getCombos().getOrDefault(otherTeam.getFirstMember(), 0) > 0 && match.getState() != MatchState.ENDING ? " &e(" + match.getCombos().get(otherTeam.getFirstMember()) + " Combo)" : ""));
        }
    }

    private void renderBridgesLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam, Player player) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(Bukkit.getPlayer((UUID)player.getUniqueId()));
        if (match.getKitType().getId().equalsIgnoreCase("BRIDGES")) {
            scores.add("&9[B]&7: " + BridgeUtil.barBuilder(match.getWins().get(match.getTeams().get(1)), "&9"));
            scores.add("&c[R]&7: " + BridgeUtil.barBuilder(match.getWins().get(match.getTeams().get(0)), "&c"));
        }
    }

    private void renderBedFightLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam, Player player) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(Bukkit.getPlayer((UUID)player.getUniqueId()));
        if (match.getKitType().getId().equalsIgnoreCase("BEDFIGHT")) {
            if (ourTeam.getId() == 0) {
                scores.add("&9[B]&7: " + (ourTeam.isBedBroken() ? "&c\u2717" : "&a\u2713"));
                scores.add("&c[R]&7: " + (otherTeam.isBedBroken() ? "&c\u2717" : "&a\u2713"));
            } else {
                scores.add("&c[R]&7: " + (ourTeam.isBedBroken() ? "&c\u2717" : "&a\u2713"));
                scores.add("&9[B]&7: " + (otherTeam.isBedBroken() ? "&c\u2717" : "&a\u2713"));
            }
        }
    }

    private void renderMlgLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam) {
        String firstTeam = ourTeam.getId() == 0 ? "&c&lRED" : "&9&lBLUE";
        scores.add(firstTeam);
        scores.add("&7" + ourTeam.getPoints() + "&8/&75");
        String secondTeam = otherTeam.getId() == 0 ? "&c&lRED" : "&9&lBLUE";
        scores.add(secondTeam);
        scores.add("&b&7" + otherTeam.getPoints() + "&8/&75");
    }

    private void renderBoxingSpectatingLines(List<String> scores, MatchTeam teamOne, MatchTeam teamTwo) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(Bukkit.getPlayer((UUID)teamOne.getFirstMember()));
        int[] ourHits = new int[]{0};
        int[] theirHits = new int[]{0};
        teamOne.getAllMembers().forEach(u -> {
            ourHits[0] = match.getBoxingHits().getOrDefault(u, 0);
        });
        teamTwo.getAllMembers().forEach(u -> {
            theirHits[0] = match.getBoxingHits().getOrDefault(u, 0);
        });
        scores.add("&6Hits: " + (ourHits[0] >= theirHits[0] ? "&a(+" : "&c(") + (ourHits[0] - theirHits[0]) + ")");
        if (match.getAllPlayers().size() > 2) {
            scores.add(" &bTeam One: &f" + ourHits[0]);
            scores.add(" &dTeam Two: &f" + theirHits[0]);
        } else {
            scores.add(" &b" + Bukkit.getPlayer((UUID)teamOne.getFirstMember()).getDisguisedName() + ": &f" + ourHits[0] + (match.getCombos().getOrDefault(teamOne.getFirstMember(), 0) > 0 && match.getState() != MatchState.ENDING ? " &e(" + match.getCombos().get(teamOne.getFirstMember()) + " Combo)" : ""));
            scores.add(" &d " + Bukkit.getPlayer((UUID)teamTwo.getFirstMember()).getDisguisedName() + ": &f" + theirHits[0] + (match.getCombos().getOrDefault(teamTwo.getFirstMember(), 0) > 0 && match.getState() != MatchState.ENDING ? " &e(" + match.getCombos().get(teamTwo.getFirstMember()) + " Combo)" : ""));
        }
    }

    private void render1v1MatchLines(List<String> scores, MatchTeam otherTeam) {
        Player player = Bukkit.getPlayer((UUID)otherTeam.getFirstMember());
        String playerName = player == null ? FrozenUUIDCache.name((UUID)otherTeam.getFirstMember()) : player.getDisguisedName();
        scores.add("&bFighting: &f" + playerName);
    }

    private void render2v2MatchLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam, Player player, HealingMethod healingMethod) {
        UUID partnerUuid = null;
        for (UUID teamMember : ourTeam.getAllMembers()) {
            if (teamMember == player.getUniqueId()) continue;
            partnerUuid = teamMember;
            break;
        }
        if (partnerUuid != null) {
            String healsStr;
            String healthStr;
            String namePrefix;
            if (ourTeam.isAlive(partnerUuid)) {
                Player partnerPlayer = Bukkit.getPlayer(partnerUuid);
                double health = (double)Math.round(partnerPlayer.getHealth()) / 2.0;
                int heals = this.healsLeft.getOrDefault(partnerUuid, 0);
                ChatColor healthColor = health > 8.0 ? ChatColor.GREEN : (health > 6.0 ? ChatColor.YELLOW : (health > 4.0 ? ChatColor.GOLD : (health > 1.0 ? ChatColor.RED : ChatColor.DARK_RED)));
                ChatColor healsColor = heals > 20 ? ChatColor.GREEN : (heals > 12 ? ChatColor.YELLOW : (heals > 8 ? ChatColor.GOLD : (heals > 3 ? ChatColor.RED : ChatColor.DARK_RED)));
                namePrefix = "&a";
                healthStr = healthColor.toString() + health + " *\u2764*" + ChatColor.GRAY;
                healsStr = healingMethod != null ? " &l\u23d0 " + healsColor.toString() + heals + " " + (heals == 1 ? healingMethod.getShortSingular() : healingMethod.getShortPlural()) : "";
            } else {
                namePrefix = "&7&m";
                healthStr = "&4RIP";
                healsStr = "";
            }
            scores.add(namePrefix + FrozenUUIDCache.name((UUID)partnerUuid));
            scores.add(healthStr + healsStr);
            scores.add("&b");
        }
        scores.add("&c&lOpponents");
        scores.addAll(this.renderTeamMemberOverviewLines(otherTeam));
        if (PotPvP.getInstance().getMatchHandler().getMatchPlaying(player).getState() == MatchState.IN_PROGRESS) {
            scores.add("&c");
        }
    }

    private void render4v4MatchLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam) {
        scores.add("&aTeam &a(" + ourTeam.getAliveMembers().size() + "/" + ourTeam.getAllMembers().size() + ")");
        scores.addAll(this.renderTeamMemberOverviewLinesWithHearts(ourTeam));
        scores.add("&b");
        scores.add("&cOpponents &c(" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size() + ")");
        scores.addAll(this.renderTeamMemberOverviewLines(otherTeam));
        if (PotPvP.getInstance().getMatchHandler().getMatchPlaying(Bukkit.getPlayer((UUID)ourTeam.getFirstAliveMember())).getState() == MatchState.IN_PROGRESS) {
            scores.add("&c");
        }
    }

    private void renderLargeMatchLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam) {
        scores.add("&aTeam &a(" + ourTeam.getAliveMembers().size() + "/" + ourTeam.getAllMembers().size() + ")");
        scores.addAll(this.renderTeamMemberOverviewLinesWithHearts(ourTeam));
        scores.add("&b");
        scores.add("&cOpponents: &f" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size());
    }

    private void renderJumboMatchLines(List<String> scores, MatchTeam ourTeam, MatchTeam otherTeam) {
        scores.add("&aTeam: &f" + ourTeam.getAliveMembers().size() + "/" + ourTeam.getAllMembers().size());
        scores.add("&cOpponents: &f" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size());
    }

    private void renderSpectatorLines(List<String> scores, Match match, MatchTeam oldTeam) {
        String rankedStr = match.getQueueType().isRanked() ? " (R)" : (match.getQueueType().isPremium() ? " (P)" : "");
        scores.add("Kit: &6" + match.getKitType().getDisplayName() + rankedStr);
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() == 2) {
            MatchTeam teamOne = teams.get(0);
            MatchTeam teamTwo = teams.get(1);
            if (match.getKitType().getId().equals("Boxing") && match.getSpectators().size() != 0) {
                this.renderBoxingSpectatingLines(scores, teamOne, teamTwo);
            }
            if (teamOne.getAllMembers().size() != 1 && teamTwo.getAllMembers().size() != 1) {
                if (oldTeam == null) {
                    scores.add("&5Team One: &f" + teamOne.getAliveMembers().size() + "/" + teamOne.getAllMembers().size());
                    scores.add("&bTeam Two: &f" + teamTwo.getAliveMembers().size() + "/" + teamTwo.getAllMembers().size());
                } else {
                    MatchTeam otherTeam = oldTeam == teamOne ? teamTwo : teamOne;
                    scores.add("&aTeam: &f" + oldTeam.getAliveMembers().size() + "/" + oldTeam.getAllMembers().size());
                    scores.add("&cOpponents: &f" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size());
                }
            }
        }
    }

    private void renderMetaLines(List<String> scores, Match match, boolean participant) {
        Date startedAt = match.getStartedAt();
        Date endedAt = match.getEndedAt();
        if (startedAt == null) {
            scores.add("&fDuration: &600:00");
            return;
        }
        String formattedDuration = TimeUtils.formatIntoMMSS((int)((int)ChronoUnit.SECONDS.between(startedAt.toInstant(), endedAt == null ? Instant.now() : endedAt.toInstant())));
        scores.add("&fDuration: &6" + formattedDuration);
    }

    private void renderPingLines(List<String> scores, Match match, Player ourPlayer) {
        if (Boolean.TRUE.booleanValue()) {
            return;
        }
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() == 2) {
            MatchTeam firstTeam = teams.get(0);
            MatchTeam secondTeam = teams.get(1);
            Set<UUID> firstTeamPlayers = firstTeam.getAllMembers();
            Set<UUID> secondTeamPlayers = secondTeam.getAllMembers();
            if (firstTeamPlayers.size() == 1 && secondTeamPlayers.size() == 1) {
                scores.add("&7&b&4");
                scores.add("&5Your Ping: &7" + PlayerUtils.getPing((Player)ourPlayer));
                Player otherPlayer = Bukkit.getPlayer((UUID)(match.getTeam(ourPlayer.getUniqueId()) == firstTeam ? secondTeam.getFirstMember() : firstTeam.getFirstMember()));
                if (otherPlayer == null) {
                    return;
                }
                scores.add("&5Their Ping: &7" + PlayerUtils.getPing((Player)otherPlayer));
            }
        }
    }

    private List<String> renderTeamMemberOverviewLinesWithHearts(MatchTeam team) {
        ArrayList<String> aliveLines = new ArrayList<String>();
        ArrayList<String> deadLines = new ArrayList<String>();
        for (UUID teamMember : team.getAllMembers()) {
            if (team.isAlive(teamMember)) {
                aliveLines.add(" &f" + FrozenUUIDCache.name((UUID)teamMember) + " " + this.getHeartString(team, teamMember));
                continue;
            }
            deadLines.add(" &7&m" + FrozenUUIDCache.name((UUID)teamMember));
        }
        ArrayList<String> result2 = new ArrayList<String>();
        result2.addAll(aliveLines);
        result2.addAll(deadLines);
        return result2;
    }

    private List<String> renderTeamMemberOverviewLines(MatchTeam team) {
        ArrayList<String> aliveLines = new ArrayList<String>();
        ArrayList<String> deadLines = new ArrayList<String>();
        for (UUID teamMember : team.getAllMembers()) {
            if (team.isAlive(teamMember)) {
                aliveLines.add(" &f" + FrozenUUIDCache.name((UUID)teamMember));
                continue;
            }
            deadLines.add(" &7&m" + FrozenUUIDCache.name((UUID)teamMember));
        }
        ArrayList<String> result2 = new ArrayList<String>();
        result2.addAll(aliveLines);
        result2.addAll(deadLines);
        return result2;
    }

    private String getHeartString(MatchTeam ourTeam, UUID partnerUuid) {
        if (partnerUuid != null) {
            String healthStr;
            if (ourTeam.isAlive(partnerUuid)) {
                Player partnerPlayer = Bukkit.getPlayer((UUID)partnerUuid);
                double health = (double)Math.round(partnerPlayer.getHealth()) / 2.0;
                ChatColor healthColor = health > 8.0 ? ChatColor.GREEN : (health > 6.0 ? ChatColor.YELLOW : (health > 4.0 ? ChatColor.GOLD : (health > 1.0 ? ChatColor.RED : ChatColor.DARK_RED)));
                healthStr = healthColor.toString() + "(" + health + " \u2764)";
            } else {
                healthStr = "&4(RIP)";
            }
            return healthStr;
        }
        return "&4(RIP)";
    }

    public String getArcherMarkScore(Player player) {
        long diff;
        if (ArcherClass.isMarked(player) && (diff = ArcherClass.getMarkedPlayers().get(player.getName()) - System.currentTimeMillis()) > 0L) {
            return ScoreFunction.TIME_FANCY.apply(Float.valueOf((float)diff / 1000.0f));
        }
        return null;
    }

    public String getBardEffectScore(Player player) {
        float diff;
        if (BardClass.getLastEffectUsage().containsKey(player.getName()) && BardClass.getLastEffectUsage().get(player.getName()) >= System.currentTimeMillis() && (diff = (float)(BardClass.getLastEffectUsage().get(player.getName()) - System.currentTimeMillis())) > 0.0f) {
            return ScoreFunction.TIME_SIMPLE.apply(Float.valueOf(diff / 1000.0f));
        }
        return null;
    }

    public String getBardEnergyScore(Player player) {
        float energy;
        if (BardClass.getEnergy().containsKey(player.getName()) && (energy = BardClass.getEnergy().get(player.getName()).floatValue()) > 0.0f) {
            return String.valueOf(BardClass.getEnergy().get(player.getName()));
        }
        return null;
    }
}

