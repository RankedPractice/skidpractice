/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.PartyHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;

public final class PartyCreateCommand {
    @Command(names={"party create", "p create", "t create", "team create", "f create"}, permission="")
    public static void partyCreate(Player sender) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        if (!PotPvP.getInstance().getLobbyHandler().isInLobby(sender)) {
            sender.sendMessage(ChatColor.RED + "You must create a party from the lobby.");
            return;
        }
        if (PotPvP.getInstance().getQueueHandler().isQueued(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Leave your queue to create a party..");
            return;
        }
        if (PotPvP.getInstance().getPartyHandler().hasParty(sender)) {
            sender.sendMessage(ChatColor.RED + "You already belong to a party.");
            return;
        }
        partyHandler.getOrCreateParty(sender);
        sender.sendMessage(ChatColor.YELLOW + "Created a new party.");
    }
}

