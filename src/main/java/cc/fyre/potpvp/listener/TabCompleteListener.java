/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChatTabCompleteEvent
 *  org.bukkit.util.StringUtil
 */
package cc.fyre.potpvp.listener;

import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.util.StringUtil;

public final class TabCompleteListener
implements Listener {
    @EventHandler
    public void onPlayerChatTabComplete(PlayerChatTabCompleteEvent event) {
        String token = event.getLastToken();
        Collection completions = event.getTabCompletions();
        completions.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!event.getPlayer().canSee(player) && player.hasMetadata("invisible") || !StringUtil.startsWithIgnoreCase((String)player.getName(), (String)token)) continue;
            completions.add(player.getName());
        }
    }
}

