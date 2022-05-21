/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.lobby.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.command.UnfollowCommand;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.lobby.LobbyItems;
import cc.fyre.potpvp.lobby.menu.StatisticsMenu;
import cc.fyre.potpvp.util.ItemListener;
import cc.fyre.potpvp.validation.PotPvPValidation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class LobbyItemListener
extends ItemListener {
    private final Map<UUID, Long> canUseRandomSpecItem = new HashMap<UUID, Long>();

    public LobbyItemListener(LobbyHandler lobbyHandler) {
        this.addHandler(LobbyItems.CREATE_PARTY_ITEM, player -> {
            if (!lobbyHandler.isInLobby((Player)player)) {
                player.sendMessage(ChatColor.RED + "You must create a party from the lobby.");
                return;
            }
            if (PotPvP.getInstance().getQueueHandler().isQueued(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Leave your queue to create a party..");
                return;
            }
            if (PotPvP.getInstance().getPartyHandler().hasParty((Player)player)) {
                player.sendMessage(ChatColor.RED + "You already belong to a party.");
                return;
            }
            PotPvP.getInstance().getPartyHandler().getOrCreateParty((Player)player);
            player.sendMessage(ChatColor.YELLOW + "Created a new party.");
        });
        this.addHandler(LobbyItems.DISABLE_SPEC_MODE_ITEM, player -> {
            if (lobbyHandler.isInLobby((Player)player)) {
                lobbyHandler.setSpectatorMode((Player)player, false);
            }
        });
        this.addHandler(LobbyItems.EVENTS_ITEM, player -> {
            /*Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
            if (lobbyHandler.isInLobby((Player)player)) {
                if (game == null) {
                    player.sendMessage(ChatColor.RED + "There are no active events.");
                    player.sendMessage(ChatColor.GREEN + "You can donate at lunar.gg/store for the ability to start an event!");
                } else {
                    player.performCommand("event join");
                }
            }*/
        });
        this.addHandler(LobbyItems.ENABLE_SPEC_MODE_ITEM, player -> {
            if (lobbyHandler.isInLobby((Player)player) && PotPvPValidation.canUseSpectateItem(player)) {
                lobbyHandler.setSpectatorMode((Player)player, true);
            }
        });
        this.addHandler(LobbyItems.PLAYER_STATISTICS, player -> new StatisticsMenu().openMenu((Player)player));
        this.addHandler(LobbyItems.UNFOLLOW_ITEM, UnfollowCommand::unfollow);
    }

    @Override
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.canUseRandomSpecItem.remove(event.getPlayer().getUniqueId());
    }
}

