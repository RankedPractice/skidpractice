/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  mkremins.fanciful.FancyMessage
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package cc.fyre.potpvp.game;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.Arena;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.bukkit.event.GameStateChangeEvent;
import cc.fyre.potpvp.game.bukkit.event.PlayerJoinGameEvent;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.parameter.GameParameterOption;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import cc.fyre.potpvp.util.PatchedPlayerUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 g2\u00020\u0001:\u0001gB+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020\u0005J\u000e\u0010S\u001a\u00020Q2\u0006\u0010R\u001a\u00020\u0005J\u0006\u0010T\u001a\u00020QJ\u0006\u0010U\u001a\u00020VJ\u0011\u0010W\u001a\b\u0012\u0004\u0012\u00020Y0X\u00a2\u0006\u0002\u0010ZJ\u001c\u0010[\u001a\u0004\u0018\u00010\b\"\u0004\b\u0000\u0010\\2\f\u0010]\u001a\b\u0012\u0004\u0012\u0002H\\0^J\u0011\u0010_\u001a\b\u0012\u0004\u0012\u00020Y0X\u00a2\u0006\u0002\u0010ZJ\u000e\u0010`\u001a\u00020Q2\u0006\u0010R\u001a\u00020\u0005J\u0010\u0010a\u001a\u00020Q2\u0006\u0010R\u001a\u00020\u0005H\u0002J\u001f\u0010b\u001a\u00020Q2\u0012\u0010c\u001a\n\u0012\u0006\b\u0001\u0012\u00020V0X\"\u00020V\u00a2\u0006\u0002\u0010dJ\u000e\u0010e\u001a\u00020Q2\u0006\u0010f\u001a\u00020%R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R!\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u00180\u0017j\b\u0012\u0004\u0012\u00020\u0018`\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010$\u001a\u00020%X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b&\u0010'\"\u0004\b(\u0010)R*\u0010*\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0017j\b\u0012\u0004\u0012\u00020\u0005`\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001b\"\u0004\b,\u0010-R!\u0010.\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0017j\b\u0012\u0004\u0012\u00020\u0005`\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001bR\u001a\u00100\u001a\u000201X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u001a\u00106\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u0010!\"\u0004\b8\u0010#R\u001a\u00109\u001a\u00020:X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R&\u0010?\u001a\u000e\u0012\u0004\u0012\u00020A\u0012\u0004\u0012\u00020B0@X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR&\u0010G\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020A0@X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bH\u0010D\"\u0004\bI\u0010FR\u001c\u0010J\u001a\u0004\u0018\u00010KX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bL\u0010M\"\u0004\bN\u0010O\u00a8\u0006h"}, d2={"Lcc/fyre/potpvp/game/Game;", "", "event", "Lcc/fyre/potpvp/game/event/GameEvent;", "host", "Lorg/bukkit/entity/Player;", "parameters", "", "Lcc/fyre/potpvp/game/parameter/GameParameterOption;", "maxPlayers", "", "(Lcc/fyre/potpvp/game/event/GameEvent;Lorg/bukkit/entity/Player;Ljava/util/List;I)V", "arena", "Lcc/fyre/potpvp/arena/Arena;", "getArena", "()Lcc/fyre/potpvp/arena/Arena;", "setArena", "(Lcc/fyre/potpvp/arena/Arena;)V", "getEvent", "()Lcc/fyre/potpvp/game/event/GameEvent;", "getHost", "()Lorg/bukkit/entity/Player;", "joinedSent", "Ljava/util/HashSet;", "Ljava/util/UUID;", "Lkotlin/collections/HashSet;", "getJoinedSent", "()Ljava/util/HashSet;", "logic", "Lcc/fyre/potpvp/game/event/GameEventLogic;", "getLogic", "()Lcc/fyre/potpvp/game/event/GameEventLogic;", "getMaxPlayers", "()I", "setMaxPlayers", "(I)V", "pendingEnd", "", "getPendingEnd", "()Z", "setPendingEnd", "(Z)V", "players", "getPlayers", "setPlayers", "(Ljava/util/HashSet;)V", "spectators", "getSpectators", "startingAt", "", "getStartingAt", "()J", "setStartingAt", "(J)V", "startingParticipants", "getStartingParticipants", "setStartingParticipants", "state", "Lcc/fyre/potpvp/game/GameState;", "getState", "()Lcc/fyre/potpvp/game/GameState;", "setState", "(Lcc/fyre/potpvp/game/GameState;)V", "voteOptions", "", "Lcc/fyre/potpvp/arena/ArenaSchematic;", "Ljava/util/concurrent/atomic/AtomicInteger;", "getVoteOptions", "()Ljava/util/Map;", "setVoteOptions", "(Ljava/util/Map;)V", "votes", "getVotes", "setVotes", "winner", "Lcc/fyre/potpvp/game/util/team/GameTeam;", "getWinner", "()Lcc/fyre/potpvp/game/util/team/GameTeam;", "setWinner", "(Lcc/fyre/potpvp/game/util/team/GameTeam;)V", "add", "", "player", "addSpectator", "end", "getEventDisplayName", "", "getFirstSpawnLocations", "", "Lorg/bukkit/Location;", "()[Lorg/bukkit/Location;", "getParameter", "T", "clazz", "Ljava/lang/Class;", "getSecondSpawnLocations", "reset", "resetSpectator", "sendMessage", "messages", "([Ljava/lang/String;)V", "start", "forced", "Companion", "potpvp-si"})
public final class Game {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final GameEvent event;
    @NotNull
    private final Player host;
    @NotNull
    private final List<GameParameterOption> parameters;
    private int maxPlayers;
    @NotNull
    private GameState state;
    private long startingAt;
    private int startingParticipants;
    private boolean pendingEnd;
    @Nullable
    private GameTeam winner;
    @NotNull
    private HashSet<Player> players;
    @NotNull
    private final GameEventLogic logic;
    @NotNull
    private final HashSet<Player> spectators;
    @NotNull
    private Map<ArenaSchematic, AtomicInteger> voteOptions;
    @NotNull
    private Map<UUID, ArenaSchematic> votes;
    @NotNull
    private Arena arena;
    @NotNull
    private final HashSet<UUID> joinedSent;
    @NotNull
    private static final Arena LATE_INIT_ARENA = new Arena();

