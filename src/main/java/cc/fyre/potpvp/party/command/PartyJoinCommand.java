/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.party.PartyInvite;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

public final class PartyJoinCommand {
    private static final String NO_PASSWORD_PROVIDED = "skasjkdasdjhksahjd";

    @Command(names={"party join", "p join", "t join", "team join", "f join"}, permission="")
    public static void partyJoin(Player sender, @Param(name="player") Player target, @Param(name="password", defaultValue="skasjkdasdjhksahjd") String providedPassword) {
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        Party targetParty = partyHandler.getParty(target);
        if (partyHandler.hasParty(sender)) {
            sender.sendMessage(ChatColor.RED + "You are already in a party. You must leave your current party first.");
            return;
        }
        if (targetParty == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not in a party.");
            return;
        }
        PartyInvite invite = targetParty.getInvite(sender.getUniqueId());
        switch (targetParty.getAccessRestriction()) {
            case PUBLIC: {
                targetParty.join(sender);
                break;
            }
            case INVITE_ONLY: {
                if (invite != null) {
                    targetParty.join(sender);
                    break;
                }
                sender.sendMessage(ChatColor.RED + "You don't have an invitation to this party.");
                break;
            }
            case PASSWORD: {
                if (providedPassword.equals(NO_PASSWORD_PROVIDED) && invite == null) {
                    sender.sendMessage(ChatColor.RED + "You need the password or an invitation to join this party.");
                    sender.sendMessage(ChatColor.GRAY + "To join with a password, use " + ChatColor.YELLOW + "/party join " + target.getName() + " <password>");
                    return;
                }
                String correctPassword = targetParty.getPassword();
                if (invite == null && !correctPassword.equals(providedPassword)) {
                    sender.sendMessage(ChatColor.RED + "Invalid password.");
                    break;
                }
                targetParty.join(sender);
                break;
            }
        }
    }
}

