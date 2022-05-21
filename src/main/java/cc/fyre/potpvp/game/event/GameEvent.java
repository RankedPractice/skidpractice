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
 *  rip.bridge.qlib.util.ItemBuilder
 */
package cc.fyre.potpvp.game.event;

import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.GameEventItems;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.event.impl.brackets.BracketsGameEvent;
import cc.fyre.potpvp.game.event.impl.sumo.SumoGameEvent;
import cc.fyre.potpvp.game.parameter.GameParameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.knockback.Knockback;
import rip.bridge.qlib.util.ItemBuilder;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bJ\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&J\n\u0010\n\u001a\u0004\u0018\u00010\u000bH&J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH&J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\r2\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0012\u001a\u00020\u0007H&J \u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H&J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\rH&J\b\u0010\u0019\u001a\u00020\u0007H&J\u001e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00070\r2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u001c"}, d2={"Lcc/fyre/potpvp/game/event/GameEvent;", "", "canStart", "", "game", "Lcc/fyre/potpvp/game/Game;", "getDescription", "", "getIcon", "Lorg/bukkit/inventory/ItemStack;", "getKnockback", "Lrip/bridge/knockback/Knockback;", "getListeners", "", "Lorg/bukkit/event/Listener;", "getLobbyItems", "getLogic", "Lcc/fyre/potpvp/game/event/GameEventLogic;", "getName", "getNameTag", "player", "Lorg/bukkit/entity/Player;", "viewer", "getParameters", "Lcc/fyre/potpvp/game/parameter/GameParameter;", "getPermission", "getScoreboardScores", "Companion", "potpvp-si"})
public interface GameEvent {
    @NotNull
    public static final Companion Companion = cc.fyre.potpvp.game.event.GameEvent$Companion.$$INSTANCE;

    @NotNull
    public String getName();

    @NotNull
    public String getPermission();

    @NotNull
    public String getDescription();

    @NotNull
    public ItemStack getIcon();

    public boolean canStart(@NotNull Game var1);

    @NotNull
    public GameEventLogic getLogic(@NotNull Game var1);

    @NotNull
    public String getNameTag(@NotNull Game var1, @NotNull Player var2, @NotNull Player var3);

    @NotNull
    public List<String> getScoreboardScores(@NotNull Player var1, @NotNull Game var2);

    @NotNull
    public List<Listener> getListeners();

    @NotNull
    public List<GameParameter> getParameters();

    @NotNull
    public List<ItemStack> getLobbyItems(@NotNull Game var1);

    @Nullable
    public Knockback getKnockback();

    @NotNull
    public static ArrayList<GameEvent> getEvents() {
        return Companion.getEvents();
    }

    public static ItemStack getLeaveItem() {
        return Companion.getLeaveItem();
    }

    @Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R,\u0010\u0007\u001a\u0012\u0012\u0004\u0012\u00020\t0\bj\b\u0012\u0004\u0012\u00020\t`\n8\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u000b\u0010\u0002\u001a\u0004\b\f\u0010\rR$\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000f8\u0006X\u0087\u0004\u00a2\u0006\u000e\n\u0000\u0012\u0004\b\u0011\u0010\u0002\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u0014"}, d2={"Lcc/fyre/potpvp/game/event/GameEvent$Companion;", "", "()V", "VOTE_PATTERN", "Ljava/util/regex/Pattern;", "getVOTE_PATTERN", "()Ljava/util/regex/Pattern;", "events", "Ljava/util/ArrayList;", "Lcc/fyre/potpvp/game/event/GameEvent;", "Lkotlin/collections/ArrayList;", "getEvents$annotations", "getEvents", "()Ljava/util/ArrayList;", "leaveItem", "Lorg/bukkit/inventory/ItemStack;", "kotlin.jvm.PlatformType", "getLeaveItem$annotations", "getLeaveItem", "()Lorg/bukkit/inventory/ItemStack;", "potpvp-si"})
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE;
        @NotNull
        private static final ArrayList<GameEvent> events;
        private static final ItemStack leaveItem;
        @NotNull
        private static final Pattern VOTE_PATTERN;

        private Companion() {
        }

        @NotNull
        public final ArrayList<GameEvent> getEvents() {
            return events;
        }

        @JvmStatic
        public static /* synthetic */ void getEvents$annotations() {
        }

        public final ItemStack getLeaveItem() {
            return leaveItem;
        }

        @JvmStatic
        public static /* synthetic */ void getLeaveItem$annotations() {
        }

        @NotNull
        public final Pattern getVOTE_PATTERN() {
            return VOTE_PATTERN;
        }

        static {
            $$INSTANCE = new Companion();
            GameEvent[] gameEventArray = new GameEvent[]{SumoGameEvent.INSTANCE, BracketsGameEvent.INSTANCE};
            events = CollectionsKt.arrayListOf(gameEventArray);
            leaveItem = ItemBuilder.of((Material)Material.INK_SACK).data((short)1).name(Intrinsics.stringPlus(ChatColor.RED.toString(), "Leave Event")).build();
            Object object = GameEventItems.getVoteItem().getItemMeta().getDisplayName();
            Intrinsics.checkNotNullExpressionValue(object, "voteItem.itemMeta.displayName");
            CharSequence charSequence = (CharSequence)object;
            object = new String[]{"%MAP%"};
            List nameSplit = StringsKt.split$default(charSequence, object, false, 0, 6, null);
            object = Pattern.compile('(' + (String)nameSplit.get(0) + ")(.*)(" + (nameSplit.size() > 1 ? (String)nameSplit.get(1) : "") + ')');
            Intrinsics.checkNotNullExpressionValue(object, "compile(\"(\" + nameSplit[\u2026eSplit[1] else \"\") + \")\")");
            VOTE_PATTERN = object;
        }
    }
}

