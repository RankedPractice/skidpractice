/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.tab.TabLayout
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.tab;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.tab.PotPvPLayoutProvider;

import java.util.*;
import java.util.function.BiConsumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class MatchSpectatorLayoutProvider
implements BiConsumer<Player, TabLayout> {
    MatchSpectatorLayoutProvider() {
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        Match match = PotPvP.getInstance().getMatchHandler().getMatchSpectating(player);
        MatchTeam oldTeam = match.getTeam(player.getUniqueId());
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() == 2) {
            boolean duel;
            MatchTeam teamOne = teams.get(0);
            MatchTeam teamTwo = teams.get(1);
            boolean bl = duel = teamOne.getAllMembers().size() == 1 && teamTwo.getAllMembers().size() == 1;
            if (oldTeam != null) {
                MatchTeam otherTeam;
                MatchTeam ourTeam = teamOne == oldTeam ? teamOne : teamTwo;
                MatchTeam matchTeam = otherTeam = teamOne == ourTeam ? teamTwo : teamOne;
                if (!duel) {
                    tabLayout.set(0, 3, ChatColor.GREEN + ChatColor.BOLD.toString() + "Team " + ChatColor.GREEN + "(" + ourTeam.getAliveMembers().size() + "/" + ourTeam.getAllMembers().size() + ")");
                } else {
                    tabLayout.set(0, 3, ChatColor.GREEN + ChatColor.BOLD.toString() + "You");
                }
                this.renderTeamMemberOverviewEntries(tabLayout, ourTeam, 0, 4, ChatColor.GREEN);
                if (!duel) {
                    tabLayout.set(2, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Enemies " + ChatColor.RED + "(" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size() + ")");
                } else {
                    tabLayout.set(2, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Opponent");
                }
                this.renderTeamMemberOverviewEntries(tabLayout, otherTeam, 2, 4, ChatColor.RED);
            } else {
                if (!duel) {
                    tabLayout.set(0, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Team One (" + teamOne.getAliveMembers().size() + "/" + teamOne.getAllMembers().size() + ")");
                } else {
                    tabLayout.set(0, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Player One");
                }
                this.renderTeamMemberOverviewEntries(tabLayout, teamOne, 0, 4, ChatColor.RED);
                if (!duel) {
                    tabLayout.set(2, 3, ChatColor.AQUA + ChatColor.BOLD.toString() + "Team Two (" + teamTwo.getAliveMembers().size() + "/" + teamTwo.getAllMembers().size() + ")");
                } else {
                    tabLayout.set(2, 3, ChatColor.AQUA + ChatColor.BOLD.toString() + "Player Two");
                }
                this.renderTeamMemberOverviewEntries(tabLayout, teamTwo, 2, 4, ChatColor.AQUA);
            }
        } else {
            LinkedHashMap<String, Integer> deadLines;
            tabLayout.set(1, 3, ChatColor.BLUE + ChatColor.BOLD.toString() + "Party FFA");
            int x = 0;
            int y = 4;
            Map<String, Integer> entries = new LinkedHashMap();
            if (oldTeam != null) {
                entries = this.renderTeamMemberOverviewLines(oldTeam, ChatColor.GREEN);
                deadLines = new LinkedHashMap();
                for (MatchTeam otherTeam : match.getTeams()) {
                    if (otherTeam == oldTeam) continue;
                    for (UUID enemy : otherTeam.getAllMembers()) {
                        if (otherTeam.isAlive(enemy)) {
                            entries.put(ChatColor.RED + FrozenUUIDCache.name((UUID)enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                            continue;
                        }
                        deadLines.put("&7&m" + FrozenUUIDCache.name((UUID)enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                    }
                }
                entries.putAll(deadLines);
            } else {
                deadLines = new LinkedHashMap<String, Integer>();
                for (MatchTeam team : match.getTeams()) {
                    for (UUID enemy : team.getAllMembers()) {
                        if (team.isAlive(enemy)) {
                            entries.put("&c" + FrozenUUIDCache.name((UUID)enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                            continue;
                        }
                        deadLines.put("&7&m" + FrozenUUIDCache.name((UUID)enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                    }
                }
                entries.putAll(deadLines);
            }
            final List<Map.Entry<String, Integer>> result2 = new ArrayList<>(entries.entrySet());
            for (int index = 0; index < result2.size(); ++index) {
                Map.Entry entry = (Map.Entry)result2.get(index);
                tabLayout.set(x++, y, (String)entry.getKey(), ((Integer)entry.getValue()).intValue());
                if (x == 3 && y == 20) {
                    int aliveLeft = 0;
                    for (int i = index; i < result2.size(); ++i) {
                        String currentEntry = (String)((Map.Entry)result2.get(i)).getKey();
                        boolean dead = ChatColor.getLastColors((String)currentEntry).equals(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString());
                        if (dead) continue;
                        ++aliveLeft;
                    }
                    if (aliveLeft == 0 || aliveLeft == 1) break;
                    tabLayout.set(x, y, ChatColor.GREEN + "+" + aliveLeft);
                    break;
                }
                if (x != 3) continue;
                x = 0;
                ++y;
            }
        }
    }

    private void renderTeamMemberOverviewEntries(TabLayout layout, MatchTeam team, int column, int start, ChatColor color) {
        ArrayList<Map.Entry<String, Integer>> result2 = new ArrayList<Map.Entry<String, Integer>>(this.renderTeamMemberOverviewLines(team, color).entrySet());
        int spotsLeft = 20 - start;
        int y = start;
        for (int index = 0; index < result2.size(); ++index) {
            Map.Entry entry = (Map.Entry)result2.get(index);
            if (spotsLeft == 1) {
                int aliveLeft = 0;
                for (int i = index; i < result2.size(); ++i) {
                    boolean dead;
                    String currentEntry = (String)((Map.Entry)result2.get(i)).getKey();
                    boolean bl = dead = !ChatColor.getLastColors((String)currentEntry).equals(color.toString());
                    if (dead) continue;
                    ++aliveLeft;
                }
                if (aliveLeft == 0) break;
                if (aliveLeft == 1) {
                    layout.set(column, y, (String)entry.getKey(), ((Integer)entry.getValue()).intValue());
                    break;
                }
                layout.set(column, y, color + "+" + aliveLeft);
                break;
            }
            layout.set(column, y, (String)entry.getKey(), ((Integer)entry.getValue()).intValue());
            ++y;
            --spotsLeft;
        }
    }

    private Map<String, Integer> renderTeamMemberOverviewLines(MatchTeam team, ChatColor aliveColor) {
        LinkedHashMap<String, Integer> aliveLines = new LinkedHashMap<String, Integer>();
        LinkedHashMap<String, Integer> deadLines = new LinkedHashMap<String, Integer>();
        for (UUID member : team.getAllMembers()) {
            int ping = PotPvPLayoutProvider.getPingOrDefault(member);
            if (team.isAlive(member)) {
                aliveLines.put(aliveColor + FrozenUUIDCache.name((UUID)member), ping);
                continue;
            }
            deadLines.put("&7&m" + FrozenUUIDCache.name((UUID)member), ping);
        }
        LinkedHashMap<String, Integer> result2 = new LinkedHashMap<String, Integer>();
        result2.putAll(aliveLines);
        result2.putAll(deadLines);
        return result2;
    }
}

