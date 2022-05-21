/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.nametag.NametagInfo
 *  rip.bridge.qlib.nametag.NametagProvider
 */
package cc.fyre.potpvp.nametag;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.pvpclasses.pvpclasses.ArcherClass;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.nametag.NametagInfo;
import rip.bridge.qlib.nametag.NametagProvider;

public final class PotPvPNametagProvider
extends NametagProvider {
    public PotPvPNametagProvider() {
        super("PotPvP Provider", 5);
    }

    public static String getNameColor(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (matchHandler.isPlayingOrSpectatingMatch(toRefresh)) {
            return PotPvPNametagProvider.getNameColorMatch(toRefresh, refreshFor);
        }
        return PotPvPNametagProvider.getNameColorLobby(toRefresh, refreshFor);
    }

    private static String getNameColorMatch(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match toRefreshMatch = matchHandler.getMatchPlayingOrSpectating(toRefresh);
        MatchTeam toRefreshTeam = toRefreshMatch.getTeam(toRefresh.getUniqueId());
        if (toRefreshTeam == null) {
            return ChatColor.GRAY.toString();
        }
        MatchTeam refreshForTeam = toRefreshMatch.getTeam(refreshFor.getUniqueId());
        if (refreshForTeam == null) {
            refreshForTeam = toRefreshMatch.getPreviousTeam(refreshFor.getUniqueId());
        }
        if (refreshForTeam != null) {
            if (toRefreshTeam == refreshForTeam) {
                return ChatColor.GREEN.toString();
            }
            if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && System.currentTimeMillis() < ArcherClass.getMarkedPlayers().get(toRefresh.getName())) {
                return ChatColor.YELLOW.toString();
            }
            return ChatColor.RED.toString();
        }
        List<MatchTeam> teams = toRefreshMatch.getTeams();
        if (teams.size() == 2) {
            if (toRefreshTeam == teams.get(0)) {
                return ChatColor.LIGHT_PURPLE.toString();
            }
            return ChatColor.AQUA.toString();
        }
        return ChatColor.RED.toString();
    }

    private static String getNameColorLobby(Player toRefresh, Player refreshFor) {
        boolean refreshForFollowingTarget;
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        Optional<UUID> following = followHandler.getFollowing(refreshFor);
        boolean bl = refreshForFollowingTarget = following.isPresent() && following.get().equals(toRefresh.getUniqueId());
        if (refreshForFollowingTarget) {
            return ChatColor.AQUA.toString();
        }
        return ChatColor.AQUA.toString();
    }

    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        /*Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game != null && game.getPlayers().contains(toRefresh) && game.getPlayers().contains(refreshFor) && game.getState() != GameState.ENDED) {
            return PotPvPNametagProvider.createNametag((String)game.getEvent().getNameTag(game, toRefresh, refreshFor), (String)"");
        }*/
        String nametag = ChatColor.translateAlternateColorCodes((char)'&', (String)PotPvPNametagProvider.getNameColor(toRefresh, refreshFor));
        return PotPvPNametagProvider.createNametag((String)nametag, (String)"");
    }
}

