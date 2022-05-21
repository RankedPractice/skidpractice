/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.follow.listener;

import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorLeaveEvent;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.event.SettingUpdateEvent;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class FollowGeneralListener
implements Listener {
    private final FollowHandler followHandler;

    public FollowGeneralListener(FollowHandler followHandler) {
        this.followHandler = followHandler;
    }

    @EventHandler
    public void onMatchStart(MatchCountdownStartEvent event) {
        Match match = event.getMatch();
        for (MatchTeam team : match.getTeams()) {
            for (UUID member : team.getAllMembers()) {
                Player memberBukkit = Bukkit.getPlayer((UUID)member);
                for (UUID follower : this.followHandler.getFollowers(memberBukkit)) {
                    match.addSpectator(Bukkit.getPlayer((UUID)follower), memberBukkit);
                }
            }
        }
    }

    @EventHandler
    public void onMatchSpectatorLeave(MatchSpectatorLeaveEvent event) {
        this.followHandler.stopFollowing(event.getSpectator());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (UUID follower : this.followHandler.getFollowers(event.getPlayer())) {
            this.followHandler.stopFollowing(Bukkit.getPlayer((UUID)follower));
        }
        this.followHandler.stopFollowing(event.getPlayer());
    }

    @EventHandler
    public void onSettingUpdate(SettingUpdateEvent event) {
        if (event.getSetting() != Setting.ALLOW_SPECTATORS || event.isEnabled()) {
            return;
        }
        for (UUID follower : this.followHandler.getFollowers(event.getPlayer())) {
            Player followerPlayer = Bukkit.getPlayer((UUID)follower);
            if (followerPlayer.isOp() || followerPlayer.hasMetadata("ModMode")) continue;
            this.followHandler.stopFollowing(followerPlayer);
        }
    }
}

