/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.util.BlockVector
 *  org.bukkit.util.Vector
 *  rip.bridge.ChunkSnapshot
 *  rip.bridge.qlib.command.FrozenCommandHandler
 *  rip.bridge.qlib.command.ParameterType
 *  rip.bridge.qlib.nametag.FrozenNametagHandler
 *  rip.bridge.qlib.nametag.NametagProvider
 *  rip.bridge.qlib.scoreboard.FrozenScoreboardHandler
 *  rip.bridge.qlib.scoreboard.ScoreboardConfiguration
 *  rip.bridge.qlib.serialization.BlockVectorAdapter
 *  rip.bridge.qlib.serialization.ItemStackAdapter
 *  rip.bridge.qlib.serialization.LocationAdapter
 *  rip.bridge.qlib.serialization.PotionEffectAdapter
 *  rip.bridge.qlib.serialization.VectorAdapter
 *  rip.bridge.qlib.tab.FrozenTabHandler
 *  rip.bridge.qlib.tab.LayoutProvider
 */
package cc.fyre.potpvp;

import cc.fyre.potpvp.arena.ArenaHandler;
import cc.fyre.potpvp.duel.DuelHandler;
import cc.fyre.potpvp.elo.EloHandler;
import cc.fyre.potpvp.follow.FollowHandler;
import cc.fyre.potpvp.killeffects.KillEffectHandler;
import cc.fyre.potpvp.kit.KitHandler;
import cc.fyre.potpvp.kittype.KitType;
import cc.fyre.potpvp.kittype.KitTypeJsonAdapter;
import cc.fyre.potpvp.kittype.KitTypeParameterType;
import cc.fyre.potpvp.leaderboard.LeaderboardHandler;
import cc.fyre.potpvp.listener.BasicPreventionListener;
import cc.fyre.potpvp.listener.BowHealthListener;
import cc.fyre.potpvp.listener.ChatFormatListener;
import cc.fyre.potpvp.listener.ChatToggleListener;
import cc.fyre.potpvp.listener.NightModeListener;
import cc.fyre.potpvp.listener.PearlCooldownListener;
import cc.fyre.potpvp.listener.RankedMatchQualificationListener;
import cc.fyre.potpvp.listener.TabCompleteListener;
import cc.fyre.potpvp.listener.TabFixListener;
import cc.fyre.potpvp.listener.ToggleVisibilityListener;
import cc.fyre.potpvp.lobby.LobbyHandler;
import cc.fyre.potpvp.match.Match;
import cc.fyre.potpvp.match.MatchHandler;
import cc.fyre.potpvp.nametag.PotPvPNametagProvider;
import cc.fyre.potpvp.party.PartyHandler;
import cc.fyre.potpvp.postmatchinv.PostMatchInvHandler;
import cc.fyre.potpvp.premium.PremiumMatchesHandler;
import cc.fyre.potpvp.profile.ProfileManager;
import cc.fyre.potpvp.pvpclasses.PvPClassHandler;
import cc.fyre.potpvp.queue.QueueHandler;
import cc.fyre.potpvp.rematch.RematchHandler;
import cc.fyre.potpvp.scoreboard.PotPvPScoreboardConfiguration;
import cc.fyre.potpvp.setting.SettingHandler;
import cc.fyre.potpvp.tab.PotPvPLayoutProvider;
import cc.fyre.potpvp.tournament.TournamentHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import rip.bridge.ChunkSnapshot;
import rip.bridge.qlib.command.FrozenCommandHandler;
import rip.bridge.qlib.command.ParameterType;
import rip.bridge.qlib.nametag.FrozenNametagHandler;
import rip.bridge.qlib.nametag.NametagProvider;
import rip.bridge.qlib.scoreboard.FrozenScoreboardHandler;
import rip.bridge.qlib.scoreboard.ScoreboardConfiguration;
import rip.bridge.qlib.serialization.BlockVectorAdapter;
import rip.bridge.qlib.serialization.ItemStackAdapter;
import rip.bridge.qlib.serialization.LocationAdapter;
import rip.bridge.qlib.serialization.PotionEffectAdapter;
import rip.bridge.qlib.serialization.VectorAdapter;
import rip.bridge.qlib.tab.FrozenTabHandler;
import rip.bridge.qlib.tab.LayoutProvider;

public final class PotPvP extends JavaPlugin {
    private static PotPvP instance;
    public static Gson gson;
    private MongoClient mongoClient;
    public MongoDatabase mongoDatabase;
    private SettingHandler settingHandler;
    private DuelHandler duelHandler;
    private KitHandler kitHandler;
    public LobbyHandler lobbyHandler;
    private ArenaHandler arenaHandler;
    private MatchHandler matchHandler;
    public PartyHandler partyHandler;
    private QueueHandler queueHandler;
    private RematchHandler rematchHandler;
    private PostMatchInvHandler postMatchInvHandler;
    private FollowHandler followHandler;
    private KillEffectHandler killEffectHandler;
    private EloHandler eloHandler;
    private TournamentHandler tournamentHandler;
    private PvPClassHandler pvpClassHandler;
    private ProfileManager profileManager;
    private LeaderboardHandler leaderboardHandler;
    private PremiumMatchesHandler premiumMatchesHandler;
    private ChatColor dominantColor;

