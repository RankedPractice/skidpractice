/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ImmutableMap
 *  mkremins.fanciful.FancyMessage
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.postmatchinv;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.postmatchinv.PostMatchInvLang;
import cc.fyre.potpvp.postmatchinv.PostMatchPlayer;
import cc.fyre.potpvp.postmatchinv.listener.PostMatchInvGeneralListener;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class PostMatchInvHandler {
    private final Map<UUID, Map<UUID, PostMatchPlayer>> playerData = new ConcurrentHashMap<UUID, Map<UUID, PostMatchPlayer>>();

    public PostMatchInvHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new PostMatchInvGeneralListener(), (Plugin)PotPvP.getInstance());
    }

    public void recordMatch(Match match) {
        this.saveInventories(match);
        this.messagePlayers(match);
    }

    private void saveInventories(Match match) {
        Map<UUID, PostMatchPlayer> matchPlayers = match.getPostMatchPlayers();
        for (MatchTeam team : match.getTeams()) {
            for (UUID member : team.getAliveMembers()) {
                this.playerData.put(member, matchPlayers);
            }
        }
        for (UUID spectator : match.getSpectators()) {
            this.playerData.put(spectator, matchPlayers);
        }
    }

    private void messagePlayers(Match match) {
        BaseComponent[] spectatorLine;
        HashMap<UUID, Object[]> invMessages = new HashMap<UUID, Object[]>();
        ArrayList<UUID> spectatorUuids = new ArrayList<UUID>(match.getSpectators());
        spectatorUuids.removeIf(uuid -> {
            Player player = Bukkit.getPlayer((UUID)uuid);
            return player == null || player.hasMetadata("ModMode") || match.getPreviousTeam((UUID)uuid) != null;
        });
        if (!spectatorUuids.isEmpty()) {
            List<String> spectatorNames = PatchedPlayerUtils.mapToNames(spectatorUuids);
            spectatorNames.sort(String::compareToIgnoreCase);
            String firstFourNames = Joiner.on((String)", ").join(spectatorNames.subList(0, Math.min(spectatorNames.size(), 4)));
            if (spectatorNames.size() > 4) {
                firstFourNames = firstFourNames + " (+" + (spectatorNames.size() - 4) + " more)";
            }
            HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[])spectatorNames.stream().map(n -> new TextComponent(ChatColor.AQUA + n + '\n')).toArray(BaseComponent[]::new));
            spectatorLine = new ComponentBuilder("Spectators (" + spectatorNames.size() + "): ").color(ChatColor.AQUA).append(firstFourNames).color(ChatColor.GRAY).event(hover).create();
        } else {
            spectatorLine = null;
        }
        this.createInvMessages(match, invMessages);
        invMessages.forEach((uuid, lines) -> {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player == null) {
                return;
            }
            String winnersLine = ChatColor.YELLOW + "Winner" + (match.getWinner().getAllMembers().size() == 1 ? "" : "s") + ": " + ChatColor.YELLOW;
            winnersLine = winnersLine + Joiner.on((String)", ").join((Iterable)match.getWinningPlayers().stream().map(FrozenUUIDCache::name).collect(Collectors.toList()));
            player.sendMessage("");
            player.sendMessage(winnersLine);
            for (Object line : lines) {
                if (line instanceof TextComponent[]) {
                    player.spigot().sendMessage((BaseComponent[])((TextComponent[])line));
                    continue;
                }
                if (line instanceof TextComponent) {
                    player.spigot().sendMessage((BaseComponent)((TextComponent)line));
                    continue;
                }
                if (!(line instanceof String)) continue;
                player.sendMessage((String)line);
            }
            if (spectatorLine != null) {
                player.spigot().sendMessage(spectatorLine);
            }
            new FancyMessage(CC.translate("&b[Replay]")).command("/playreplay " + match.get_id()).tooltip(CC.translate("&7Click to view the replay!")).send(player);
            player.sendMessage("");
        });
    }

    private void createInvMessages(Match match, Map<UUID, Object[]> invMessages) {
        MatchTeam loserTeam;
        List<MatchTeam> teams = match.getTeams();
        if (teams.size() != 2) {
            Object[] generic = PostMatchInvLang.genFfaInvs(match.getWinner(), teams);
            this.writeSpecInvMessages(match, invMessages, generic);
            this.writeTeamInvMessages(teams, invMessages, generic);
            return;
        }
        MatchTeam winnerTeam = match.getWinner();
        MatchTeam matchTeam = loserTeam = teams.get(0) == winnerTeam ? teams.get(1) : teams.get(0);
        if (winnerTeam.getAllMembers().size() == 1 && loserTeam.getAllMembers().size() == 1) {
            Object[] generic = PostMatchInvLang.gen1v1PlayerInvs(winnerTeam.getFirstMember(), loserTeam.getFirstMember());
            this.writeSpecInvMessages(match, invMessages, generic);
            this.writeTeamInvMessages(teams, invMessages, generic);
        } else {
            this.writeSpecInvMessages(match, invMessages, PostMatchInvLang.genSpectatorInvs(winnerTeam, loserTeam));
            this.writeTeamInvMessages(winnerTeam, invMessages, PostMatchInvLang.genTeamInvs(winnerTeam, winnerTeam, loserTeam));
            this.writeTeamInvMessages(loserTeam, invMessages, PostMatchInvLang.genTeamInvs(loserTeam, winnerTeam, loserTeam));
        }
    }

    private void writeTeamInvMessages(Iterable<MatchTeam> teams, Map<UUID, Object[]> messageMap, Object[] messages) {
        for (MatchTeam team : teams) {
            this.writeTeamInvMessages(team, messageMap, messages);
        }
    }

    private void writeTeamInvMessages(MatchTeam team, Map<UUID, Object[]> messageMap, Object[] messages) {
        for (UUID member : team.getAllMembers()) {
            if (!messageMap.containsKey(member) && !team.isAlive(member)) continue;
            messageMap.put(member, messages);
        }
    }

    private void writeSpecInvMessages(Match match, Map<UUID, Object[]> messageMap, Object[] messages) {
        for (UUID spectator : match.getSpectators()) {
            messageMap.put(spectator, messages);
        }
    }

    public Map<UUID, PostMatchPlayer> getPostMatchData(UUID forWhom) {
        return this.playerData.getOrDefault(forWhom, (Map<UUID, PostMatchPlayer>)ImmutableMap.of());
    }

    public void removePostMatchData(UUID forWhom) {
        this.playerData.remove(forWhom);
    }
}

