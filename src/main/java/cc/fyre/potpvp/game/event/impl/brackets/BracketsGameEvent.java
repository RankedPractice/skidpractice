/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  rip.bridge.knockback.Knockback
 */
package cc.fyre.potpvp.game.event.impl.brackets;

import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.GameState;
import cc.fyre.potpvp.game.event.GameEvent;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEventListeners;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameKitParameter;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEventLogic;
import cc.fyre.potpvp.game.parameter.GameParameter;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import cc.fyre.potpvp.util.CC;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.knockback.Knockback;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\u0004H\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\n\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u000e\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0016J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u00112\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u0016\u001a\u00020\u0004H\u0016J \u0010\u0017\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0016J\u000e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0011H\u0016J\b\u0010\u001d\u001a\u00020\u0004H\u0016J\u001e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00040\u00112\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2={"Lcc/fyre/potpvp/game/event/impl/brackets/BracketsGameEvent;", "Lcc/fyre/potpvp/game/event/GameEvent;", "()V", "DESCRIPTION", "", "NAME", "PERMISSION", "canStart", "", "game", "Lcc/fyre/potpvp/game/Game;", "getDescription", "getIcon", "Lorg/bukkit/inventory/ItemStack;", "getKnockback", "Lrip/bridge/knockback/Knockback;", "getListeners", "", "Lorg/bukkit/event/Listener;", "getLobbyItems", "getLogic", "Lcc/fyre/potpvp/game/event/GameEventLogic;", "getName", "getNameTag", "player", "Lorg/bukkit/entity/Player;", "viewer", "getParameters", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "getPermission", "getScoreboardScores", "potpvp-si"})
public final class BracketsGameEvent
implements GameEvent {
    @NotNull
    public static final BracketsGameEvent INSTANCE = new BracketsGameEvent();
    @NotNull
    private static final String NAME = "Brackets";
    @NotNull
    private static final String PERMISSION = "com.qrakn.morpheus.host.brackets";
    @NotNull
    private static final String DESCRIPTION = "Compete against other players in brackets.";

    private BracketsGameEvent() {
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
        return new ItemStack(Material.IRON_SWORD);
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
        boolean bl = false;
        return new ArrayList();
    }

    @Override
    @NotNull
    public String getNameTag(@NotNull Game game, @NotNull Player player, @NotNull Player viewer) {
        Object object;
        Intrinsics.checkNotNullParameter(game, "game");
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(viewer, "viewer");
        Object object2 = game.getLogic();
        Object object3 = object = object2 instanceof BracketsGameEventLogic ? (BracketsGameEventLogic)object2 : null;
        if (object == null) {
            return "";
        }
        Object logic = object;
        if (Intrinsics.areEqual(((GameTeamEventLogic)logic).getInvites().get(player.getUniqueId()), viewer.getUniqueId()) || Intrinsics.areEqual(((GameTeamEventLogic)logic).getInvites().get(viewer.getUniqueId()), player.getUniqueId())) {
            object = ChatColor.YELLOW.toString();
            Intrinsics.checkNotNullExpressionValue(object, "YELLOW.toString()");
            return object;
        }
        GameTeam participant = ((GameTeamEventLogic)logic).get(player);
        if (participant != null && ArraysKt.contains(participant.getPlayers(), viewer)) {
            object2 = ChatColor.GREEN.toString();
            Intrinsics.checkNotNullExpressionValue(object2, "GREEN.toString()");
            return object2;
        }
        if (participant == null && game.getState() != GameState.STARTING) {
            object2 = ChatColor.GRAY.toString();
            Intrinsics.checkNotNullExpressionValue(object2, "GRAY.toString()");
            return object2;
        }
        object2 = ChatColor.GOLD.toString();
        Intrinsics.checkNotNullExpressionValue(object2, "GOLD.toString()");
        return object2;
    }

    @Override
    @NotNull
    public List<Listener> getListeners() {
        BracketsGameEventListeners[] bracketsGameEventListenersArray = new BracketsGameEventListeners[]{new BracketsGameEventListeners()};
        return CollectionsKt.arrayListOf(bracketsGameEventListenersArray);
    }

    @Override
    @NotNull
    public List<GameParameter> getParameters() {
        GameParameter[] gameParameterArray = new GameParameter[]{GameTeamSizeParameter.INSTANCE, BracketsGameKitParameter.INSTANCE};
        return CollectionsKt.listOf(gameParameterArray);
    }

    @Override
    @NotNull
    public List<ItemStack> getLobbyItems(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        boolean bl = false;
        return CollectionsKt.emptyList();
    }

    @Override
    @Nullable
    public Knockback getKnockback() {
        return null;
    }
}

