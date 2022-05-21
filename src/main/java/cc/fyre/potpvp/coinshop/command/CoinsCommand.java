/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.coinshop.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.coinshop.menu.CoinsMenu;
import cc.fyre.potpvp.util.CC;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public class CoinsCommand {
    @Command(names={"coinshop"}, permission="")
    public static void coinshop(Player sender) {
        new CoinsMenu().openMenu(sender);
    }

    @Command(names={"coins set"}, permission="op")
    public static void setStars(CommandSender sender, @Param(name="target") UUID target, @Param(name="amount") int amount) {
        PotPvP.getInstance().getProfileManager().getProfile(target).setCoins(amount);
        PotPvP.getInstance().getProfileManager().getProfile(target).save();
        sender.sendMessage(CC.translate("&aSuccess, the players stars is now " + amount));
    }

    @Command(names={"coins add"}, permission="op")
    public static void addStars(CommandSender sender, @Param(name="target") UUID target, @Param(name="amount") int amount) {
        int toAdd = PotPvP.getInstance().getProfileManager().getProfile(target).getCoins() + amount;
        PotPvP.getInstance().getProfileManager().getProfile(target).setCoins(toAdd);
        PotPvP.getInstance().getProfileManager().getProfile(target).save();
        sender.sendMessage(CC.translate("&aSuccess, the players stars is now " + toAdd));
    }
}

