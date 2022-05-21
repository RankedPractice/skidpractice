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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

final class MatchParticipantLayoutProvider
implements BiConsumer<Player, TabLayout> {
    MatchParticipantLayoutProvider() {
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void accept(final Player player, final TabLayout tabLayout) {
        final Match match = PotPvP.getInstance().getMatchHandler().getMatchPlaying(player);
        final List<MatchTeam> teams = match.getTeams();
        if (teams.size() == 2) {
            final MatchTeam ourTeam = match.getTeam(player.getUniqueId());
            final MatchTeam otherTeam = (teams.get(0) == ourTeam) ? teams.get(1) : teams.get(0);
            final boolean duel = ourTeam.getAllMembers().size() == 1 && otherTeam.getAllMembers().size() == 1;
            if (!duel) {
                tabLayout.set(0, 3, ChatColor.GREEN + ChatColor.BOLD.toString() + "Team " + ChatColor.GREEN + "(" + ourTeam.getAliveMembers().size() + "/" + ourTeam.getAllMembers().size() + ")");
            }
            else {
                tabLayout.set(0, 3, ChatColor.GREEN + ChatColor.BOLD.toString() + "You");
            }
            this.renderTeamMemberOverviewEntries(tabLayout, ourTeam, 0, 4, ChatColor.GREEN);
            if (!duel) {
                tabLayout.set(2, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Enemies " + ChatColor.RED + "(" + otherTeam.getAliveMembers().size() + "/" + otherTeam.getAllMembers().size() + ")");
            }
            else {
                tabLayout.set(2, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Opponent");
            }
            this.renderTeamMemberOverviewEntries(tabLayout, otherTeam, 2, 4, ChatColor.RED);
        }
        else {
            tabLayout.set(1, 3, ChatColor.RED + ChatColor.BOLD.toString() + "Party FFA");
            int x = 0;
            int y = 4;
            final Map<String, Integer> entries = new LinkedHashMap<>();
            final MatchTeam ourTeam2 = match.getTeam(player.getUniqueId());
            final Map<String, Integer> aliveLines = new LinkedHashMap<>();
            final Map<String, Integer> deadLines = new LinkedHashMap<>();
            for (final UUID teamMember : ourTeam2.getAllMembers()) {
                if (ourTeam2.isAlive(teamMember)) {
                    aliveLines.put(ChatColor.GREEN + FrozenUUIDCache.name(teamMember), PotPvPLayoutProvider.getPingOrDefault(teamMember));
                }
                else {
                    deadLines.put("&7&m" + FrozenUUIDCache.name(teamMember), PotPvPLayoutProvider.getPingOrDefault(teamMember));
                }
            }
            entries.putAll(aliveLines);
            entries.putAll(deadLines);
            final Map<String, Integer> deadLines2 = new LinkedHashMap<>();
            for (final MatchTeam otherTeam2 : match.getTeams()) {
                if (otherTeam2 == ourTeam2) {
                    continue;
                }
                for (final UUID enemy : otherTeam2.getAllMembers()) {
                    if (otherTeam2.isAlive(enemy)) {
                        entries.put(ChatColor.RED + FrozenUUIDCache.name(enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                    }
                    else {
                        deadLines2.put("&7&m" + FrozenUUIDCache.name(enemy), PotPvPLayoutProvider.getPingOrDefault(enemy));
                    }
                }
            }
            entries.putAll(deadLines2);
            final List<Map.Entry<String, Integer>> result = new ArrayList<>(entries.entrySet());
            int index = 0;
            while (index < result.size()) {
                final Map.Entry<String, Integer> entry = result.get(index);
                tabLayout.set(x++, y, entry.getKey(), entry.getValue());
                if (x == 3 && y == 20) {
                    int aliveLeft = 0;
                    for (int i = index; i < result.size(); ++i) {
                        final String currentEntry = result.get(i).getKey();
                        final boolean dead = ChatColor.getLastColors(currentEntry).equals(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString());
                        if (!dead) {
                            ++aliveLeft;
                        }
                    }
                    if (aliveLeft != 0 && aliveLeft != 1) {
                        tabLayout.set(x, y, ChatColor.GREEN + "+" + aliveLeft);
                        break;
                    }
                    break;
                }
                else {
                    if (x == 3) {
                        x = 0;
                        ++y;
                    }
                    ++index;
                }
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

