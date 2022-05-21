/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  rip.bridge.qlib.uuid.FrozenUUIDCache
 */
package cc.fyre.potpvp.match;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchState;
import cc.fyre.potpvp.match.MatchTeam;
import cc.fyre.potpvp.match.listener.GoldenHeadListener;
import cc.fyre.potpvp.match.listener.KitSelectionListener;
import cc.fyre.potpvp.match.listener.MatchBedFightListener;
import cc.fyre.potpvp.match.listener.MatchBlockPickupListener;
import cc.fyre.potpvp.match.listener.MatchBoxingListener;
import cc.fyre.potpvp.match.listener.MatchBridgeListener;
import cc.fyre.potpvp.match.listener.MatchBuildListener;
import cc.fyre.potpvp.match.listener.MatchComboListener;
import cc.fyre.potpvp.match.listener.MatchCountdownListener;
import cc.fyre.potpvp.match.listener.MatchDeathMessageListener;
import cc.fyre.potpvp.match.listener.MatchDurationLimitListener;
import cc.fyre.potpvp.match.listener.MatchEnderPearlDamageListener;
import cc.fyre.potpvp.match.listener.MatchFreezeListener;
import cc.fyre.potpvp.match.listener.MatchGeneralListener;
import cc.fyre.potpvp.match.listener.MatchHardcoreHealingListener;
import cc.fyre.potpvp.match.listener.MatchHealthDisplayListener;
import cc.fyre.potpvp.match.listener.MatchMLGListener;
import cc.fyre.potpvp.match.listener.MatchPartySpectateListener;
import cc.fyre.potpvp.match.listener.MatchRodListener;
import cc.fyre.potpvp.match.listener.MatchSoupListener;
import cc.fyre.potpvp.match.listener.MatchStatsListener;
import cc.fyre.potpvp.match.listener.MatchWizardListener;
import cc.fyre.potpvp.match.listener.SpectatorItemListener;
import cc.fyre.potpvp.match.listener.SpectatorPreventionListener;
import cc.fyre.potpvp.queue.QueueType;
import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import rip.bridge.qlib.uuid.FrozenUUIDCache;

public final class MatchHandler {
    public static final String MONGO_COLLECTION_NAME = "matches";
    private final Set<Match> hostedMatches = Collections.newSetFromMap(new ConcurrentHashMap());
    private boolean rankedMatchesDisabled;
    private boolean premiumMatchesDisabled;
    private boolean unrankedMatchesDisabled;
    private final Map<UUID, Match> playingMatchCache = new ConcurrentHashMap<UUID, Match>();
    private final Map<UUID, Match> spectatingMatchCache = new ConcurrentHashMap<UUID, Match>();