    public Game(@NotNull GameEvent event, @NotNull Player host, @NotNull List<? extends GameParameterOption> parameters2, int maxPlayers) {
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(parameters2, "parameters");
        this.event = event;
        this.host = host;
        this.parameters = parameters2;
        this.maxPlayers = maxPlayers;
        this.state = GameState.STARTING;
        this.players = new HashSet();
        this.logic = this.event.getLogic(this);
        this.spectators = new HashSet();
        boolean bl = false;
        this.voteOptions = new HashMap();
        bl = false;
        this.votes = new HashMap();
        this.arena = LATE_INIT_ARENA;
        bl = false;
        this.joinedSent = new HashSet();
    }

    @NotNull
    public final GameEvent getEvent() {
        return this.event;
    }

    @NotNull
    public final Player getHost() {
        return this.host;
    }

    public final int getMaxPlayers() {
        return this.maxPlayers;
    }

    public final void setMaxPlayers(int n) {
        this.maxPlayers = n;
    }

    @NotNull
    public final GameState getState() {
        return this.state;
    }

    public final void setState(@NotNull GameState gameState) {
        Intrinsics.checkNotNullParameter((Object)gameState, "<set-?>");
        this.state = gameState;
    }

    public final long getStartingAt() {
        return this.startingAt;
    }

    public final void setStartingAt(long l) {
        this.startingAt = l;
    }

    public final int getStartingParticipants() {
        return this.startingParticipants;
    }

    public final void setStartingParticipants(int n) {
        this.startingParticipants = n;
    }

    public final boolean getPendingEnd() {
        return this.pendingEnd;
    }

    public final void setPendingEnd(boolean bl) {
        this.pendingEnd = bl;
    }

    @Nullable
    public final GameTeam getWinner() {
        return this.winner;
    }

    public final void setWinner(@Nullable GameTeam gameTeam) {
        this.winner = gameTeam;
    }

    @NotNull
    public final HashSet<Player> getPlayers() {
        return this.players;
    }

    public final void setPlayers(@NotNull HashSet<Player> hashSet) {
        Intrinsics.checkNotNullParameter(hashSet, "<set-?>");
        this.players = hashSet;
    }

    @NotNull
    public final GameEventLogic getLogic() {
        return this.logic;
    }

    @NotNull
    public final HashSet<Player> getSpectators() {
        return this.spectators;
    }

    @NotNull
    public final Map<ArenaSchematic, AtomicInteger> getVoteOptions() {
        return this.voteOptions;
    }

    public final void setVoteOptions(@NotNull Map<ArenaSchematic, AtomicInteger> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.voteOptions = map;
    }

    @NotNull
    public final Map<UUID, ArenaSchematic> getVotes() {
        return this.votes;
    }

    public final void setVotes(@NotNull Map<UUID, ArenaSchematic> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.votes = map;
    }

    @NotNull
    public final Arena getArena() {
        return this.arena;
    }

