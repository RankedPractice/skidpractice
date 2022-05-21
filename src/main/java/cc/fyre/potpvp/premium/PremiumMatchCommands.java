/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.premium;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.premium.PremiumMatchesHandler;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class PremiumMatchCommands {
    private static final PremiumMatchesHandler handler = PotPvP.getInstance().getPremiumMatchesHandler();

    @Command(names={"premium add"}, permission="potpvp.admin")
    public static void add(CommandSender sender, @Param(name="target") OfflinePlayer target) {
        int newAmount = handler.increasePremiumMatches(target.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + target.getName() + "'s new premium matches amount is " + newAmount);
    }

    @Command(names={"premium set"}, permission="potpvp.admin")
    public static void set(CommandSender sender, @Param(name="target") OfflinePlayer target, @Param(name="amount", defaultValue="1") int amount) {
        int newAmount = handler.setPremiumMatches(target.getUniqueId(), amount);
        sender.sendMessage(ChatColor.GREEN + target.getName() + "'s new premium matches amount is " + newAmount + " (Added " + amount + " matches)");
    }

    @Command(names={"premium remove"}, permission="potpvp.admin")
    public static void remove(CommandSender sender, @Param(name="target") OfflinePlayer target, @Param(name="amount", defaultValue="1") int amount) {
        int newAmount = handler.removePremiumMatches(target.getUniqueId(), amount);
        sender.sendMessage(ChatColor.GREEN + target.getName() + "'s new premium matches amount is " + newAmount + " (Removed " + amount + " matches)");
    }

    @Command(names={"premium clear"}, permission="potpvp.admin")
    public static void clear(CommandSender sender, @Param(name="target") OfflinePlayer target) {
        int newAmount = handler.setPremiumMatches(target.getUniqueId(), 0);
        sender.sendMessage(ChatColor.GREEN + target.getName() + "'s new premium matches amount is " + newAmount + "!");
    }
}

