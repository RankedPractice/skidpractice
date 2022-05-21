/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.menu.Button
 *  rip.bridge.qlib.menu.pagination.PaginatedMenu
 */
package cc.fyre.potpvp.party.menu.otherparties;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.menu.otherparties.OtherPartyButton;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.bridge.qlib.menu.Button;
import rip.bridge.qlib.menu.pagination.PaginatedMenu;

public final class OtherPartiesMenu
extends PaginatedMenu {
    public OtherPartiesMenu() {
        this.setPlaceholder(true);
        this.setAutoUpdate(true);
    }

    public String getPrePaginatedTitle(Player player) {
        return "Other parties";
    }

    public Map<Integer, Button> getAllPagesButtons(Player player) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        ArrayList<Party> parties = new ArrayList<Party>(partyHandler.getParties());
        int index = 0;
        parties.sort(Comparator.comparing(p -> p.getMembers().size()));
        for (Party party : parties) {
            if (party.isMember(player.getUniqueId()) || !lobbyHandler.isInLobby(Bukkit.getPlayer((UUID)party.getLeader())) || !settingHandler.getSetting(Bukkit.getPlayer((UUID)party.getLeader()), Setting.RECEIVE_DUELS) || PotPvP.getInstance().getTournamentHandler().isInTournament(party)) continue;
            buttons.put(index++, new OtherPartyButton(party));
        }
        return buttons;
    }

    public int size(Map<Integer, Button> buttons) {
        return 54;
    }

    public int getMaxItemsPerPage(Player player) {
        return 45;
    }
}