    public final void setArena(@NotNull Arena arena) {
        Intrinsics.checkNotNullParameter(arena, "<set-?>");
        this.arena = arena;
    }

    @NotNull
    public final HashSet<UUID> getJoinedSent() {
        return this.joinedSent;
    }

    @NotNull
    public final String getEventDisplayName() {
        String teamSize = this.parameters.contains(GameTeamSizeParameter.Duos.INSTANCE) ? " Duos" : "";
        return Intrinsics.stringPlus(this.event.getName(), teamSize);
    }

    public final void addSpectator(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (this.state == GameState.ENDED) {
            return;
        }
        this.spectators.add(player);
        if (!this.players.contains(player)) {
            this.players.add(player);
            player.sendMessage(ChatColor.GREEN + "You are now spectating the event.");
        }
        this.reset(player);
        player.teleport(this.arena.getSpectatorSpawn().clone().add(0.0, -1.0, 0.0));
        Bukkit.getPluginManager().callEvent((Event)new PlayerJoinGameEvent(player, this));
    }

    public final void add(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (this.state != GameState.STARTING) {
            return;
        }
        this.players.add(player);
        if (!this.joinedSent.contains(player.getUniqueId())) {
            this.joinedSent.add(player.getUniqueId());
            String[] stringArray = new String[]{PatchedPlayerUtils.getFormattedName(player.getUniqueId()) + ChatColor.GRAY + " has joined the " + ChatColor.GOLD + "event" + ChatColor.GRAY + ". (" + this.players.size() + '/' + this.maxPlayers + ')'};
            this.sendMessage(stringArray);
        }
        if (this.getParameter(GameTeamSizeParameter.Duos.class) != null) {
            player.sendMessage("");
            new FancyMessage("You've joined a ").color(ChatColor.GRAY).then("team event").color(ChatColor.GOLD).then(". Use the ").color(ChatColor.GRAY).then("/partner").color(ChatColor.GOLD).suggest("/partner").then(" command to team-up with another player.").color(ChatColor.GRAY).send(player);
            player.sendMessage("");
        }
        this.reset(player);
        Bukkit.getPluginManager().callEvent((Event)new PlayerJoinGameEvent(player, this));
    }

    private final void resetSpectator(Player player) {
        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(0);
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.CREATIVE);
        Collection $this$toTypedArray$iv = this.event.getLobbyItems(this);
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        ItemStack[] itemStackArray = thisCollection$iv.toArray(new ItemStack[0]);
        if (itemStackArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        ItemStack[] itemStackArray2 = itemStackArray;
        player.getInventory().addItem(Arrays.copyOf(itemStackArray2, itemStackArray2.length));
        player.getInventory().setItem(8, GameEvent.Companion.getLeaveItem());
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
        player.updateInventory();
    }

    public final void reset(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (this.spectators.contains(player)) {
            this.resetSpectator(player);
            return;
        }
        if (this.arena == LATE_INIT_ARENA) {
            player.teleport(PotPvP.getInstance().gameHandler.getLobbyLocation());
        } else {
            player.teleport(this.arena.getSpectatorSpawn().clone().add(0.0, -1.0, 0.0));
        }
        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(0);
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.SURVIVAL);
        Collection $this$toTypedArray$iv = this.event.getLobbyItems(this);
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        ItemStack[] itemStackArray = thisCollection$iv.toArray(new ItemStack[0]);
        if (itemStackArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        ItemStack[] itemStackArray2 = itemStackArray;
        player.getInventory().addItem(Arrays.copyOf(itemStackArray2, itemStackArray2.length));
        player.getInventory().setItem(8, GameEvent.Companion.getLeaveItem());
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.updateInventory();
    }

