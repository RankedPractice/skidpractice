/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  mkremins.fanciful.FancyMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  rip.bridge.qlib.command.Command
 *  rip.bridge.qlib.command.FrozenCommandHandler
 *  rip.bridge.qlib.command.Param
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.tournament;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.PotPvPLang;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.party.Party;
import cc.fyre.potpvp.tournament.Tournament;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.bridge.qlib.command.Command;
import rip.bridge.qlib.command.FrozenCommandHandler;
import rip.bridge.qlib.command.Param;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public class TournamentHandler
implements Listener {
    private Tournament tournament = null;
    private static TournamentHandler instance;
    private static List<TournamentStatus> allStatuses;

    public TournamentHandler() {
        instance = this;
        FrozenCommandHandler.registerClass(this.getClass());
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)PotPvP.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)PotPvP.getInstance(), () -> {
            if (this.tournament != null) {
                this.tournament.check();
            }
        }, 20L, 20L);
        this.populateTournamentStatuses();
    }

    public boolean isInTournament(Party party) {
        return this.tournament != null && this.tournament.isInTournament(party);
    }

    public boolean isInTournament(Match match) {
        return this.tournament != null && this.tournament.getMatches().contains(match);
    }

    @Command(names={"tournament start"}, permission="tournament.create")
    public static void tournamentCreate(CommandSender sender, @Param(name="kit-type") KitType type2, @Param(name="teamSize") int teamSize, @Param(name="requiredTeams") int requiredTeams) {
        if (instance.getTournament() != null) {
            sender.sendMessage(ChatColor.RED + "There's already an ongoing tournament!");
            return;
        }
        if (type2 == null) {
            sender.sendMessage(ChatColor.RED + "Kit type not found!");
            return;
        }
        if (teamSize < 1 || 10 < teamSize) {
            sender.sendMessage(ChatColor.RED + "Invalid team size range. Acceptable inputs: 1 -> 10");
            return;
        }
        if (requiredTeams < 4) {
            sender.sendMessage(ChatColor.RED + "Required teams must be at least 4.");
            return;
        }
        FancyMessage joinMessage = new FancyMessage("").then("A ").color(ChatColor.GRAY).then("tournament").color(ChatColor.GOLD).style(new ChatColor[]{ChatColor.BOLD}).then(" has started.").color(ChatColor.GRAY).then(" Type ").color(ChatColor.GRAY).then("/join").color(ChatColor.GOLD).command("/join").then(" to play.").color(ChatColor.GRAY).then(" (0/" + (teamSize < 3 ? teamSize * requiredTeams : requiredTeams) + ")").color(ChatColor.GRAY);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("");
            joinMessage.send(player);
            player.sendMessage("");
        }
        final Tournament tournament = new Tournament(type2, teamSize, requiredTeams);
        instance.setTournament(tournament);
        new BukkitRunnable(){

            public void run() {
                if (instance.getTournament() == tournament) {
                    tournament.broadcastJoinMessage();
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 1200L, 1200L);
    }

    @Command(names={"tournament join", "join", "jointournament"}, permission="")
    public static void tournamentJoin(Player sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to join.");
            return;
        }
        int tournamentTeamSize = instance.getTournament().getRequiredPartySize();
        if (!(instance.getTournament().getCurrentRound() == -1 && instance.getTournament().getBeginNextRoundIn() == 31 || instance.getTournament().getCurrentRound() == 0 && sender.hasPermission("tournaments.joinduringcountdown"))) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }
        Party senderParty = PotPvP.getInstance().getPartyHandler().getParty(sender);
        if (senderParty == null) {
            if (tournamentTeamSize == 1) {
                senderParty = PotPvP.getInstance().getPartyHandler().getOrCreateParty(sender);
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have a team to join the tournament with!");
                return;
            }
        }
        int notInLobby = 0;
        int queued = 0;
        for (UUID member : senderParty.getMembers()) {
            if (!PotPvP.getInstance().getLobbyHandler().isInLobby(Bukkit.getPlayer((UUID)member))) {
                ++notInLobby;
            }
            if (PotPvP.getInstance().getQueueHandler().getQueueEntry(member) == null) continue;
            ++queued;
        }
        if (notInLobby != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team aren't in the lobby.");
            return;
        }
        if (queued != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team are currently queued.");
            return;
        }
        if (!senderParty.getLeader().equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You must be the leader of your team to join the tournament.");
            return;
        }
        if (instance.isInTournament(senderParty)) {
            sender.sendMessage(ChatColor.RED + "Your team is already in the tournament!");
            return;
        }
        if (senderParty.getMembers().size() != instance.getTournament().getRequiredPartySize()) {
            sender.sendMessage(ChatColor.RED + "You need exactly " + instance.getTournament().getRequiredPartySize() + " members in your party to join the tournament.");
            return;
        }
        if (PotPvP.getInstance().getQueueHandler().getQueueEntry(senderParty) != null) {
            sender.sendMessage(ChatColor.RED + "You can't join the tournament if your party is currently queued.");
            return;
        }
        senderParty.message(ChatColor.GREEN + "Joined the tournament.");
        instance.getTournament().addParty(senderParty);
    }

    @Command(names = { "tournament status", "tstatus", "status" }, permission = "")
    public static void tournamentStatus(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no ongoing tournament to get the status of.");
            return;
        }

        sender.sendMessage(PotPvPLang.LONG_LINE);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Live &5Tournament &7Fights"));
        sender.sendMessage("");
        List<Match> ongoingMatches = instance.getTournament().getMatches().stream().filter(m -> m.getState() != MatchState.TERMINATED).collect(Collectors.toList());

        for (Match match : ongoingMatches) {
            MatchTeam firstTeam = match.getTeams().get(0);
            MatchTeam secondTeam = match.getTeams().get(1);

            if (firstTeam.getAllMembers().size() == 1) {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + FrozenUUIDCache.name(firstTeam.getFirstMember()) + ChatColor.GRAY + " vs " + ChatColor.LIGHT_PURPLE + FrozenUUIDCache.name(secondTeam.getFirstMember()));
            } else {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + FrozenUUIDCache.name(firstTeam.getFirstMember()) + ChatColor.GRAY + "'s team vs " + ChatColor.LIGHT_PURPLE + FrozenUUIDCache.name(secondTeam.getFirstMember()) + ChatColor.GRAY + "'s team");
            }
        }
        sender.sendMessage(PotPvPLang.LONG_LINE);
    }

    @Command(names={"tournament cancel", "tcancel"}, permission="op")
    public static void tournamentCancel(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to cancel.");
            return;
        }
        Bukkit.broadcastMessage((String)"");
        Bukkit.broadcastMessage((String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&7The &6&ltournament&7 was &6cancelled."));
        Bukkit.broadcastMessage((String)"");
        instance.setTournament(null);
    }

    @Command(names={"tournament forcestart"}, permission="op")
    public static void tournamentForceStart(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no tournament to force start.");
            return;
        }
        if (instance.getTournament().getCurrentRound() != -1 || instance.getTournament().getBeginNextRoundIn() != 31) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }
        instance.getTournament().start();
        sender.sendMessage(ChatColor.GREEN + "Force started tournament.");
    }

    private void populateTournamentStatuses() {
        List<KitType> viewableKits = KitType.getAllTypes().stream().filter(kit -> !kit.isHidden()).collect(Collectors.toList());
        allStatuses.add(new TournamentStatus(0, ImmutableList.of(1), ImmutableList.of(16, 32), viewableKits));
        allStatuses.add(new TournamentStatus(250, ImmutableList.of(1), ImmutableList.of(32), viewableKits));
        if (KitType.byId("NODEBUFF") != null) {
            allStatuses.add(new TournamentStatus(300, ImmutableList.of(1), ImmutableList.of(48, 64), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(400, ImmutableList.of(1), ImmutableList.of(64), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(500, ImmutableList.of(1), ImmutableList.of(128), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(600, ImmutableList.of(1), ImmutableList.of(128), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(700, ImmutableList.of(1), ImmutableList.of(128), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(800, ImmutableList.of(1), ImmutableList.of(128), (List<KitType>)ImmutableList.of(KitType.byId("NODEBUFF"))));
        }
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    static {
        allStatuses = Lists.newArrayList();
    }

    private static class TournamentStatus {
        private int minimumPlayerCount;
        private List<Integer> teamSizes;
        private List<Integer> teamCounts;
        private List<KitType> kitTypes;

        public TournamentStatus(int minimumPlayerCount, List<Integer> teamSizes, List<Integer> teamCounts, List<KitType> kitTypes) {
            this.minimumPlayerCount = minimumPlayerCount;
            this.teamSizes = teamSizes;
            this.teamCounts = teamCounts;
            this.kitTypes = kitTypes;
        }

        public static TournamentStatus forPlayerCount(int playerCount) {
            for (int i = allStatuses.size() - 1; 0 <= i; --i) {
                if (((TournamentStatus)allStatuses.get((int)i)).minimumPlayerCount > playerCount) continue;
                return (TournamentStatus)allStatuses.get(i);
            }
            throw new IllegalArgumentException("No suitable sizes found!");
        }

        public int getMinimumPlayerCount() {
            return this.minimumPlayerCount;
        }

        public List<Integer> getTeamSizes() {
            return this.teamSizes;
        }

        public List<Integer> getTeamCounts() {
            return this.teamCounts;
        }

        public List<KitType> getKitTypes() {
            return this.kitTypes;
        }
    }
}

