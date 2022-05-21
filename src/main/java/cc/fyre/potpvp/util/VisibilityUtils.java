/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamEventLogic;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class VisibilityUtils {
    public static boolean SHOW_IN_LOBBY = true;

    public static void updateVisibilityFlicker(Player target) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(otherPlayer);
            otherPlayer.hidePlayer(target);
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> VisibilityUtils.updateVisibility(target), 10L);
    }

    public static void updateVisibility(Player target) {
        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (VisibilityUtils.shouldSeePlayer(otherPlayer, target)) {
                otherPlayer.showPlayer(target);
            } else {
                otherPlayer.hidePlayer(target);
            }
            if (VisibilityUtils.shouldSeePlayer(target, otherPlayer)) {
                target.showPlayer(otherPlayer);
                continue;
            }
            target.hidePlayer(otherPlayer);
        }
    }

    private static boolean shouldSeePlayer(Player viewer, Player target) {
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match targetMatch = matchHandler.getMatchPlayingOrSpectating(target);
        Game game = PotPvP.getInstance().getGameHandler().getOngoingGame();
        if (game != null && game.getPlayers().contains(viewer) && game.getPlayers().contains(target)) {
            if (game.getState() == GameState.ENDED) {
                return true;
            }
            if (game.getSpectators().contains(target) && !game.getSpectators().contains(viewer)) {
                return false;
            }
            if (game.getSpectators().contains(target) && game.getSpectators().contains(viewer)) {
                return true;
            }
            if (game.getLogic() instanceof GameTeamEventLogic) {
                GameTeam team = ((GameTeamEventLogic)game.getLogic()).get(target);
                return team == null || !team.hasDied(target);
            }
            return true;
        }
        if (targetMatch == null) {
            Party targetParty = partyHandler.getParty(target);
            Optional<UUID> following = followHandler.getFollowing(viewer);
            boolean bothInLobby = lobbyHandler.isInLobby(viewer) && lobbyHandler.isInLobby(target);
            boolean viewerShowingLobbyPlayers = settingHandler.getSetting(viewer, Setting.SHOW_PLAYERS_IN_LOBBY);
            boolean targetShownInLobby = target.hasPermission("potpvp.lobby.shown");
            boolean viewerPlayingMatch = matchHandler.isPlayingOrSpectatingMatch(viewer);
            boolean viewerSameParty = targetParty != null && targetParty.isMember(viewer.getUniqueId());
            boolean viewerFollowingTarget = following.isPresent() && following.get().equals(target.getUniqueId());
            return bothInLobby && viewerShowingLobbyPlayers && (targetShownInLobby || SHOW_IN_LOBBY) || viewerPlayingMatch || viewerSameParty || viewerFollowingTarget;
        }
        boolean targetIsSpectator = targetMatch.isSpectator(target.getUniqueId());
        boolean viewerSpecSetting = settingHandler.getSetting(viewer, Setting.VIEW_OTHER_SPECTATORS);
        boolean viewerIsSpectator = matchHandler.isSpectatingMatch(viewer);
        return !targetIsSpectator || viewerSpecSetting && viewerIsSpectator && !target.hasMetadata("ModMode");
    }
}