    public final void start(boolean forced) {
        this.startingParticipants = this.players.size();
        if (!this.event.canStart(this)) {
            Iterable $this$forEach$iv = this.players;
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                Player it = (Player)element$iv;
                boolean bl = false;
                it.sendMessage(ChatColor.RED + "There weren't enough players to start the event.");
            }
            this.end();
            return;
        }
        String format = forced ? ChatColor.GRAY + "The " + ChatColor.GOLD + "event " + ChatColor.GRAY + "has been forcefully started." : ChatColor.GRAY + "The " + ChatColor.GOLD + "event " + ChatColor.GRAY + "has started.";
        String[] stringArray = new String[1];
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objectArray = new Object[]{this.getEventDisplayName(), this.players.size(), this.maxPlayers};
        boolean bl = false;
        String string = String.format(format, Arrays.copyOf(objectArray, objectArray.length));
        Intrinsics.checkNotNullExpressionValue(string, "java.lang.String.format(format, *args)");
        String string2 = ChatColor.translateAlternateColorCodes((char)'&', (String)string);
        Intrinsics.checkNotNullExpressionValue(string2, "translateAlternateColorC\u2026layers.size, maxPlayers))");
        stringArray[0] = string2;
        this.sendMessage(stringArray);
        this.arena.takeSnapshot();
        this.logic.start();
        Bukkit.getPluginManager().callEvent((Event)new GameStateChangeEvent(this, GameState.RUNNING));
    }

    public final void end() {
        for (Player player : this.players) {
            ((CraftPlayer)player).setKbProfile(null);
        }
        this.arena.restore();
        Bukkit.getPluginManager().callEvent((Event)new GameStateChangeEvent(this, GameState.ENDED));
    }

    @NotNull
    public final Location[] getFirstSpawnLocations() {
        Location[] locationArray;
        if (this.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass()) != null) {
            Vector direction = this.arena.getTeam1Spawn().getDirection();
            Location[] locationArray2 = new Location[2];
            Location location = this.arena.getTeam1Spawn().clone().add(direction.clone().setX(-direction.getZ()).setZ(direction.getX()));
            Intrinsics.checkNotNullExpressionValue(location, "arena.team1Spawn.clone()\u2026ion.z).setZ(direction.x))");
            locationArray2[0] = location;
            location = this.arena.getTeam1Spawn().clone().add(direction.clone().setX(direction.getZ()).setZ(-direction.getX()));
            Intrinsics.checkNotNullExpressionValue(location, "arena.team1Spawn.clone()\u2026on.z).setZ(-direction.x))");
            locationArray2[1] = location;
            locationArray = locationArray2;
        } else {
            Location[] locationArray3 = new Location[1];
            Location location = this.arena.getTeam1Spawn();
            Intrinsics.checkNotNullExpressionValue(location, "arena.team1Spawn");
            locationArray3[0] = location;
            locationArray = locationArray3;
        }
        return locationArray;
    }

    @NotNull
    public final Location[] getSecondSpawnLocations() {
        Location[] locationArray;
        if (this.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass()) != null) {
            Vector direction = this.arena.getTeam2Spawn().getDirection();
            Location[] locationArray2 = new Location[2];
            Location location = this.arena.getTeam2Spawn().clone().add(direction.clone().setX(-direction.getZ()).setZ(direction.getX()));
            Intrinsics.checkNotNullExpressionValue(location, "arena.team2Spawn.clone()\u2026ion.z).setZ(direction.x))");
            locationArray2[0] = location;
            location = this.arena.getTeam2Spawn().clone().add(direction.clone().setX(direction.getZ()).setZ(-direction.getX()));
            Intrinsics.checkNotNullExpressionValue(location, "arena.team2Spawn.clone()\u2026on.z).setZ(-direction.x))");
            locationArray2[1] = location;
            locationArray = locationArray2;
        } else {
            Location[] locationArray3 = new Location[1];
            Location location = this.arena.getTeam2Spawn();
            Intrinsics.checkNotNullExpressionValue(location, "arena.team2Spawn");
            locationArray3[0] = location;
            locationArray = locationArray3;
        }
        return locationArray;
    }

    public final void sendMessage(String ... messages) {
        Intrinsics.checkNotNullParameter(messages, "messages");
        for (Player player : this.players) {
            for (String msg : messages) {
                player.sendMessage(ChatColor.GRAY + '[' + ChatColor.YELLOW + this.event.getName() + "Event" + ChatColor.GRAY + "] " + ChatColor.RESET + msg);
            }
        }
    }

    @Nullable
    public final <T> GameParameterOption getParameter(@NotNull Class<T> clazz) {
        Intrinsics.checkNotNullParameter(clazz, "clazz");
        for (GameParameterOption parameter : this.parameters) {
            if (!Intrinsics.areEqual(parameter.getClass(), clazz) && !clazz.isAssignableFrom(parameter.getClass())) continue;
            T t = clazz.cast(parameter);
            if (t == null) {
                throw new NullPointerException("null cannot be cast to non-null type cc.fyre.potpvp.game.parameter.GameParameterOption");
            }
            return (GameParameterOption)t;
        }
        return null;
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lcc/fyre/potpvp/game/Game$Companion;", "", "()V", "LATE_INIT_ARENA", "Lcc/fyre/potpvp/arena/Arena;", "getLATE_INIT_ARENA", "()Lcc/fyre/potpvp/arena/Arena;", "potpvp-si"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Arena getLATE_INIT_ARENA() {
            return LATE_INIT_ARENA;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