    public MatchHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)new GoldenHeadListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new KitSelectionListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchBedFightListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchBlockPickupListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchBuildListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchComboListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchCountdownListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchDeathMessageListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchDurationLimitListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchBoxingListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchBridgeListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchEnderPearlDamageListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchFreezeListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchGeneralListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchHardcoreHealingListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchMLGListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchHealthDisplayListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchPartySpectateListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchRodListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchSoupListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchStatsListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new MatchWizardListener(), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new SpectatorItemListener(this), (Plugin)PotPvP.getInstance());
        Bukkit.getPluginManager().registerEvents((Listener)new SpectatorPreventionListener(), (Plugin)PotPvP.getInstance());
    }

    public Match startMatch(List<MatchTeam> teams, KitType kitType, ArenaSchematic arenaSchematic, QueueType queueType, boolean allowRematches) {
        boolean anyOps = false;
        for (MatchTeam team : teams) {
            for (UUID member : team.getAllMembers()) {
                Player memberPlayer = Bukkit.getPlayer((UUID)member);
                if (!anyOps && memberPlayer.isOp()) {
                    anyOps = true;
                }
                if (!this.isPlayingOrSpectatingMatch(memberPlayer)) continue;
                throw new IllegalArgumentException(FrozenUUIDCache.name((UUID)member) + " is already in a match!");
            }
        }
        if (!anyOps) {
            if (queueType.isRanked() && this.rankedMatchesDisabled) {
                throw new IllegalArgumentException("Ranked match creation is disabled!");
            }
            if (queueType.isPremium() && this.premiumMatchesDisabled) {
                throw new IllegalArgumentException("Premium match creation is disabled!");
            }
            if (this.unrankedMatchesDisabled) {
                throw new IllegalArgumentException("Unranked match creation is disabled!");
            }
        }
        long matchSize = teams.stream().mapToInt(t -> t.getAllMembers().size()).sum();
        ArenaHandler arenaHandler = PotPvP.getInstance().getArenaHandler();
        Optional<Arena> openArenaOpt = arenaSchematic == null ? arenaHandler.allocateUnusedArena(schematic -> schematic.isEnabled() /*&& schematic.getEvent() == null*/ && !schematic.isTeamFightsOnly() && MatchHandler.canUseSchematic(kitType, schematic) && matchSize <= (long)schematic.getMaxPlayerCount() && matchSize >= (long)schematic.getMinPlayerCount() && (!queueType.isRanked() && !queueType.isPremium() || schematic.isSupportsRanked()) && (kitType.getId().equals("ARCHER") || !schematic.isArcherOnly())) : arenaHandler.allocateUnusedArena(schematic -> schematic.equals(arenaSchematic) && schematic.isEnabled() /*&& schematic.getEvent() == null*/ && !schematic.isTeamFightsOnly() && MatchHandler.canUseSchematic(kitType, schematic) && matchSize <= (long)schematic.getMaxPlayerCount() && matchSize >= (long)schematic.getMinPlayerCount() && (!queueType.isRanked() && !queueType.isPremium() || schematic.isSupportsRanked()) && (kitType.getId().equals("ARCHER") || !schematic.isArcherOnly()));
        if (kitType.equals(KitType.teamFight)) {
            openArenaOpt = arenaHandler.allocateUnusedArena(schematic -> schematic.isEnabled() /*&& schematic.getEvent() == null*/ && MatchHandler.canUseSchematic(kitType, schematic) && matchSize <= (long)schematic.getMaxPlayerCount() && matchSize >= (long)schematic.getMinPlayerCount() && schematic.isTeamFightsOnly());
        }
        if (!openArenaOpt.isPresent()) {
            PotPvP.getInstance().getLogger().warning("Failed to start match: No open arenas found");
            return null;
        }
        Match match = new Match(kitType, openArenaOpt.get(), teams, queueType, allowRematches);
        this.hostedMatches.add(match);
        match.startCountdown(false);
        return match;
    }

    public static boolean canUseSchematic(KitType kitType, ArenaSchematic schematic) {
        String kitId = kitType.getId();
        /*if (schematic.getEvent() != null) {
            return false;
        }*/
        if (kitId.equals("ARCHER")) {
            return schematic.isArcherOnly();
        }
        if (kitId.equals("BUILDUHC")) {
            return schematic.isBuildUHCOnly();
        }
        if (kitId.equals("SPLEEF")) {
            return schematic.isSpleefOnly();
        }
        if (kitId.equalsIgnoreCase("BRIDGES")) {
            return schematic.isBridgesOnly();
        }
        if (kitId.equalsIgnoreCase("MLGRUSH")) {
            return schematic.isMlgOnly();
        }
        if (kitId.equalsIgnoreCase("BEDFIGHT")) {
            return schematic.isBedFightsOnly();
        }
        if (kitId.equals("SUMO")) {
            return schematic.isSumoOnly();
        }
        if (kitId.equals("HCF")) {
            return schematic.isHCFOnly();
        }
        if (kitType.equals(KitType.teamFight)) {
            return schematic.isTeamFightsOnly();
        }
        if (schematic.isArcherOnly()) {
            return kitId.equalsIgnoreCase("ARCHER");
        }
        if (schematic.isBuildUHCOnly()) {
            return kitId.equalsIgnoreCase("BUILDUHC");
        }
        if (schematic.isSpleefOnly()) {
            return kitId.equalsIgnoreCase("SPLEEF");
        }
        if (schematic.isMlgOnly()) {
            return kitId.equalsIgnoreCase("MLGRUSH");
        }
        if (schematic.isBridgesOnly()) {
            return kitId.equalsIgnoreCase("BRIDGES");
        }
        if (schematic.isBedFightsOnly()) {
            return kitId.equalsIgnoreCase("BEDFIGHT");
        }
        if (schematic.isSumoOnly()) {
            return kitId.equalsIgnoreCase("SUMO");
        }
        if (schematic.isHCFOnly()) {
            return kitId.equalsIgnoreCase("HCF");
        }
        return true;
    }

    void removeMatch(Match match) {
        this.hostedMatches.remove(match);
    }

    public Set<Match> getHostedMatches() {
        return ImmutableSet.copyOf(this.hostedMatches);
    }

    public int countPlayersPlayingInProgressMatches() {
        return this.countPlayersPlayingMatches(m -> m.getState() == MatchState.COUNTDOWN || m.getState() == MatchState.IN_PROGRESS);
    }

    public int countPlayersPlayingMatches(Predicate<Match> inclusionPredicate) {
        int result2 = 0;
        for (Match match : this.hostedMatches) {
            if (!inclusionPredicate.test(match)) continue;
            for (MatchTeam team : match.getTeams()) {
                result2 += team.getAliveMembers().size();
            }
        }
        return result2;
    }

    public Match getMatchPlaying(Player player) {
        return this.playingMatchCache.get(player.getUniqueId());
    }

    public Match getMatchSpectating(Player player) {
        return this.spectatingMatchCache.get(player.getUniqueId());
    }

    public Match getMatchPlayingOrSpectating(Player player) {
        Match playing = this.playingMatchCache.get(player.getUniqueId());
        if (playing != null) {
            return playing;
        }
        return this.spectatingMatchCache.get(player.getUniqueId());
    }

    public boolean isPlayingMatch(Player player) {
        return this.playingMatchCache.containsKey(player.getUniqueId());
    }

    public boolean isSpectatingMatch(Player player) {
        return this.spectatingMatchCache.containsKey(player.getUniqueId());
    }

    public boolean isPlayingOrSpectatingMatch(Player player) {
        if (player == null) {
            return false;
        }
        return this.playingMatchCache.containsKey(player.getUniqueId()) || this.spectatingMatchCache.containsKey(player.getUniqueId());
    }

    public boolean isRankedMatchesDisabled() {
        return this.rankedMatchesDisabled;
    }

    public void setRankedMatchesDisabled(boolean rankedMatchesDisabled) {
        this.rankedMatchesDisabled = rankedMatchesDisabled;
    }

    public boolean isPremiumMatchesDisabled() {
        return this.premiumMatchesDisabled;
    }

    public void setPremiumMatchesDisabled(boolean premiumMatchesDisabled) {
        this.premiumMatchesDisabled = premiumMatchesDisabled;
    }

    public boolean isUnrankedMatchesDisabled() {
        return this.unrankedMatchesDisabled;
    }

    public void setUnrankedMatchesDisabled(boolean unrankedMatchesDisabled) {
        this.unrankedMatchesDisabled = unrankedMatchesDisabled;
    }

    Map<UUID, Match> getPlayingMatchCache() {
        return this.playingMatchCache;
    }

    Map<UUID, Match> getSpectatingMatchCache() {
        return this.spectatingMatchCache;
    }
}

