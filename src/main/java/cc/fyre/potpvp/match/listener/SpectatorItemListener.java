/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.MatchUtils;
import cc.fyre.potpvp.match.SpectatorItems;
import cc.fyre.potpvp.match.command.LeaveCommand;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.util.FancyPlayerInventory;
import cc.fyre.potpvp.util.ItemListener;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class SpectatorItemListener
extends ItemListener {
    private final Map<UUID, Long> toggleVisiblityUsable = new ConcurrentHashMap<UUID, Long>();

    public SpectatorItemListener(MatchHandler matchHandler) {
        this.setPreProcessPredicate(matchHandler::isSpectatingMatch);
        Consumer<Player> toggleSpectatorsConsumer = player -> {
            boolean togglePermitted;
            SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
            UUID playerUuid = player.getUniqueId();
            boolean bl = togglePermitted = this.toggleVisiblityUsable.getOrDefault(playerUuid, 0L) < System.currentTimeMillis();
            if (!togglePermitted) {
                player.sendMessage(ChatColor.RED + "Please wait before doing this again!");
                return;
            }
            boolean enabled = !settingHandler.getSetting((Player)player, Setting.VIEW_OTHER_SPECTATORS);
            settingHandler.updateSetting((Player)player, Setting.VIEW_OTHER_SPECTATORS, enabled);
            if (enabled) {
                player.sendMessage(ChatColor.GREEN + "Now showing other spectators.");
            } else {
                player.sendMessage(ChatColor.RED + "Now hiding other spectators.");
            }
            MatchUtils.resetInventory(player);
            this.toggleVisiblityUsable.put(playerUuid, System.currentTimeMillis() + 3000L);
        };
        this.addHandler(SpectatorItems.RETURN_TO_LOBBY_ITEM, LeaveCommand::leave);
        this.addHandler(SpectatorItems.LEAVE_PARTY_ITEM, LeaveCommand::leave);
        this.addHandler(SpectatorItems.SHOW_SPECTATORS_ITEM, toggleSpectatorsConsumer);
        this.addHandler(SpectatorItems.HIDE_SPECTATORS_ITEM, toggleSpectatorsConsumer);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        Match clickerMatch = matchHandler.getMatchSpectating(event.getPlayer());
        Player clicker = event.getPlayer();
        if (clickerMatch == null || !clicker.getItemInHand().isSimilar(SpectatorItems.VIEW_INVENTORY_ITEM)) {
            return;
        }
        Player clicked = (Player)event.getRightClicked();
        MatchTeam clickedTeam = clickerMatch.getTeam(clicked.getUniqueId());
        if (clickedTeam == null) {
            clicker.sendMessage(ChatColor.RED + "Cannot view inventory of " + clicked.getName());
            return;
        }
        boolean bypassPerm = clicker.hasPermission("potpvp.inventory.all");
        boolean sameTeam = clickedTeam.getAllMembers().contains(clicker.getUniqueId());
        if (bypassPerm || sameTeam) {
            clicker.sendMessage(ChatColor.AQUA + "Opening inventory of: " + clicked.getName());
            FancyPlayerInventory.open(clicked, clicker);
        } else {
            clicker.sendMessage(ChatColor.RED + clicked.getName() + " is not on your team.");
        }
    }

    @Override
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.toggleVisiblityUsable.remove(event.getPlayer().getUniqueId());
    }
}

