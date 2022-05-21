/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.util.Callback
 */
package cc.fyre.potpvp.duel.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.arena.menu.select.SelectArenaMenu;
import cc.fyre.potpvp.duel.DuelHandler;
import cc.fyre.potpvp.duel.PartyDuelInvite;
import cc.fyre.potpvp.duel.PlayerDuelInvite;
import cc.fyre.potpvp.duel.command.AcceptCommand;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.menu.select.SelectKitTypeMenu;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.util.DisguiseUtil;
import cc.fyre.potpvp.validation.PotPvPValidation;
import java.util.UUID;
import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.util.Callback;

public final class DuelCommand {
    @Command(names={"duel", "1v1"}, permission="")
    public static void duel(Player sender, @Param(name="player") Player target) {
        if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You can't duel yourself!");
            return;
        }
        PartyHandler partyHandler = PotPvP.getInstance().getPartyHandler();
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        Party senderParty = partyHandler.getParty(sender);
        Party targetParty = partyHandler.getParty(target);
        if (senderParty != null && targetParty != null) {
            if (!PotPvPValidation.canSendDuel(senderParty, targetParty, sender)) {
                return;
            }
            new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                sender.closeInventory();
                if (sender.hasPermission("potpvp.arena.select")) {
                    new SelectArenaMenu((KitType)kitType, (Callback<ArenaSchematic>)((Callback)schematic -> {
                        Party newSenderParty = partyHandler.getParty(sender);
                        Party newTargetParty = partyHandler.getParty(target);
                        if (newSenderParty != null && newTargetParty != null) {
                            if (newSenderParty.isLeader(sender.getUniqueId())) {
                                DuelCommand.duel(sender, newSenderParty, newTargetParty, kitType, schematic, false);
                            } else {
                                sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
                            }
                        }
                    }), ChatColor.BLUE.toString() + ChatColor.BOLD + "Select an arena...").openMenu(sender);
                } else {
                    Party newSenderParty = partyHandler.getParty(sender);
                    Party newTargetParty = partyHandler.getParty(target);
                    if (newSenderParty != null && newTargetParty != null) {
                        if (newSenderParty.isLeader(sender.getUniqueId())) {
                            DuelCommand.duel(sender, newSenderParty, newTargetParty, kitType, null, false);
                        } else {
                            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
                        }
                    }
                }
            }), "Select a kit type...").openMenu(sender);
        } else if (senderParty == null && targetParty == null) {
            if (!PotPvPValidation.canSendDuel(sender, target)) {
                return;
            }
            if (target.hasPermission("potpvp.famous") && System.currentTimeMillis() - lobbyHandler.getLastLobbyTime(target) < 3000L) {
                sender.sendMessage(ChatColor.RED + target.getName() + " just returned to the lobby, please wait a moment.");
                return;
            }
            new SelectKitTypeMenu((Callback<KitType>)((Callback)kitType -> {
                sender.closeInventory();
                if (sender.hasPermission("potpvp.arena.select")) {
                    new SelectArenaMenu((KitType)kitType, (Callback<ArenaSchematic>)((Callback)schematic -> DuelCommand.duel(sender, target, kitType, schematic, false)), ChatColor.BLUE.toString() + ChatColor.BOLD + "Select an arena...").openMenu(sender);
                } else {
                    DuelCommand.duel(sender, target, kitType, null, false);
                }
            }), "Select a kit type...").openMenu(sender);
        } else if (senderParty == null) {
            sender.sendMessage(ChatColor.RED + "You must create a party to duel " + target.getName() + "'s party.");
        } else {
            sender.sendMessage(ChatColor.RED + "You must leave your party to duel " + target.getName() + ".");
        }
    }

    public static void duel(Player sender, Player target, KitType kitType, ArenaSchematic arenaSchematic, boolean rematch) {
        if (!PotPvPValidation.canSendDuel(sender, target)) {
            return;
        }
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        PlayerDuelInvite autoAcceptInvite = duelHandler.findInvite(target, sender);
        if (autoAcceptInvite != null && autoAcceptInvite.getKitType() == kitType) {
            AcceptCommand.accept(sender, target);
            return;
        }
        PlayerDuelInvite alreadySentInvite = duelHandler.findInvite(sender, target);
        if (alreadySentInvite != null) {
            if (alreadySentInvite.getKitType() == kitType) {
                sender.sendMessage(ChatColor.YELLOW + "You have already invited " + ChatColor.AQUA + target.getName() + ChatColor.YELLOW + " to a " + kitType.getColoredDisplayName() + ChatColor.YELLOW + " duel.");
                return;
            }
            duelHandler.removeInvite(alreadySentInvite);
        }
        if (rematch) {
            for (TextComponent[] parts : DuelCommand.createRematchNotification(sender.getDisguisedName())) {
                target.spigot().sendMessage((BaseComponent[])parts);
            }
        } else {
            DuelCommand.createInviteNotification(sender.getName(), sender.getDisguisedName(), kitType.getDisplayName()).send(target);
        }
        if (arenaSchematic == null) {
            sender.sendMessage(ChatColor.YELLOW + "Duel request sent to " + ChatColor.DARK_GREEN + target.getDisguisedName() + ChatColor.YELLOW + " with " + ChatColor.DARK_GREEN + kitType.getDisplayName() + ChatColor.YELLOW + ".");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Duel request sent to " + ChatColor.DARK_GREEN + target.getDisguisedName() + ChatColor.YELLOW + " with " + ChatColor.DARK_GREEN + kitType.getDisplayName() + ChatColor.YELLOW + " on arena " + ChatColor.DARK_GREEN + arenaSchematic.getName() + ChatColor.YELLOW + ".");
        }
        duelHandler.insertInvite(new PlayerDuelInvite(sender, target, kitType, arenaSchematic));
    }

    public static void duel(Player sender, Party senderParty, Party targetParty, KitType kitType, ArenaSchematic arenaSchematic, boolean rematch) {
        if (!PotPvPValidation.canSendDuel(senderParty, targetParty, sender)) {
            return;
        }
        DuelHandler duelHandler = PotPvP.getInstance().getDuelHandler();
        PartyDuelInvite autoAcceptInvite = duelHandler.findInvite(targetParty, senderParty);
        String targetPartyLeader = Bukkit.getPlayer((UUID)targetParty.getLeader()).getDisplayName();
        if (autoAcceptInvite != null && autoAcceptInvite.getKitType() == kitType) {
            AcceptCommand.accept(sender, Bukkit.getPlayer((UUID)targetParty.getLeader()));
            return;
        }
        PartyDuelInvite alreadySentInvite = duelHandler.findInvite(senderParty, targetParty);
        if (alreadySentInvite != null) {
            if (alreadySentInvite.getKitType() == kitType) {
                sender.sendMessage(ChatColor.GOLD + "You have already invited " + ChatColor.RESET + targetPartyLeader + "'s party" + ChatColor.GOLD + " to a " + ChatColor.LIGHT_PURPLE + kitType.getDisplayName() + ChatColor.GOLD + " duel.");
                return;
            }
            duelHandler.removeInvite(alreadySentInvite);
        }
        if (rematch) {
            Player targetPlayer = Bukkit.getPlayer((UUID)targetParty.getLeader());
            for (TextComponent[] parts : DuelCommand.createRematchNotification(sender.getDisguisedName() + "'s Party (" + senderParty.getMembers().size() + ")")) {
                targetPlayer.spigot().sendMessage((BaseComponent[])parts);
            }
        } else {
            DuelCommand.createInviteNotification(sender.getName(), sender.getDisguisedName() + "'s Party (" + senderParty.getMembers().size() + ")", kitType.getDisplayName()).send(Bukkit.getPlayer((UUID)targetParty.getLeader()));
        }
        if (arenaSchematic == null) {
            sender.sendMessage(ChatColor.YELLOW + "Duel request sent to " + ChatColor.DARK_GREEN + DisguiseUtil.getDisguisedName(targetParty.getLeader()) + "'s Party (" + targetParty.getMembers().size() + ")" + ChatColor.YELLOW + " with " + ChatColor.DARK_GREEN + kitType.getDisplayName() + ChatColor.YELLOW + ".");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Duel request sent to " + ChatColor.DARK_GREEN + DisguiseUtil.getDisguisedName(targetParty.getLeader()) + "'s Party (" + targetParty.getMembers().size() + ")" + ChatColor.YELLOW + " with " + ChatColor.DARK_GREEN + kitType.getDisplayName() + ChatColor.YELLOW + " on arena " + ChatColor.DARK_GREEN + arenaSchematic.getName() + ChatColor.YELLOW + ".");
        }
        duelHandler.insertInvite(new PartyDuelInvite(senderParty, targetParty, kitType, arenaSchematic));
    }

    private static FancyMessage createInviteNotification(String senderName, String senderString, String kitType) {
        return new FancyMessage("").then(ChatColor.DARK_GREEN + senderString).then(" has requested to duel you with ").color(ChatColor.YELLOW).then(kitType).color(ChatColor.DARK_GREEN).then(". ").color(ChatColor.YELLOW).then("Click this message to accept.").color(ChatColor.YELLOW).command("/accept " + senderName).formattedTooltip(new FancyMessage(ChatColor.GREEN + "Click here to accept"));
    }

    private static TextComponent[][] createRematchNotification(String sender) {
        TextComponent firstAndLast = new TextComponent(ChatColor.GRAY + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        TextComponent padding = new TextComponent(ChatColor.GRAY + "\u2588\u2588");
        TextComponent spacer = new TextComponent(" ");
        TextComponent grayDot = new TextComponent(ChatColor.GRAY + "\u2588");
        TextComponent goldDot = new TextComponent(ChatColor.GOLD + "\u2588");
        TextComponent namePart = new TextComponent(ChatColor.AQUA + sender + ChatColor.YELLOW + " has requested a");
        TextComponent requestPart = new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + "REMATCH");
        ClickEvent.Action runCommand = ClickEvent.Action.RUN_COMMAND;
        HoverEvent.Action showText = HoverEvent.Action.SHOW_TEXT;
        TextComponent commandPart = new TextComponent(ChatColor.AQUA + "Click here to accept");
        commandPart.setClickEvent(new ClickEvent(runCommand, "/accept " + sender));
        commandPart.setHoverEvent(new HoverEvent(showText, new BaseComponent[]{new TextComponent(ChatColor.GREEN + "Click here to accept")}));
        return new TextComponent[][]{{firstAndLast}, {padding, goldDot, goldDot, goldDot, goldDot, goldDot, grayDot, grayDot, grayDot}, {padding, goldDot, grayDot, grayDot, grayDot, grayDot, goldDot, padding}, {padding, goldDot, grayDot, grayDot, grayDot, grayDot, goldDot, padding}, {padding, goldDot, goldDot, goldDot, goldDot, goldDot, grayDot, grayDot, grayDot, spacer, namePart}, {padding, goldDot, grayDot, goldDot, grayDot, grayDot, grayDot, padding, spacer, requestPart}, {padding, goldDot, grayDot, grayDot, goldDot, grayDot, grayDot, padding, spacer, commandPart}, {padding, goldDot, grayDot, grayDot, grayDot, goldDot, grayDot, padding}, {padding, goldDot, grayDot, grayDot, grayDot, grayDot, goldDot, padding}, {firstAndLast}};
    }
}

