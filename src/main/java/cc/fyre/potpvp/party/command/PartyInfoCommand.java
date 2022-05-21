/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.party.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import com.google.common.base.Joiner;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class PartyInfoCommand {
    @Command(names={"party info", "p info", "t info", "team info", "f info", "p i", "t i", "f i", "party i", "team i"}, permission="")
    public static void partyInfo(Player sender, @Param(name="player", defaultValue="self") Player target) {
        Party party = PotPvP.getInstance().getPartyHandler().getParty(target);
        if (party == null) {
            if (sender == target) {
                sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " isn't in a party.");
            }
            return;
        }
        String leaderName = FrozenUUIDCache.name((UUID)party.getLeader());
        int memberCount = party.getMembers().size();
        String members2 = Joiner.on((String)", ").join(PatchedPlayerUtils.mapToNames(party.getMembers()));
        sender.sendMessage(ChatColor.GRAY + PotPvPLang.LONG_LINE);
        sender.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.GOLD + leaderName);
        sender.sendMessage(ChatColor.YELLOW + "Members " + ChatColor.GOLD + "(" + memberCount + ")" + ChatColor.YELLOW + ": " + ChatColor.GRAY + members2);
        switch (party.getAccessRestriction()) {
            case PUBLIC: {
                sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.GREEN + "Open");
                break;
            }
            case INVITE_ONLY: {
                sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.GOLD + "Invite-Only");
                break;
            }
            case PASSWORD: {
                if (party.isLeader(sender.getUniqueId())) {
                    HoverEvent.Action showText = HoverEvent.Action.SHOW_TEXT;
                    BaseComponent[] passwordComponent = new BaseComponent[]{new TextComponent(party.getPassword())};
                    ComponentBuilder builder = new ComponentBuilder("Privacy: ").color(ChatColor.YELLOW);
                    builder.append("Password Protected ").color(ChatColor.RED);
                    builder.append("[Hover for password]").color(ChatColor.GRAY);
                    builder.event(new HoverEvent(showText, passwordComponent));
                    sender.spigot().sendMessage(builder.create());
                    break;
                }
                sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.RED + "Password Protected");
                break;
            }
        }
        sender.sendMessage(ChatColor.GRAY + PotPvPLang.LONG_LINE);
    }
}

