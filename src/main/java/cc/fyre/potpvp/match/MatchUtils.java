/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 */
package cc.fyre.potpvp.match;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.SpectatorItems;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public final class MatchUtils {
    public static void resetInventory(Player player) {
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        FollowHandler followHandler = PotPvP.getInstance().getFollowHandler();
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchSpectating(player);
        if (match == null) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);
        if (match.getState() != MatchState.ENDING) {
            boolean canViewInventories = player.hasPermission("potpvp.inventory.all");
            if (!canViewInventories) {
                for (MatchTeam team : match.getTeams()) {
                    if (!team.getAllMembers().contains(player.getUniqueId())) continue;
                    canViewInventories = true;
                    break;
                }
            }
            if (canViewInventories) {
                inventory.setItem(0, SpectatorItems.VIEW_INVENTORY_ITEM);
            }
            if (settingHandler.getSetting(player, Setting.VIEW_OTHER_SPECTATORS)) {
                inventory.setItem(1, SpectatorItems.HIDE_SPECTATORS_ITEM);
            } else {
                inventory.setItem(1, SpectatorItems.SHOW_SPECTATORS_ITEM);
            }
            if (partyHandler.hasParty(player)) {
                inventory.setItem(8, SpectatorItems.LEAVE_PARTY_ITEM);
            } else {
                inventory.setItem(8, SpectatorItems.RETURN_TO_LOBBY_ITEM);
            }
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> ((Player)player).updateInventory(), 1L);
    }
}

