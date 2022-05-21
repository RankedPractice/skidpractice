/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.BlockVector
 *  org.bukkit.util.Vector
 *  org.spigotmc.SpigotConfig
 *  rip.bridge.qlib.nametag.FrozenNametagHandler
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.match;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.elo.EloCalculator;
import cc.fyre.potpvp.kit.Kit;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.match.MatchEndReason;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.event.MatchCountdownStartEvent;
import cc.fyre.potpvp.match.event.MatchEndEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorJoinEvent;
import cc.fyre.potpvp.match.event.MatchSpectatorLeaveEvent;
import cc.fyre.potpvp.match.event.MatchStartEvent;
import cc.fyre.potpvp.match.event.MatchTerminateEvent;
import cc.fyre.potpvp.match.replay.ReplayableAction;
import cc.fyre.potpvp.postmatchinv.PostMatchPlayer;
import cc.fyre.potpvp.profile.LoadProfile;
import cc.fyre.potpvp.queue.QueueType;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.util.CC;
import cc.fyre.potpvp.util.InventoryUtils;
import cc.fyre.potpvp.util.ItemListener;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import cc.fyre.potpvp.util.VisibilityUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.spigotmc.SpigotConfig;
import rip.bridge.qlib.nametag.FrozenNametagHandler;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class Match {
    private static final int MATCH_END_DELAY_SECONDS = 3;
    private final String _id = UUID.randomUUID().toString().substring(0, 7);
    private String replayId;
    private final KitType kitType;
    private final Arena arena;
    private Map<UUID, Integer> lives = Maps.newHashMap();
    private final List<MatchTeam> teams;
    private final Map<UUID, PostMatchPlayer> postMatchPlayers = new HashMap<UUID, PostMatchPlayer>();
    private final Set<UUID> spectators = new HashSet<UUID>();
    private Map<UUID, Integer> boxingHits = Maps.newHashMap();
    private Map<MatchTeam, Integer> hits = Maps.newHashMap();
    private MatchTeam winner;
    private Map<UUID, Integer> kills = Maps.newHashMap();
    private Map<UUID, Kit> usedKit = Maps.newHashMap();
    private Map<MatchTeam, Integer> wins = Maps.newHashMap();
    private MatchEndReason endReason;
    private MatchState state;
    private Date startedAt;
    private Date endedAt;
    private QueueType queueType;
    private boolean allowRematches;
    private EloCalculator.Result eloChange;
    private final Set<BlockVector> placedBlocks = new HashSet<BlockVector>();
    private final transient Set<UUID> spectatorMessagesUsed = new HashSet<UUID>();
    private Map<UUID, UUID> lastHit = Maps.newHashMap();
    private Map<UUID, Integer> combos = Maps.newHashMap();
    private Map<UUID, Integer> totalHits = Maps.newHashMap();
    private Map<UUID, Integer> longestCombo = Maps.newHashMap();
    private Map<UUID, Integer> missedPots = Maps.newHashMap();
    private List<ReplayableAction> replayableActions = Lists.newArrayList();
    private Set<UUID> allPlayers = Sets.newHashSet();
    private Set<UUID> disconnectedPlayers = Sets.newHashSet();
    private Set<UUID> winningPlayers;
    private Set<UUID> losingPlayers;
    private int round = 1;

    public Match(KitType kitType, Arena arena, List<MatchTeam> teams, QueueType queueType, boolean allowRematches) {
        this.kitType = (KitType)Preconditions.checkNotNull((Object)kitType, (Object)"kitType");
        this.arena = (Arena)Preconditions.checkNotNull((Object)arena, (Object)"arena");
        this.teams = ImmutableList.copyOf(teams);
        this.queueType = queueType;
        this.allowRematches = allowRematches;
        this.saveState();
    }

    private void saveState() {
        if (this.kitType.isBuildingAllowed()) {
            this.arena.takeSnapshot();
        }
    }

    public void checkBoxingHits() {
        MatchTeam teamA = this.teams.get(0);
        MatchTeam teamB = this.teams.get(1);
        int[] playerA = new int[]{0};
        teamA.getAllMembers().forEach(u -> {
            playerA[0] = this.boxingHits.getOrDefault(u, 0);
        });
        int[] playerB = new int[]{0};
        teamB.getAllMembers().forEach(u -> {
            playerB[0] = this.boxingHits.getOrDefault(u, 0);
        });
        if (playerA[0] == 100 * teamA.getAllMembers().size()) {
            teamB.getAliveMembers().forEach(u -> Bukkit.getPlayer((UUID)u).setHealth(0.0));
            this.endMatch(MatchEndReason.ENEMIES_ELIMINATED);
        } else if (playerB[0] == 100 * teamB.getAllMembers().size()) {
            teamA.getAliveMembers().forEach(u -> Bukkit.getPlayer((UUID)u).setHealth(0.0));
            this.endMatch(MatchEndReason.ENEMIES_ELIMINATED);
        }
    }

    void startCountdown(boolean roundReset) {
        this.state = MatchState.COUNTDOWN;
        Map<UUID, Match> playingCache = PotPvP.getInstance().getMatchHandler().getPlayingMatchCache();
        HashSet<Player> updateVisibility = new HashSet<Player>();
        for (MatchTeam team : this.getTeams()) {
            for (UUID playerUuid : team.getAllMembers()) {
                Player player = Bukkit.getPlayer((UUID)playerUuid);
                if (player == null) continue;
                if (roundReset) {
                    if (this.spectators.contains(playerUuid)) {
                        this.removeSpectator(player, false, false);
                    }
                    team.markAlive(playerUuid);
                }
                if (!team.isAlive(playerUuid)) continue;
                if (this.kitType.getKnockbackName() != null) {
                    player.setKbProfile(SpigotConfig.getKnockbackByName((String)this.kitType.getKnockbackName()));
                }
                playingCache.put(player.getUniqueId(), this);
                Location spawn = (team == this.teams.get(0) ? this.arena.getTeam1Spawn() : this.arena.getTeam2Spawn()).clone();
                if (this.kitType.getId().equalsIgnoreCase("bedfight")) {
                    spawn = (team == this.teams.get(0) ? this.arena.getBlueSpawnSpawn() : this.arena.getRedSpawn()).clone();
                }
                Vector oldDirection = spawn.getDirection();
                Block block = spawn.getBlock();
                while (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                    if ((block = block.getRelative(BlockFace.DOWN)).getY() > 0) continue;
                    block = spawn.getBlock();
                    break;
                }
                spawn = block.getLocation();
                spawn.setDirection(oldDirection);
                spawn.add(0.5, 0.0, 0.5);
                this.kills.put(player.getUniqueId(), 0);
                this.wins.put(team, 0);
                player.teleport(spawn);
                team.setSpawnLoc(spawn);
                player.getInventory().setHeldItemSlot(0);
                FrozenNametagHandler.reloadPlayer((Player)player);
                FrozenNametagHandler.reloadOthersFor((Player)player);
                updateVisibility.add(player);
                PatchedPlayerUtils.resetInventory(player, GameMode.SURVIVAL);
            }
        }
        updateVisibility.forEach(VisibilityUtils::updateVisibilityFlicker);
        Bukkit.getPluginManager().callEvent((Event)new MatchCountdownStartEvent(this));
        new BukkitRunnable(){
            int countdownTimeRemaining;
            {
                this.countdownTimeRemaining = Match.this.kitType.getId().equals("SUMO") ? 5 : 5;
            }

            public void run() {
                if (Match.this.state != MatchState.COUNTDOWN) {
                    this.cancel();
                    return;
                }
                if (this.countdownTimeRemaining == 0) {
                    Match.this.playSoundAll(Sound.NOTE_PLING, 2.0f);
                    Match.this.startMatch();
                    return;
                }
                if (this.countdownTimeRemaining <= 3) {
                    Match.this.playSoundAll(Sound.NOTE_PLING, 1.0f);
                }
                Match.this.messageAll(ChatColor.GREEN.toString() + "Starting in " + ChatColor.YELLOW + this.countdownTimeRemaining + ChatColor.GREEN + " second" + (this.countdownTimeRemaining == 1 ? "" : "s") + "...");
                --this.countdownTimeRemaining;
            }
        }.runTaskTimer((Plugin)PotPvP.getInstance(), 0L, 20L);
    }

    private void startMatch() {
        this.state = MatchState.IN_PROGRESS;
        this.startedAt = new Date();
        this.messageAll(ChatColor.GREEN + (this.kitType.id.equals("SUMO") && this.queueType.isRanked() ? "The round has started!" : "The match has started!"));
        Bukkit.getPluginManager().callEvent((Event)new MatchStartEvent(this));
        for (Location bedLoc : this.arena.getBedLocs()) {
            bedLoc.getBlock().setType(Material.BED);
        }
    }

    public void endMatch(MatchEndReason reason) {
        if (this.state == MatchState.ENDING || this.state == MatchState.TERMINATED) {
            return;
        }
        this.state = MatchState.ENDING;
        this.endedAt = new Date();
        this.endReason = reason;
        try {
            for (MatchTeam matchTeam : this.getTeams()) {
                for (UUID playerUuid : matchTeam.getAllMembers()) {
                    this.allPlayers.add(playerUuid);
                    Player player = Bukkit.getPlayer((UUID)playerUuid);
                    if (player != null) {
                        player.setKbProfile(null);
                    }
                    if (!matchTeam.isAlive(playerUuid)) continue;
                    this.postMatchPlayers.computeIfAbsent(playerUuid, v -> new PostMatchPlayer(player, this.kitType.getHealingMethod(), this.totalHits.getOrDefault(player.getUniqueId(), 0), this.longestCombo.getOrDefault(player.getUniqueId(), 0), this.missedPots.getOrDefault(player.getUniqueId(), 0)));
                }
            }
            this.messageAll(ChatColor.GREEN + "The match has ended!");
            Bukkit.getPluginManager().callEvent((Event)new MatchEndEvent(this));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        int delayTicks = 60;
        if (JavaPlugin.getProvidingPlugin(this.getClass()).isEnabled()) {
            Bukkit.getScheduler().runTaskLater((Plugin)PotPvP.getInstance(), this::terminateMatch, (long)delayTicks);
        } else {
            this.terminateMatch();
        }
    }

    private void terminateMatch() {
        if (this.state == MatchState.TERMINATED) {
            return;
        }
        this.state = MatchState.TERMINATED;
        if (this.startedAt == null) {
            this.startedAt = new Date();
        }
        if (this.endedAt == null) {
            this.endedAt = new Date();
        }
        if (this.winner == null) {
            return;
        }
        this.winningPlayers = this.winner.getAllMembers();
        this.losingPlayers = this.teams.stream().filter(team -> team != this.winner).flatMap(team -> team.getAllMembers().stream()).collect(Collectors.toSet());
        Bukkit.getPluginManager().callEvent((Event)new MatchTerminateEvent(this));
        MatchHandler matchHandler = PotPvP.getInstance().getMatchHandler();
        LobbyHandler lobbyHandler = PotPvP.getInstance().getLobbyHandler();
        Map<UUID, Match> playingCache = matchHandler.getPlayingMatchCache();
        Map<UUID, Match> spectateCache = matchHandler.getSpectatingMatchCache();
        if (this.kitType.isBuildingAllowed()) {
            this.arena.restore();
        }
        PotPvP.getInstance().getArenaHandler().releaseArena(this.arena);
        matchHandler.removeMatch(this);
        this.getTeams().forEach(team -> team.getAllMembers().forEach(player -> {
            playingCache.remove(player);
            spectateCache.remove(player);
            if (Bukkit.getPlayer((UUID)player) != null) {
                lobbyHandler.returnToLobby(Bukkit.getPlayer((UUID)player));
            }
        }));
        this.spectators.forEach(player -> {
            if (Bukkit.getPlayer((UUID)player) != null) {
                playingCache.remove(player);
                spectateCache.remove(player);
                lobbyHandler.returnToLobby(Bukkit.getPlayer((UUID)player));
            }
        });
    }

    public Set<UUID> getSpectators() {
        return ImmutableSet.copyOf(this.spectators);
    }

    public Map<UUID, PostMatchPlayer> getPostMatchPlayers() {
        return ImmutableMap.copyOf(this.postMatchPlayers);
    }

    public void checkEnded() {
        if (this.state == MatchState.ENDING || this.state == MatchState.TERMINATED) {
            return;
        }
        if (!this.getKitType().getId().equalsIgnoreCase("boxing")) {
            if (this.getKitType().getId().equalsIgnoreCase("bridges")) {
                for (MatchTeam team : this.teams) {
                    if (this.wins.get(team) < 3) continue;
                    this.winner = team;
                    this.endMatch(MatchEndReason.ENEMIES_ELIMINATED);
                }
            } else {
                ArrayList<MatchTeam> teamsAlive = new ArrayList<MatchTeam>();
                if (this.getKitType().getId().equalsIgnoreCase("bedfight")) {
                    for (MatchTeam team : this.teams) {
                        if (team.bedBroken || team.getAliveMembers().isEmpty()) continue;
                        teamsAlive.add(team);
                    }
                } else {
                    for (MatchTeam team : this.teams) {
                        if (team.getAliveMembers().isEmpty()) continue;
                        teamsAlive.add(team);
                    }
                }
                if (teamsAlive.size() == 1) {
                    this.winner = (MatchTeam)teamsAlive.get(0);
                    this.endMatch(MatchEndReason.ENEMIES_ELIMINATED);
                }
            }
        } else {
            for (MatchTeam team : this.teams) {
                if (this.hits.getOrDefault(team, 0) < 100) continue;
                this.winner = team;
            }
            this.endMatch(MatchEndReason.ENEMIES_ELIMINATED);
        }
    }

    public boolean isSpectator(UUID uuid) {
        return this.spectators.contains(uuid);
    }

    public void addSpectator(Player player, Player target) {
        this.addSpectator(player, target, false);
    }

    public void addSpectator(Player player, Player target, boolean fromMatch) {
        if (!fromMatch && this.state == MatchState.ENDING) {
            player.sendMessage(ChatColor.RED + "This match is no longer available for spectating.");
            return;
        }
        Map<UUID, Match> spectateCache = PotPvP.getInstance().getMatchHandler().getSpectatingMatchCache();
        spectateCache.put(player.getUniqueId(), this);
        this.spectators.add(player.getUniqueId());
        if (!fromMatch) {
            Location tpTo = this.arena.getSpectatorSpawn();
            if (target != null) {
                tpTo = target.getLocation().clone().add(0.0, 1.5, 0.0);
            }
            player.teleport(tpTo);
            player.sendMessage(ChatColor.YELLOW + "Now spectating " + ChatColor.AQUA + this.getSimpleDescription(true) + ChatColor.YELLOW + "...");
            this.sendSpectatorMessage(player, ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " is now spectating.");
        } else {
            player.getInventory().setHeldItemSlot(0);
        }
        FrozenNametagHandler.reloadPlayer((Player)player);
        FrozenNametagHandler.reloadOthersFor((Player)player);
        VisibilityUtils.updateVisibility(player);
        PatchedPlayerUtils.resetInventory(player, GameMode.CREATIVE, KitType.teamFight, true);
        InventoryUtils.resetInventoryDelayed(player);
        player.setAllowFlight(true);
        player.setFlying(true);
        ItemListener.addButtonCooldown(player, 1500);
        Bukkit.getPluginManager().callEvent((Event)new MatchSpectatorJoinEvent(player, this));
    }

    public void removeSpectator(Player player) {
        this.removeSpectator(player, true, true);
    }

    public void removeSpectator(Player player, boolean returnToLobby, boolean sendMessage) {
        Map<UUID, Match> spectateCache = PotPvP.getInstance().getMatchHandler().getSpectatingMatchCache();
        spectateCache.remove(player.getUniqueId());
        this.spectators.remove(player.getUniqueId());
        ItemListener.addButtonCooldown(player, 1500);
        if (sendMessage) {
            this.sendSpectatorMessage(player, ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " is no longer spectating.");
        }
        if (returnToLobby) {
            PotPvP.getInstance().getLobbyHandler().returnToLobby(player);
        }
        Bukkit.getPluginManager().callEvent((Event)new MatchSpectatorLeaveEvent(player, this));
    }

    private void sendSpectatorMessage(Player spectator, String message) {
        if (spectator.hasMetadata("ModMode") || !this.spectatorMessagesUsed.add(spectator.getUniqueId())) {
            return;
        }
        SettingHandler settingHandler = PotPvP.getInstance().getSettingHandler();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == spectator) continue;
            boolean sameMatch = this.isSpectator(online.getUniqueId()) || this.getTeam(online.getUniqueId()) != null;
            boolean spectatorMessagesEnabled = settingHandler.getSetting(online, Setting.SHOW_SPECTATOR_JOIN_MESSAGES);
            if (!sameMatch || !spectatorMessagesEnabled) continue;
            online.sendMessage(message);
        }
    }

    public void markDead(Player player, boolean mlgrush) {
        MatchTeam team = this.getTeam(player.getUniqueId());
        if (team == null) {
            return;
        }
        if (mlgrush) {
            player.setFallDistance(0.0f);
            Kit kit = this.usedKit.getOrDefault(player.getUniqueId(), Kit.ofDefaultKit(this.kitType));
            Location spawn = team.getSpawnLoc();
            Vector oldDirection = spawn.getDirection();
            Block block = spawn.getBlock();
            while (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                if ((block = block.getRelative(BlockFace.DOWN)).getY() > 0) continue;
                block = spawn.getBlock();
                break;
            }
            spawn = block.getLocation();
            spawn.setDirection(oldDirection);
            spawn.add(0.5, 0.0, 0.5);
            player.teleport(spawn);
            kit.apply(player);
            return;
        }
        if (this.kitType.getId().equals("MLGRUSH")) {
            this.addSpectator(player, null, true);
        }
        Map<UUID, Match> playingCache = PotPvP.getInstance().getMatchHandler().getPlayingMatchCache();
        team.markDead(player.getUniqueId());
        playingCache.remove(player.getUniqueId());
        this.postMatchPlayers.put(player.getUniqueId(), new PostMatchPlayer(player, this.kitType.getHealingMethod(), this.totalHits.getOrDefault(player.getUniqueId(), 0), this.longestCombo.getOrDefault(player.getUniqueId(), 0), this.missedPots.getOrDefault(player.getUniqueId(), 0)));
        this.checkEnded();
    }

    public void markDead(Player player) {
        MatchTeam team = this.getTeam(player.getUniqueId());
        LoadProfile profile = LoadProfile.byUUID(player.getUniqueId());
        if (team == null) {
            return;
        }
        Player killer = Bukkit.getPlayer((String)profile.getLastDamagerName());
        if (killer != null && this.getTeam(killer.getUniqueId()) != null && this.getTeams().contains(this.getTeam(killer.getUniqueId()))) {
            this.kills.put(killer.getUniqueId(), this.kills.get(killer.getUniqueId()) + 1);
        }
        if (this.getKitType().getId().equalsIgnoreCase("bridges")) {
            Location spawn = team.getSpawnLoc();
            Vector oldDirection = spawn.getDirection();
            Block block = spawn.getBlock();
            while (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                if ((block = block.getRelative(BlockFace.DOWN)).getY() > 0) continue;
                block = spawn.getBlock();
                break;
            }
            spawn = block.getLocation();
            spawn.setDirection(oldDirection);
            spawn.add(0.5, 0.0, 0.5);
            player.teleport(spawn);
            player.setNoDamageTicks(20);
            this.getUsedKit().getOrDefault(player.getUniqueId(), Kit.ofDefaultKit(this.getKitType())).apply(player);
            return;
        }
        if (this.getKitType().getId().equalsIgnoreCase("bedfight") && !team.bedBroken) {
            this.respawnTask(player, 3);
            return;
        }
        Map<UUID, Match> playingCache = PotPvP.getInstance().getMatchHandler().getPlayingMatchCache();
        team.markDead(player.getUniqueId());
        playingCache.remove(player.getUniqueId());
        this.postMatchPlayers.put(player.getUniqueId(), new PostMatchPlayer(player, this.kitType.getHealingMethod(), this.totalHits.getOrDefault(player.getUniqueId(), 0), this.longestCombo.getOrDefault(player.getUniqueId(), 0), this.missedPots.getOrDefault(player.getUniqueId(), 0)));
        this.addSpectator(player, null, true);
        this.checkEnded();
    }

    public MatchTeam getTeam(UUID playerUuid) {
        for (MatchTeam team : this.teams) {
            if (!team.isAlive(playerUuid)) continue;
            return team;
        }
        return null;
    }

    public MatchTeam getPreviousTeam(UUID playerUuid) {
        for (MatchTeam team : this.teams) {
            if (!team.getAllMembers().contains(playerUuid)) continue;
            return team;
        }
        return null;
    }

    public String getSimpleDescription(boolean includeRankedUnranked) {
        String players;
        if (this.teams.size() == 2) {
            MatchTeam teamA = this.teams.get(0);
            MatchTeam teamB = this.teams.get(1);
            if (teamA.getAliveMembers().size() == 1 && teamB.getAliveMembers().size() == 1) {
                String nameA = FrozenUUIDCache.name((UUID)teamA.getFirstAliveMember());
                String nameB = FrozenUUIDCache.name((UUID)teamB.getFirstAliveMember());
                players = nameA + " vs " + nameB;
            } else {
                players = teamA.getAliveMembers().size() + " vs " + teamB.getAliveMembers().size();
            }
        } else {
            int numTotalPlayers = 0;
            for (MatchTeam team : this.teams) {
                numTotalPlayers += team.getAliveMembers().size();
            }
            players = numTotalPlayers + " player fight";
        }
        if (includeRankedUnranked) {
            String rankedStr = this.queueType.getName();
            return players + " (" + rankedStr + " " + this.kitType.getDisplayName() + ")";
        }
        return players;
    }

    public void messageAll(String message) {
        this.messageAlive(message);
        this.messageSpectators(message);
    }

    public MatchTeam getTeam(int teamId) {
        return this.teams.get(teamId);
    }

    public void playSoundAll(Sound sound, float pitch) {
        this.playSoundAlive(sound, pitch);
        this.playSoundSpectators(sound, pitch);
    }

    public void messageSpectators(String message) {
        for (UUID spectator : this.spectators) {
            Player spectatorBukkit = Bukkit.getPlayer((UUID)spectator);
            if (spectatorBukkit == null) continue;
            spectatorBukkit.sendMessage(message);
        }
    }

    public void playSoundSpectators(Sound sound, float pitch) {
        for (UUID spectator : this.spectators) {
            Player spectatorBukkit = Bukkit.getPlayer((UUID)spectator);
            if (spectatorBukkit == null) continue;
            spectatorBukkit.playSound(spectatorBukkit.getEyeLocation(), sound, 10.0f, pitch);
        }
    }

    public void messageAlive(String message) {
        for (MatchTeam team : this.teams) {
            team.messageAlive(message);
        }
    }

    public void playSoundAlive(Sound sound, float pitch) {
        for (MatchTeam team : this.teams) {
            team.playSoundAlive(sound, pitch);
        }
    }

    public void respawnTask(final Player player, final int secs) {
        if (player.hasMetadata("respawning")) {
            return;
        }
        final int[] done = new int[]{0};
        for (Player p : this.getAllPlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList())) {
            p.hidePlayer(player);
        }
        player.setGameMode(GameMode.CREATIVE);
        player.setMetadata("respawning", (MetadataValue)new FixedMetadataValue((Plugin)PotPvP.getInstance(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                done[0] = done[0] + 1;
                if (done[0] > secs) {
                    for (Player p : Match.this.getAllPlayers().stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList())) {
                        p.showPlayer(player);
                    }
                    player.sendMessage(CC.translate("&aYou have been respawned!"));
                    player.removeMetadata("respawning", (Plugin)PotPvP.getInstance());
                    MatchTeam team = Match.this.getTeam(player.getUniqueId());
                    if (team != null) {
                        player.teleport(team.getSpawnLoc());
                    }
                    player.setGameMode(GameMode.SURVIVAL);
                    if (Match.this.getUsedKit().get(player.getUniqueId()) != null) {
                        Match.this.getUsedKit().get(player.getUniqueId()).apply(player);
                    } else {
                        Kit.ofDefaultKit(Match.this.kitType).apply(player);
                    }
                    this.cancel();
                } else {
                    player.teleport(Match.this.arena.getSpectatorSpawn());
                    player.sendMessage(CC.translate("&aYou are going to be respawned in " + (secs + 1 - done[0]) + "."));
                }
            }
        }.runTaskTimerAsynchronously((Plugin)PotPvP.getInstance(), 10L, 20L);
    }

    public void recordPlacedBlock(Block block) {
        this.placedBlocks.add(block.getLocation().toVector().toBlockVector());
    }

    public boolean canBeBroken(Block block) {
        return this.kitType.getId().equals("SPLEEF") && (block.getType() == Material.SNOW_BLOCK || block.getType() == Material.GRASS || block.getType() == Material.DIRT) || this.placedBlocks.contains(block.getLocation().toVector().toBlockVector());
    }

    public String get_id() {
        return this._id;
    }

    public String getReplayId() {
        return this.replayId;
    }

    public void setReplayId(String replayId) {
        this.replayId = replayId;
    }

    public KitType getKitType() {
        return this.kitType;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Map<UUID, Integer> getLives() {
        return this.lives;
    }

    public List<MatchTeam> getTeams() {
        return this.teams;
    }

    public Map<UUID, Integer> getBoxingHits() {
        return this.boxingHits;
    }

    public Map<MatchTeam, Integer> getHits() {
        return this.hits;
    }

    public MatchTeam getWinner() {
        return this.winner;
    }

    public Map<UUID, Integer> getKills() {
        return this.kills;
    }

    public Map<UUID, Kit> getUsedKit() {
        return this.usedKit;
    }

    public Map<MatchTeam, Integer> getWins() {
        return this.wins;
    }

    public MatchEndReason getEndReason() {
        return this.endReason;
    }

    public MatchState getState() {
        return this.state;
    }

    public Date getStartedAt() {
        return this.startedAt;
    }

    public Date getEndedAt() {
        return this.endedAt;
    }

    public QueueType getQueueType() {
        return this.queueType;
    }

    public boolean isAllowRematches() {
        return this.allowRematches;
    }

    public EloCalculator.Result getEloChange() {
        return this.eloChange;
    }

    public void setEloChange(EloCalculator.Result eloChange) {
        this.eloChange = eloChange;
    }

    public Map<UUID, UUID> getLastHit() {
        return this.lastHit;
    }

    public Map<UUID, Integer> getCombos() {
        return this.combos;
    }

    public Map<UUID, Integer> getTotalHits() {
        return this.totalHits;
    }

    public Map<UUID, Integer> getLongestCombo() {
        return this.longestCombo;
    }

    public Map<UUID, Integer> getMissedPots() {
        return this.missedPots;
    }

    public List<ReplayableAction> getReplayableActions() {
        return this.replayableActions;
    }

    public Set<UUID> getAllPlayers() {
        return this.allPlayers;
    }

    public Set<UUID> getDisconnectedPlayers() {
        return this.disconnectedPlayers;
    }

    public Set<UUID> getWinningPlayers() {
        return this.winningPlayers;
    }

    public Set<UUID> getLosingPlayers() {
        return this.losingPlayers;
    }
}

