/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.elo.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class EloSetCommands {
    @Command(names={"elo setSolo"}, permission="op")
    public static void eloSetSolo(Player sender, @Param(name="target") Player target, @Param(name="kit type") KitType kitType, @Param(name="new elo") int newElo) {
        EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
        eloHandler.setElo(target, kitType, newElo);
        sender.sendMessage(ChatColor.YELLOW + "Set " + target.getName() + "'s " + kitType.getDisplayName() + " elo to " + newElo + ".");
    }

    @Command(names={"elo setTeam"}, permission="op")
    public static void eloSetTeam(Player sender, @Param(name="target") Player target, @Param(name="kit type") KitType kitType, @Param(name="new elo") int newElo) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        EloHandler eloHandler = PotPvP.getInstance().getEloHandler();
        Party targetParty = partyHandler.getParty(target);
        if (targetParty == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not in a party.");
            return;
        }
        eloHandler.setElo(targetParty.getMembers(), kitType, newElo);
        sender.sendMessage(ChatColor.YELLOW + "Set " + kitType.getDisplayName() + " elo of " + FrozenUUIDCache.name((UUID)targetParty.getLeader()) + "'s party to " + newElo + ".");
    }
}