    public PotPvP() {
        this.dominantColor = ChatColor.AQUA;
    }

    public void onEnable() {
        (PotPvP.instance = this).saveDefaultConfig();
        this.setupMongo();
        for (final World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6000L);
        }
        this.settingHandler = new SettingHandler();
        this.profileManager = new ProfileManager();
        this.duelHandler = new DuelHandler();
        this.kitHandler = new KitHandler();
        this.lobbyHandler = new LobbyHandler();
        this.arenaHandler = new ArenaHandler();
        this.matchHandler = new MatchHandler();
        this.partyHandler = new PartyHandler();
        this.queueHandler = new QueueHandler();
        this.rematchHandler = new RematchHandler();
        this.postMatchInvHandler = new PostMatchInvHandler();
        this.followHandler = new FollowHandler();
        this.eloHandler = new EloHandler();
        this.tournamentHandler = new TournamentHandler();
        this.pvpClassHandler = new PvPClassHandler();
        this.killEffectHandler = new KillEffectHandler();
        this.premiumMatchesHandler = new PremiumMatchesHandler();
        this.getServer().getPluginManager().registerEvents((Listener)new BasicPreventionListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BowHealthListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ChatFormatListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ChatToggleListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new NightModeListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PearlCooldownListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RankedMatchQualificationListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TabCompleteListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TabCompleteListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TabFixListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ToggleVisibilityListener(), (Plugin)this);
        FrozenCommandHandler.registerAll((Plugin)this);
        FrozenCommandHandler.registerParameterType((Class)KitType.class, (ParameterType)new KitTypeParameterType());
        this.registerPersistence();
        FrozenNametagHandler.registerProvider((NametagProvider)new PotPvPNametagProvider());
        FrozenScoreboardHandler.setConfiguration(PotPvPScoreboardConfiguration.create());
        FrozenTabHandler.setLayoutProvider((LayoutProvider)new PotPvPLayoutProvider());
    }

    public void onDisable() {
        for (final Match match : this.matchHandler.getHostedMatches()) {
            if (match.getKitType().isBuildingAllowed()) {
                match.getArena().restore();
            }
        }
        try {
            this.arenaHandler.saveSchematics();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(this.getServer().getPlayerExact(playerName));
        }
        PotPvP.instance = null;
    }

    private void setupMongo() {
        this.mongoClient = new MongoClient(this.getConfig().getString("Mongo.Host"), this.getConfig().getInt("Mongo.Port"));
        final String databaseId = this.getConfig().getString("Mongo.Database");
        this.mongoDatabase = this.mongoClient.getDatabase(databaseId);
    }

    private void registerPersistence() {
    }

    public ArenaHandler getArenaHandler() {
        return this.arenaHandler;
    }

    public static PotPvP getInstance() {
        return PotPvP.instance;
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    public SettingHandler getSettingHandler() {
        return this.settingHandler;
    }

    public DuelHandler getDuelHandler() {
        return this.duelHandler;
    }

    public KitHandler getKitHandler() {
        return this.kitHandler;
    }

    public LobbyHandler getLobbyHandler() {
        return this.lobbyHandler;
    }

    public MatchHandler getMatchHandler() {
        return this.matchHandler;
    }

    public PartyHandler getPartyHandler() {
        return this.partyHandler;
    }

    public QueueHandler getQueueHandler() {
        return this.queueHandler;
    }

    public RematchHandler getRematchHandler() {
        return this.rematchHandler;
    }

    public PostMatchInvHandler getPostMatchInvHandler() {
        return this.postMatchInvHandler;
    }

    public FollowHandler getFollowHandler() {
        return this.followHandler;
    }

    public KillEffectHandler getKillEffectHandler() {
        return this.killEffectHandler;
    }

    public EloHandler getEloHandler() {
        return this.eloHandler;
    }

    public TournamentHandler getTournamentHandler() {
        return this.tournamentHandler;
    }

    public PvPClassHandler getPvpClassHandler() {
        return this.pvpClassHandler;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public LeaderboardHandler getLeaderboardHandler() {
        return this.leaderboardHandler;
    }

    public PremiumMatchesHandler getPremiumMatchesHandler() {
        return this.premiumMatchesHandler;
    }

    public ChatColor getDominantColor() {
        return this.dominantColor;
    }

    static {
        PotPvP.gson = new GsonBuilder().registerTypeHierarchyAdapter((Class)PotionEffect.class, (Object)new PotionEffectAdapter()).registerTypeHierarchyAdapter((Class)ItemStack.class, (Object)new ItemStackAdapter()).registerTypeHierarchyAdapter((Class)Location.class, (Object)new LocationAdapter()).registerTypeHierarchyAdapter((Class)Vector.class, (Object)new VectorAdapter()).registerTypeAdapter(BlockVector.class, (Object)new BlockVectorAdapter()).registerTypeHierarchyAdapter((Class)KitType.class, (Object)new KitTypeJsonAdapter()).registerTypeAdapter(ChunkSnapshot.class, (Object)new ChunkSnapshotAdapter()).serializeNulls().create();
    }

    private static class ChunkSnapshotAdapter extends TypeAdapter<ChunkSnapshot>
    {
        public ChunkSnapshot read(final JsonReader arg0) {
            return null;
        }

        public void write(final JsonWriter arg0, final ChunkSnapshot arg1) {
        }
    }
}
