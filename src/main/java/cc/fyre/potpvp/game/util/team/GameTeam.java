/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  rip.bridge.qlib.util.PlayerUtils
 */
package cc.fyre.potpvp.game.util.team;

import cc.fyre.potpvp.util.PatchedPlayerUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.bridge.qlib.util.PlayerUtils;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0003\"\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u0006\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0004J\u000e\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\nJ\u0006\u0010$\u001a\u00020\u0010J\u000e\u0010%\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u0004J\u0006\u0010&\u001a\u00020\nJ\u0006\u0010'\u001a\u00020\u001fR\u001e\u0010\u0006\u001a\u0012\u0012\u0004\u0012\u00020\u00040\u0007j\b\u0012\u0004\u0012\u00020\u0004`\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R$\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040\u0003X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0018\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0005R\u001a\u0010\u0019\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0012\"\u0004\b\u001b\u0010\u0014R\u001a\u0010\u001c\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\f\"\u0004\b\u001e\u0010\u000e\u00a8\u0006("}, d2={"Lcc/fyre/potpvp/game/util/team/GameTeam;", "", "players", "", "Lorg/bukkit/entity/Player;", "([Lorg/bukkit/entity/Player;)V", "died", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "fighting", "", "getFighting", "()Z", "setFighting", "(Z)V", "kills", "", "getKills", "()I", "setKills", "(I)V", "getPlayers", "()[Lorg/bukkit/entity/Player;", "setPlayers", "[Lorg/bukkit/entity/Player;", "round", "getRound", "setRound", "starting", "getStarting", "setStarting", "", "player", "getName", "", "displayNames", "getPing", "hasDied", "isFinished", "reset", "potpvp-si"})
public final class GameTeam {
    @NotNull
    private Player[] players;
    @NotNull
    private ArrayList<Player> died;
    private int round;
    private int kills;
    private boolean fighting;
    private boolean starting;

    public GameTeam(Player ... players) {
        Intrinsics.checkNotNullParameter(players, "players");
        this.players = players;
        this.died = new ArrayList();
    }

    @NotNull
    public final Player[] getPlayers() {
        return this.players;
    }

    public final void setPlayers(@NotNull Player[] playerArray) {
        Intrinsics.checkNotNullParameter(playerArray, "<set-?>");
        this.players = playerArray;
    }

    public final int getRound() {
        return this.round;
    }

    public final void setRound(int n) {
        this.round = n;
    }

    public final int getKills() {
        return this.kills;
    }

    public final void setKills(int n) {
        this.kills = n;
    }

    public final boolean getFighting() {
        return this.fighting;
    }

    public final void setFighting(boolean bl) {
        this.fighting = bl;
    }

    public final boolean getStarting() {
        return this.starting;
    }

    public final void setStarting(boolean bl) {
        this.starting = bl;
    }

    public final boolean isFinished() {
        return this.died.size() == this.players.length;
    }

    public final void died(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (!this.died.contains(player)) {
            this.died.add(player);
        }
    }

    public final boolean hasDied(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        return this.died.contains(player);
    }

    public final void reset() {
        this.died.clear();
        this.fighting = false;
        this.starting = false;
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public final String getName(boolean displayNames) {
        void $this$mapTo$iv$iv;
        Player[] $this$map$iv = this.players;
        boolean $i$f$map = false;
        Player[] playerArray = $this$map$iv;
        Collection destination$iv$iv = new ArrayList($this$map$iv.length);
        boolean $i$f$mapTo = false;
        void var8_7 = $this$mapTo$iv$iv;
        int n = ((void)var8_7).length;
        for (int i = 0; i < n; ++i) {
            void it;
            void item$iv$iv;
            void var12_11 = item$iv$iv = var8_7[i];
            Collection collection = destination$iv$iv;
            boolean bl = false;
            String string = displayNames ? PatchedPlayerUtils.getFormattedName(it.getUniqueId()) : it.getName();
            collection.add(string);
        }
        Collection $this$toTypedArray$iv = (List)destination$iv$iv;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        Object[] objectArray = thisCollection$iv.toArray(new String[0]);
        if (objectArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        String string = StringUtils.join((Object[])objectArray, (String)(displayNames ? Intrinsics.stringPlus(ChatColor.GRAY.toString(), " + ") : " + "));
        Intrinsics.checkNotNullExpressionValue(string, "join(players.map { if (d\u2026ing() + \" + \" else \" + \")");
        return string;
    }

    public final int getPing() {
        return PlayerUtils.getPing((Player)this.players[0]);
    }
}

