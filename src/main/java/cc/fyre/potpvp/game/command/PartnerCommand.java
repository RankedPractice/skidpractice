/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.Param
 */
package cc.fyre.potpvp.game.command;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.PlayerGameInteractionEvent;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.Param;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0003\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/command/PartnerCommand;", "", "()V", "partner", "", "player", "Lorg/bukkit/entity/Player;", "potpvp-si"})
public final class PartnerCommand {
    @NotNull
    public static final PartnerCommand INSTANCE = new PartnerCommand();

    private PartnerCommand() {
    }

    @Command(names={"partner", "event partner"}, permission="")
    @JvmStatic
    public static final void partner(@NotNull Player player, @Param(name="partner") @NotNull Player partner) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(partner, "partner");
        Game game = PotPvP.getInstance().gameHandler.getOngoingGame();
        if (game == null) {
            return;
        }
        Game game2 = game;
        GameEventLogic logic = game2.getLogic();
        Object object = game2.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass());
        if (object == null) {
            return;
        }
        if (!(logic instanceof GameTeamEventLogic)) {
            return;
        }
        if (game2.getPlayers().contains(player) && game2.getPlayers().contains(partner) && game2.getState() == GameState.STARTING) {
            if (((GameTeamEventLogic)logic).contains(player)) {
                player.sendMessage(Intrinsics.stringPlus(ChatColor.RED.toString(), "You're already in a team."));
                return;
            }
            if (((GameTeamEventLogic)logic).contains(partner)) {
                player.sendMessage(ChatColor.RED.toString() + partner.getName() + " is already in a team!");
                return;
            }
            if (Intrinsics.areEqual(((GameTeamEventLogic)logic).getInvites().get(player.getUniqueId()), partner.getUniqueId()) && !Intrinsics.areEqual(partner, player)) {
                player.sendMessage(ChatColor.RED.toString() + "You've already sent " + partner.getName() + " a team request.");
                return;
            }
            if (Intrinsics.areEqual(((GameTeamEventLogic)logic).getInvites().get(partner.getUniqueId()), player.getUniqueId())) {
                player.sendMessage(ChatColor.GREEN.toString() + "You joined " + partner.getName() + "'s team.");
                partner.sendMessage(ChatColor.GREEN.toString() + player.getName() + " joined your team.");
                ((GameTeamEventLogic)logic).getInvites().remove(player.getUniqueId());
                ((GameTeamEventLogic)logic).getInvites().remove(partner.getUniqueId());
                Bukkit.getPluginManager().callEvent((Event)new PlayerGameInteractionEvent(player, game2));
                Bukkit.getPluginManager().callEvent((Event)new PlayerGameInteractionEvent(partner, game2));
                object = new Player[]{player, partner};
                ((GameTeamEventLogic)logic).getParticipants().add(new GameTeam((Player)object));
                return;
            }
            object = ((GameTeamEventLogic)logic).getInvites();
            UUID uUID = player.getUniqueId();
            Intrinsics.checkNotNullExpressionValue(uUID, "player.uniqueId");
            UUID uUID2 = partner.getUniqueId();
            Intrinsics.checkNotNullExpressionValue(uUID2, "partner.uniqueId");
            boolean bl = false;
            object.put(uUID, uUID2);
            partner.sendMessage("");
            partner.sendMessage(player.getDisplayName() + ChatColor.YELLOW + " would like to team with you.");
            new FancyMessage("Click here to accept.").color(ChatColor.YELLOW).command(Intrinsics.stringPlus("/partner ", player.getName())).formattedTooltip(new FancyMessage(ChatColor.YELLOW + "Click here to accept.")).send(partner);
            partner.sendMessage("");
            player.sendMessage(ChatColor.YELLOW.toString() + "You sent a team request to " + partner.getDisplayName() + ChatColor.YELLOW + '.');
        }
    }
}

