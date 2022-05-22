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
import rip.bridge.bridge.BridgeGlobal;
import rip.bridge.bridge.global.profile.Profile;
import rip.bridge.qlib.nametag.NametagInfo;
import rip.bridge.qlib.nametag.NametagProvider;

public final class PotPvPNametagProvider extends NametagProvider {

    public PotPvPNametagProvider() {
        super("PotPvP Provider", 5);
    }

    public static String getNameColor(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();

        if (matchHandler.isPlayingOrSpectatingMatch(toRefresh)) {
            return ChatColor.translateAlternateColorCodes('&', getNameColorMatch(toRefresh, refreshFor).toString());
        } else {
            return ChatColor.translateAlternateColorCodes('&', getNameColorLobby(toRefresh, refreshFor));
        }
    }

    private static ChatColor getNameColorMatch(Player toRefresh, Player refreshFor) {
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();

        Match toRefreshMatch = matchHandler.getMatchPlayingOrSpectating(toRefresh);
        MatchTeam toRefreshTeam = toRefreshMatch.getTeam(toRefresh.getUniqueId());

        // they're a spectator, so we see them as gray
        if (toRefreshTeam == null) {
            return ChatColor.GRAY;
        }

        MatchTeam refreshForTeam = toRefreshMatch.getTeam(refreshFor.getUniqueId());

        // if we can't find a current team, check if they have any
        // previously teams we can use for this
        if (refreshForTeam == null) {
            refreshForTeam = toRefreshMatch.getPreviousTeam(refreshFor.getUniqueId());
        }

        // if we were/are both on teams display a friendly/enemy color
        if (refreshForTeam != null) {
            if (toRefreshTeam == refreshForTeam) {
                return ChatColor.GREEN;
            } else {
                if (ArcherClass.getMarkedPlayers().containsKey(toRefresh.getName()) && System.currentTimeMillis() < ArcherClass.getMarkedPlayers().get(toRefresh.getName())) {
                    return ChatColor.YELLOW;
                }
                return ChatColor.RED;
            }
        }

        // if we're a spectator just display standard colors
        List<MatchTeam> teams = toRefreshMatch.getTeams();

        // we have predefined colors for 'normal' matches
        if (teams.size() == 2) {
            // team 1 = RED, team 2 = AQUA
            if (toRefreshTeam == teams.get(0)) {
                return ChatColor.RED;
            } else {
                return ChatColor.AQUA;
            }
        } else {
            // we don't have colors defined for larger matches
            // everyone is just red for spectators
            return ChatColor.RED;
        }
    }

    private static String getNameColorLobby(Player toRefresh, Player refreshFor) {
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();

        Optional<UUID> following = followHandler.getFollowing(refreshFor);
        Profile profile = BridgeGlobal.getProfileHandler().getProfileByUUID(refreshFor.getUniqueId());
        boolean refreshForFollowingTarget = following.isPresent() && following.get().equals(toRefresh.getUniqueId());

        if (refreshForFollowingTarget) {
            return ChatColor.AQUA.toString();
        } else {
            return profile.getCurrentGrant().getRank().getColor();
        }
    }

    public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
        String prefixColor = getNameColor(toRefresh, refreshFor);
        return PotPvPNametagProvider.createNametag(prefixColor, "");
    }
}

