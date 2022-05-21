/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerHealthChangeEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 */
package cc.fyre.potpvp.match.listener;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchSpectatorJoinEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorLeaveEvent;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public final class MatchHealthDisplayListener
implements Listener {
    private static final String OBJECTIVE_NAME = "HealthDisplay";
    private static Field aField = null;
    private static Field bField = null;
    private static Field cField = null;
    private static Field dField = null;

    @EventHandler
    public void onMatchCountdownStart(MatchStartEvent event) {
        Match match = event.getMatch();
        if (!match.getKitType().isHealthShown()) {
            return;
        }
        Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), () -> {
            for (Player player : this.getRecipients(match)) {
                this.initialize(player);
            }
            for (Player player : this.getPlayers(match)) {
                this.sendToAll(player, match);
            }
        }, 1L);
    }

    @EventHandler
    public void onMatchTerminate(MatchTerminateEvent event) {
        Match match = event.getMatch();
        if (!match.getKitType().isHealthShown()) {
            return;
        }
        for (Player player : this.getRecipients(match)) {
            this.clearAll(player);
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        if (match.getKitType().isHealthShown()) {
            for (Player viewer : this.getRecipients(match)) {
                this.clear(viewer, player);
            }
        }
    }

    @EventHandler
    public void onSpectatorJoin(MatchSpectatorJoinEvent event) {
        Match match = event.getMatch();
        if (!match.getKitType().isHealthShown()) {
            return;
        }
        this.initialize(event.getSpectator());
        this.sendAllTo(event.getSpectator(), match);
    }

    @EventHandler
    public void onSpectatorLeave(MatchSpectatorLeaveEvent event) {
        if (!event.getMatch().getKitType().isHealthShown()) {
            return;
        }
        this.clearAll(event.getSpectator());
    }

    @EventHandler
    public void onHealthChange(PlayerHealthChangeEvent event) {
        Player player = event.getPlayer();
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        if (!matchHandler.isPlayingMatch(player)) {
            return;
        }
        Match match = matchHandler.getMatchPlaying(player);
        if (match.getKitType().isHealthShown()) {
            this.sendToAll(player, match);
        }
    }

    private void sendAllTo(Player viewer, Match match) {
        Objective objective = viewer.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
        if (objective == null) {
            return;
        }
        for (Player target : this.getPlayers(match)) {
            try {
                PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
                aField.set(packet, target.getDisguisedName());
                bField.set(packet, OBJECTIVE_NAME);
                cField.set(packet, this.getHealth(target));
                dField.set(packet, 0);
                ((CraftPlayer)viewer).getHandle().playerConnection.sendPacket((Packet)packet);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToAll(Player target, Match match) {
        try {
            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
            aField.set(packet, target.getDisguisedName());
            bField.set(packet, OBJECTIVE_NAME);
            cField.set(packet, this.getHealth(target));
            dField.set(packet, 0);
            for (Player viewer : this.getRecipients(match)) {
                ((CraftPlayer)viewer).getHandle().playerConnection.sendPacket((Packet)packet);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize(Player player) {
        if (player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) == null) {
            Objective objective = player.getScoreboard().registerNewObjective(OBJECTIVE_NAME, "dummy");
            objective.setDisplayName(ChatColor.DARK_RED + "\u2764");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
    }

    private void clearAll(Player player) {
        Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
        if (objective != null) {
            objective.unregister();
        }
        player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
    }

    private void clear(Player viewer, Player target) {
        viewer.getScoreboard().resetScores(target.getDisguisedName());
    }

    private List<Player> getRecipients(Match match) {
        ArrayList<Player> recipients = new ArrayList<Player>();
        recipients.addAll(this.getPlayers(match));
        match.getSpectators().stream().map(Bukkit::getPlayer).forEach(recipients::add);
        return recipients;
    }

    private List<Player> getPlayers(Match match) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (MatchTeam team : match.getTeams()) {
            team.getAliveMembers().stream().map(Bukkit::getPlayer).forEach(players::add);
        }
        return players;
    }

    private int getHealth(Player player) {
        return (int)Math.ceil(player.getHealth() + (double)((CraftPlayer)player).getHandle().getAbsorptionHearts());
    }

    static {
        try {
            aField = PacketPlayOutScoreboardScore.class.getDeclaredField("a");
            aField.setAccessible(true);
            bField = PacketPlayOutScoreboardScore.class.getDeclaredField("b");
            bField.setAccessible(true);
            cField = PacketPlayOutScoreboardScore.class.getDeclaredField("c");
            cField.setAccessible(true);
            dField = PacketPlayOutScoreboardScore.class.getDeclaredField("d");
            dField.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

