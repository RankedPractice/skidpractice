/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.menu.menus.ConfirmMenu
 *  rip.bridge.qlib.qLib
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.command;

import cc.fyre.potpvp.PotPvP;
import com.google.common.base.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.menu.menus.ConfirmMenu;
import rip.bridge.qlib.qLib;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class StatsResetCommands {
    private static String REDIS_PREFIX = "PotPvP:statsResetToken:";

    @Command(names={"statsreset addtoken"}, permission="op", async=true)
    public static void addToken(CommandSender sender, @Param(name="player") String playerName, @Param(name="amount") int amount) {
        UUID uuid = FrozenUUIDCache.uuid((String)playerName);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate '" + playerName + "'.");
            return;
        }
        StatsResetCommands.addTokens(uuid, amount);
        sender.sendMessage(ChatColor.GREEN + "Added " + amount + " token" + (amount == 1 ? "" : "s") + " to " + FrozenUUIDCache.name((UUID)uuid) + ".");
    }

    @Command(names={"statsreset"}, permission="", async=true)
    public static void reset(Player sender) {
        int tokens = StatsResetCommands.getTokens(sender.getUniqueId());
        if (tokens <= 0) {
            sender.sendMessage(ChatColor.RED + "You need at least one token to reset your stats.");
            return;
        }
        Bukkit.getScheduler().runTask((Plugin)PotPvP.getInstance(), () -> new ConfirmMenu("Stats reset", reset -> {
            if (!reset.booleanValue()) {
                sender.sendMessage(ChatColor.RED + "Stats reset aborted.");
                return;
            }
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)PotPvP.getInstance(), () -> {
                PotPvP.getInstance().getEloHandler().resetElo(sender.getUniqueId());
                StatsResetCommands.removeTokens(sender.getUniqueId(), 1);
                int tokens1 = StatsResetCommands.getTokens(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Reset your stats! Used one token. " + tokens1 + " token" + (tokens1 == 1 ? "" : "s") + " left.");
            });
        }).openMenu(sender));
    }

    private static int getTokens(UUID player) {
        return (Integer)qLib.getInstance().runBackboneRedisCommand(redis -> Integer.valueOf((String)Objects.firstNonNull((Object)redis.get(REDIS_PREFIX + player.toString()), (Object)"0")));
    }

    private static void addTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(redis -> {
            redis.incrBy(REDIS_PREFIX + player.toString(), (long)amountBy);
            return null;
        });
    }

    public static void removeTokens(UUID player, int amountBy) {
        qLib.getInstance().runBackboneRedisCommand(redis -> {
            redis.decrBy(REDIS_PREFIX + player.toString(), (long)amountBy);
            return null;
        });
    }
}

