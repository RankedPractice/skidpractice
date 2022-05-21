/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.spigotmc.SpigotConfig
 *  rip.bridge.knockback.Knockback
 *  rip.bridge.qlib.util.ItemBuilder
 *  rip.bridge.qlib.util.TimeUtils
 */
package cc.fyre.potpvp.game.event.impl.sumo;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.arena.ArenaSchematic;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.event.GameEventItems;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEvent;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEventListeners;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEventLogic;
import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import cc.fyre.potpvp.util.CC;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.SpigotConfig;
import rip.bridge.knockback.Knockback;
import rip.bridge.qlib.util.ItemBuilder;
import rip.bridge.qlib.util.TimeUtils;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\n\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0016J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00120\u00162\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u001b\u001a\u00020\u0004H\u0016J \u0010\u001c\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0016J\u000e\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u0016H\u0016J\b\u0010\"\u001a\u00020\u0004H\u0016J\u001e\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00040\u00162\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u000e\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006'"}, d2={"Lcc/fyre/potpvp/game/event/impl/sumo/SumoGameEvent;", "Lcc/fyre/potpvp/game/event/GameEvent;", "()V", "DESCRIPTION", "", "NAME", "PERMISSION", "knockbackName", "getKnockbackName", "()Ljava/lang/String;", "setKnockbackName", "(Ljava/lang/String;)V", "canStart", "", "game", "Lcc/fyre/potpvp/game/Game;", "getDescription", "getIcon", "Lorg/bukkit/inventory/ItemStack;", "getKnockback", "Lrip/bridge/knockback/Knockback;", "getListeners", "", "Lorg/bukkit/event/Listener;", "getLobbyItems", "getLogic", "Lcc/fyre/potpvp/game/event/GameEventLogic;", "getName", "getNameTag", "player", "Lorg/bukkit/entity/Player;", "viewer", "getParameters", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "getPermission", "getScoreboardScores", "setAndSaveKbProfile", "", "knockback", "potpvp-si"})
public final class SumoGameEvent
implements GameEvent {
    @NotNull
    public static final SumoGameEvent INSTANCE = new SumoGameEvent();
    @NotNull
    private static String knockbackName = "Default";
    @NotNull
    private static final String NAME = "Sumo";
    @NotNull
    private static final String PERMISSION = "potpvp.event.host.sumo";
    @NotNull
    private static final String DESCRIPTION = "Knock your opponent off the platform.";

    private SumoGameEvent() {
    }

    @NotNull
    public final String getKnockbackName() {
        return knockbackName;
    }

    public final void setKnockbackName(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        knockbackName = string;
    }

    public final void setAndSaveKbProfile(@NotNull Knockback knockback) {
        Intrinsics.checkNotNullParameter(knockback, "knockback");
        String string = knockback.getName();
        Intrinsics.checkNotNullExpressionValue(string, "knockback.name");
        knockbackName = string;
        PotPvP.getInstance().mongoDatabase.getCollection("eventMetadata").updateOne(new Document("_id", "sumo_metadata"), (Bson)new Document("$set", new Document("knockbackName", knockback.getName())), new UpdateOptions().upsert(true));
    }

    @Override
    @NotNull
    public String getName() {
        return NAME;
    }

    @Override
    @NotNull
    public String getPermission() {
        return PERMISSION;
    }

    @Override
    @NotNull
    public String getDescription() {
        String string = CC.translate(DESCRIPTION);
        Intrinsics.checkNotNullExpressionValue(string, "translate(DESCRIPTION)");
        return string;
    }

    @Override
    @NotNull
    public ItemStack getIcon() {
        return new ItemStack(Material.LEASH);
    }

    @Override
    public boolean canStart(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        if (game.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass()) != null) {
            return game.getPlayers().size() >= 4;
        }
        return game.getPlayers().size() >= 2;
    }

    @Override
    @NotNull
    public GameEventLogic getLogic(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        return new SumoGameEventLogic(game);
    }

    @Override
    @NotNull
    public List<String> getScoreboardScores(@NotNull Player player, @NotNull Game game) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(game, "game");
        ArrayList<String> toReturn = new ArrayList<String>();
        SumoGameEventLogic logic = (SumoGameEventLogic)game.getLogic();
        boolean starting = game.getState() == GameState.STARTING;
        boolean running = game.getState() == GameState.RUNNING;
        boolean ending = game.getPendingEnd();
        toReturn.add("&b&lEvent &7(" + game.getEventDisplayName() + ')');
        if (ending) {
            if (game.getWinner() != null) {
                GameTeam gameTeam = game.getWinner();
                Intrinsics.checkNotNull(gameTeam);
                toReturn.add(Intrinsics.stringPlus("&bWinner: &r", gameTeam.getName(false)));
            } else {
                toReturn.add("&bGame over, GG!");
            }
        }
        if (!ending && (starting || running)) {
            String participantType;
            String string = participantType = game.getParameter(GameTeamSizeParameter.Duos.class) != null ? "Teams" : "Players";
            if (starting) {
                toReturn.add("  " + participantType + ": &b" + logic.getPlayersLeft() + '/' + game.getMaxPlayers());
            } else {
                toReturn.add("  " + participantType + ": &b" + logic.getPlayersLeft() + '/' + game.getStartingParticipants());
            }
            if (starting) {
                toReturn.add(Intrinsics.stringPlus("  Starting in: &b", TimeUtils.formatLongIntoMMSS((long)TimeUnit.SECONDS.convert(game.getStartingAt() - System.currentTimeMillis(), TimeUnit.MILLISECONDS))));
                toReturn.add("");
                toReturn.add(ChatColor.AQUA + ChatColor.BOLD + "Map Votes");
                Object object = game.getVoteOptions();
                boolean bl = false;
                Iterator<Map.Entry<ArenaSchematic, AtomicInteger>> iterator2 = object.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Object object2 = object = iterator2.next();
                    boolean bl2 = false;
                    ArenaSchematic k = (ArenaSchematic)object2.getKey();
                    Object object3 = object;
                    boolean bl3 = false;
                    AtomicInteger v = (AtomicInteger)object3.getValue();
                    toReturn.add("  " + k.name + ": " + ChatColor.GOLD + v);
                }
            }
            if (running) {
                GameTeam fighter;
                GameTeam opponent;
                toReturn.add(Intrinsics.stringPlus("  Round: &b", logic.getRoundsPlayed() + 1));
                if (game.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass()) == null && (opponent = logic.getNextParticipant(fighter = logic.getNextParticipant(null))) != null && fighter != null) {
                    toReturn.add("&a&r&7&m--------------------");
                    if (System.currentTimeMillis() >= game.getStartingAt()) {
                        toReturn.add(fighter.getName(false) + "&b vs. &r" + opponent.getName(false));
                    } else {
                        toReturn.add("Waiting...");
                    }
                }
            }
        }
        return toReturn;
    }

    @Override
    @NotNull
    public String getNameTag(@NotNull Game game, @NotNull Player player, @NotNull Player viewer) {
        Intrinsics.checkNotNullParameter(game, "game");
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(viewer, "viewer");
        return BracketsGameEvent.INSTANCE.getNameTag(game, player, viewer);
    }

    @Override
    @NotNull
    public List<Listener> getListeners() {
        SumoGameEventListeners[] sumoGameEventListenersArray = new SumoGameEventListeners[]{new SumoGameEventListeners()};
        return CollectionsKt.arrayListOf(sumoGameEventListenersArray);
    }

    @Override
    @NotNull
    public List<GameParameter> getParameters() {
        boolean bl = false;
        return CollectionsKt.emptyList();
    }

    @Override
    @NotNull
    public List<ItemStack> getLobbyItems(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        boolean bl = false;
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
        if (game.getState() == GameState.STARTING) {
            ItemStack voteItem = GameEventItems.getVoteItem();
            Iterable $this$forEach$iv = game.getVoteOptions().keySet();
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                ArenaSchematic it = (ArenaSchematic)element$iv;
                boolean bl2 = false;
                ItemBuilder itemBuilder = ItemBuilder.copyOf((ItemStack)voteItem);
                String string = voteItem.getItemMeta().getDisplayName();
                Intrinsics.checkNotNullExpressionValue(string, "voteItem.itemMeta.displayName");
                String string2 = string;
                string = it.name;
                Intrinsics.checkNotNullExpressionValue(string, "it.name");
                toReturn.add(itemBuilder.name(StringsKt.replace$default(string2, "%MAP%", string, false, 4, null)).build());
            }
        }
        return toReturn;
    }

    @Override
    @Nullable
    public Knockback getKnockback() {
        return SpigotConfig.getKnockbackByName((String)knockbackName);
    }

    static {
        Document document = (Document)PotPvP.getInstance().mongoDatabase.getCollection("eventMetadata").find(new Document("_id", "sumo_metadata")).first();
        if (document != null) {
            String string = document.getString("knockbackName");
            Intrinsics.checkNotNullExpressionValue(string, "document.getString(\"knockbackName\")");
            knockbackName = string;
        }
    }
}

