/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package cc.fyre.potpvp.game.util.team;

import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.event.GameEventLogic;
import cc.fyre.potpvp.game.util.team.GameTeam;
import cc.fyre.potpvp.game.util.team.GameTeamSizeParameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0002J\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u001a\u001a\u00020\u0018H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR \u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001b"}, d2={"Lcc/fyre/potpvp/game/util/team/GameTeamEventLogic;", "Lcc/fyre/potpvp/game/event/GameEventLogic;", "game", "Lcc/fyre/potpvp/game/Game;", "(Lcc/fyre/potpvp/game/Game;)V", "invites", "", "Ljava/util/UUID;", "getInvites", "()Ljava/util/Map;", "setInvites", "(Ljava/util/Map;)V", "participants", "", "Lcc/fyre/potpvp/game/util/team/GameTeam;", "getParticipants", "()Ljava/util/Set;", "setParticipants", "(Ljava/util/Set;)V", "contains", "", "player", "Lorg/bukkit/entity/Player;", "generateTeams", "", "get", "start", "potpvp-si"})
public abstract class GameTeamEventLogic
implements GameEventLogic {
    @NotNull
    private final Game game;
    @NotNull
    private Map<UUID, UUID> invites;
    @NotNull
    private Set<GameTeam> participants;

    public GameTeamEventLogic(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        this.game = game;
        this.invites = new HashMap();
        this.participants = new HashSet();
    }

    @NotNull
    public final Map<UUID, UUID> getInvites() {
        return this.invites;
    }

    public final void setInvites(@NotNull Map<UUID, UUID> map) {
        Intrinsics.checkNotNullParameter(map, "<set-?>");
        this.invites = map;
    }

    @NotNull
    public final Set<GameTeam> getParticipants() {
        return this.participants;
    }

    public final void setParticipants(@NotNull Set<GameTeam> set) {
        Intrinsics.checkNotNullParameter(set, "<set-?>");
        this.participants = set;
    }

    @Override
    public void start() {
        if (this.game.getParameter(GameTeamSizeParameter.Duos.INSTANCE.getClass()) != null) {
            this.generateTeams();
        } else {
            for (Player player : this.game.getPlayers()) {
                Player[] playerArray = new Player[1];
                Intrinsics.checkNotNullExpressionValue(player, "player");
                playerArray[0] = player;
                this.participants.add(new GameTeam(playerArray));
            }
        }
        this.invites.clear();
    }

    private final void generateTeams() {
        block0: for (Player player : CollectionsKt.toMutableList((Collection)this.game.getPlayers())) {
            if (this.contains(player)) continue;
            Player[] playerArray = new Player[]{player};
            this.participants.add(new GameTeam(playerArray));
            for (Player other : this.game.getPlayers()) {
                if (Intrinsics.areEqual(player, other)) continue;
                Intrinsics.checkNotNullExpressionValue(other, "other");
                if (this.contains(other)) continue;
                player.sendMessage(ChatColor.YELLOW + "You were automatically put into a team with " + other.getDisplayName() + ChatColor.YELLOW + '.');
                other.sendMessage(ChatColor.YELLOW + "You were automatically put into a team with " + player.getDisplayName() + ChatColor.YELLOW + '.');
                Player[] playerArray2 = new Player[]{player, other};
                GameTeam team = new GameTeam(playerArray2);
                this.participants.add(team);
                continue block0;
            }
        }
    }

    @Nullable
    public final GameTeam get(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        for (GameTeam participant : this.participants) {
            if (!ArraysKt.contains(participant.getPlayers(), player)) continue;
            return participant;
        }
        return null;
    }

    public final boolean contains(@NotNull Player player) {
        Intrinsics.checkNotNullParameter(player, "player");
        for (GameTeam participant : this.participants) {
            if (!ArraysKt.contains(participant.getPlayers(), player)) continue;
            return true;
        }
        return false;
    }
}

