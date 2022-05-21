/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.menu.Menu
 */
package cc.fyre.potpvp.lobby;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.duel.DuelHandler;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.kit.KitItems;
import cc.fyre.potpvp.kit.menu.editkit.EditKitMenu;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.lobby.LobbyItems;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.PartyItems;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.queue.QueueItems;
import cc.fyre.potpvp.rematch.RematchData;
import cc.fyre.potpvp.rematch.RematchHandler;
import cc.fyre.potpvp.rematch.RematchItems;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.menu.Menu;

public final class LobbyUtils {
    public static void resetInventory(Player player) {
        if (Menu.currentlyOpenedMenus.get(player.getName()) instanceof EditKitMenu || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);
        if (partyHandler.hasParty(player)) {
            LobbyUtils.renderPartyItems(player, inventory, partyHandler.getParty(player));
        } else {
            LobbyUtils.renderSoloItems(player, inventory);
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }

    private static void renderPartyItems(Player player, PlayerInventory inventory, Party party) {
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        if (party.isLeader(player.getUniqueId())) {
            int partySize = party.getMembers().size();
            if (partySize == 2) {
                if (!queueHandler.isQueuedUnranked(party)) {
                    inventory.setItem(1, QueueItems.JOIN_PARTY_UNRANKED_QUEUE_ITEM);
                    inventory.setItem(3, PartyItems.ASSIGN_CLASSES);
                } else {
                    inventory.setItem(1, QueueItems.LEAVE_PARTY_UNRANKED_QUEUE_ITEM);
                }
                if (!queueHandler.isQueuedRanked(party)) {
                    inventory.setItem(2, QueueItems.JOIN_PARTY_RANKED_QUEUE_ITEM);
                    inventory.setItem(3, PartyItems.ASSIGN_CLASSES);
                } else {
                    inventory.setItem(2, QueueItems.LEAVE_PARTY_RANKED_QUEUE_ITEM);
                }
            } else if (partySize > 2 && !queueHandler.isQueued(party)) {
                inventory.setItem(1, PartyItems.START_TEAM_SPLIT_ITEM);
                inventory.setItem(2, PartyItems.START_FFA_ITEM);
                inventory.setItem(3, PartyItems.ASSIGN_CLASSES);
            }
        } else {
            int partySize = party.getMembers().size();
            if (partySize >= 2) {
                inventory.setItem(1, PartyItems.ASSIGN_CLASSES);
            }
        }
        inventory.setItem(0, PartyItems.icon(party));
        inventory.setItem(6, PartyItems.OTHER_PARTIES_ITEM);
        inventory.setItem(7, KitItems.OPEN_EDITOR_ITEM);
        inventory.setItem(8, PartyItems.LEAVE_PARTY_ITEM);
    }

    private static void renderSoloItems(Player player, PlayerInventory inventory) {
        RematchHandler rematchHandler = PotPvP.getInstance().getRematchHandler();
        QueueHandler queueHandler = PotPvP.getInstance().getQueueHandler();
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        boolean specMode = lobbyHandler.isInSpectatorMode(player);
        boolean followingSomeone = followHandler.getFollowing(player).isPresent();
        player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE || specMode);
        if (specMode || followingSomeone) {
            inventory.setItem(2, LobbyItems.SPECTATE_MENU_ITEM);
            inventory.setItem(4, LobbyItems.DISABLE_SPEC_MODE_ITEM);
            inventory.setItem(3, LobbyItems.SPECTATE_RANDOM_ITEM);
            if (followingSomeone) {
                inventory.setItem(8, LobbyItems.UNFOLLOW_ITEM);
            }
        } else {
            Player target;
            RematchData rematchData = rematchHandler.getRematchData(player);
            if (rematchData != null && (target = Bukkit.getPlayer((UUID)rematchData.getTarget())) != null) {
                if (duelHandler.findInvite(player, target) != null) {
                    inventory.setItem(2, RematchItems.SENT_REMATCH_ITEM);
                } else if (duelHandler.findInvite(target, player) != null) {
                    inventory.setItem(2, RematchItems.ACCEPT_REMATCH_ITEM);
                } else {
                    inventory.setItem(2, RematchItems.REQUEST_REMATCH_ITEM);
                }
            }
            if (queueHandler.isQueuedRanked(player.getUniqueId())) {
                inventory.setItem(0, QueueItems.LEAVE_SOLO_UNRANKED_QUEUE_ITEM);
            } else if (queueHandler.isQueuedUnranked(player.getUniqueId())) {
                inventory.setItem(0, QueueItems.LEAVE_SOLO_UNRANKED_QUEUE_ITEM);
            } else if (queueHandler.isQueuedPremium(player.getUniqueId())) {
                inventory.setItem(0, QueueItems.LEAVE_SOLO_PREMIUM_QUEUE_ITEM);
            } else {
                inventory.setItem(0, QueueItems.JOIN_SOLO_UNRANKED_QUEUE_ITEM);
                inventory.setItem(1, QueueItems.JOIN_SOLO_RANKED_QUEUE_ITEM);
                inventory.setItem(2, QueueItems.JOIN_SOLO_PREMIUM_QUEUE_ITEM);
                inventory.setItem(4, LobbyItems.ENABLE_SPEC_MODE_ITEM);
                inventory.setItem(7, LobbyItems.EVENTS_ITEM);
                inventory.setItem(8, KitItems.OPEN_EDITOR_ITEM);
            }
        }
    }

    private LobbyUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

