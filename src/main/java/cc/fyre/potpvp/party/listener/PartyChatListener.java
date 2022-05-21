/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package cc.fyre.potpvp.party.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.Party;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PartyChatListener
implements Listener {
    private final Map<UUID, Long> canUsePartyChat = new ConcurrentHashMap<UUID, Long>();

    @EventHandler(ignoreCancelled=true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getMessage().startsWith("@")) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        String message = event.getMessage().substring(1).trim();
        Party party = PotPvP.getInstance().getPartyHandler().getParty(player);
        if (party == null) {
            player.sendMessage(ChatColor.RED + "You aren't in a party!");
            return;
        }
        if (this.canUsePartyChat.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
            player.sendMessage(ChatColor.RED + "Wait a bit before sending another message.");
            return;
        }
        ChatColor prefixColor = party.isLeader(player.getUniqueId()) ? ChatColor.AQUA : ChatColor.LIGHT_PURPLE;
        party.message(prefixColor.toString() + ChatColor.BOLD + "[P] " + player.getName() + ": " + ChatColor.LIGHT_PURPLE + message);
        this.canUsePartyChat.put(player.getUniqueId(), System.currentTimeMillis() + 2000L);
        PotPvP.getInstance().getLogger().info("[Party Chat] " + player.getName() + ": " + message);
    }
}

