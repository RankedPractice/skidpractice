/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.pagination.PaginatedMenu
 */
package cc.fyre.potpvp.lobby.menu;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.lobby.menu.SpectateButton;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.pagination.PaginatedMenu;

public final class SpectateMenu
extends PaginatedMenu {
    public SpectateMenu() {
        this.setAutoUpdate(true);
    }

    public String getPrePaginatedTitle(Player player) {
        return "Spectate a match";
    }

    public Map<Integer, Button> getAllPagesButtons(Player player) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int i = 0;
        for (Match match : PotPvP.getInstance().getMatchHandler().getHostedMatches()) {
            if (match.isSpectator(player.getUniqueId()) || match.getState() == MatchState.ENDING) continue;
            if (!PotPvP.getInstance().getTournamentHandler().isInTournament(match)) {
                int numTotalPlayers = 0;
                int numSpecDisabled = 0;
                for (MatchTeam team : match.getTeams()) {
                    for (UUID member : team.getAliveMembers()) {
                        ++numTotalPlayers;
                        if (settingHandler.getSetting(Bukkit.getPlayer((UUID)member), Setting.ALLOW_SPECTATORS)) continue;
                        ++numSpecDisabled;
                    }
                }
                if ((double)((float)numSpecDisabled / (float)numTotalPlayers) >= 0.5) continue;
            }
            buttons.put(i++, new SpectateButton(match));
        }
        return buttons;
    }

    public int size(Map<Integer, Button> buttons) {
        return 54;
    }
}

