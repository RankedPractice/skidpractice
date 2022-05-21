/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.util.com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.repository.EloRepository;
import cc.fyre.potpvp.kittype.KitType;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.util.com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class EloConvertCommand {
    @Command(names={"eloconvert"}, permission="op")
    public static void eloconvert(Player sender) {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        EloRepository repository = PotPvP.getInstance().getEloHandler().getEloRepository();
        for (int i = 0; i < offlinePlayers.length; ++i) {
            OfflinePlayer target = offlinePlayers[i];
            if (i % 100 == 0) {
                sender.sendMessage(ChatColor.GREEN + "Converting: " + i + "/" + offlinePlayers.length);
            }
            try {
                Map<KitType, Integer> map = repository.loadElo((Set<UUID>)ImmutableSet.of(target.getUniqueId()));
                repository.saveElo((Set<UUID>)ImmutableSet.of(target.getUniqueId()), map);
                continue;
            }
            catch (IOException e) {
                sender.sendMessage(ChatColor.RED + "An error occured.");
                e.printStackTrace();
            }
        }
    }
}

