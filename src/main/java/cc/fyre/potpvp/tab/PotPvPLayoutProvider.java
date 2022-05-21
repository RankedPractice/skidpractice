/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.tab.LayoutProvider
 *  rip.bridge.qlib.tab.TabLayout
 *  rip.bridge.qlib.util.PlayerUtils
 */
package cc.fyre.potpvp.tab;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;

import java.util.UUID;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.bridge.qlib.tab.LayoutProvider;
import rip.bridge.qlib.tab.TabLayout;
import rip.bridge.qlib.util.PlayerUtils;

public final class PotPvPLayoutProvider
implements LayoutProvider {
    static final int MAX_TAB_Y = 20;
    private static boolean testing = true;
    private final BiConsumer<Player, TabLayout> headerLayoutProvider = new HeaderLayoutProvider();
    private final BiConsumer<Player, TabLayout> lobbyLayoutProvider = new LobbyLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchSpectatorLayoutProvider = new MatchSpectatorLayoutProvider();
    private final BiConsumer<Player, TabLayout> matchParticipantLayoutProvider = new MatchParticipantLayoutProvider();

    public TabLayout provide(Player player) {
        TabLayout tabLayout = TabLayout.create((Player)player);
        Match match = PotPvP.getInstance().getMatchHandler().getMatchPlayingOrSpectating(player);
        this.headerLayoutProvider.accept(player, tabLayout);
        if (match != null) {
            if (match.isSpectator(player.getUniqueId())) {
                this.matchSpectatorLayoutProvider.accept(player, tabLayout);
            } else {
                this.matchParticipantLayoutProvider.accept(player, tabLayout);
            }
        } else {
            this.lobbyLayoutProvider.accept(player, tabLayout);
        }
        //this.onlinePlayersLayoutProvider.accept(player, tabLayout);
        //this.onlinePlayersLayoutProvider.accept(player, tabLayout);
        return tabLayout;
    }

    static int getPingOrDefault(UUID check2) {
        Player player = Bukkit.getPlayer((UUID)check2);
        return player != null ? PlayerUtils.getPing((Player)player) : 0;
    }
}

