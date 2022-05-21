/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.bukkit.Location
 *  rip.bridge.qlib.qLib
 */
package cc.fyre.potpvp.game;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.game.Game;
import cc.fyre.potpvp.game.task.GameStartTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import java.lang.reflect.Type;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.bridge.qlib.qLib;

@Metadata(mv={1, 5, 1}, k=1, xi=48, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0014\u001a\u0004\u0018\u00010\rJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\rJ\u000e\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u000fR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001a"}, d2={"Lcc/fyre/potpvp/game/GameHandler;", "", "eventMetaCollection", "Lcom/mongodb/client/MongoCollection;", "Lorg/bson/Document;", "(Lcom/mongodb/client/MongoCollection;)V", "hostEnabled", "", "getHostEnabled", "()Z", "setHostEnabled", "(Z)V", "lobbyLocation", "Lorg/bukkit/Location;", "ongoingGame", "Lcc/fyre/potpvp/game/Game;", "getOngoingGame", "()Lcc/fyre/potpvp/game/Game;", "setOngoingGame", "(Lcc/fyre/potpvp/game/Game;)V", "getLobbyLocation", "setLobbyLocation", "", "location", "startGame", "game", "potpvp-si"})
public final class GameHandler {
    @NotNull
    private final MongoCollection<Document> eventMetaCollection;
    private boolean hostEnabled;
    @Nullable
    private Location lobbyLocation;
    @Nullable
    private Game ongoingGame;

    public GameHandler(@NotNull MongoCollection<Document> eventMetaCollection) {
        Intrinsics.checkNotNullParameter(eventMetaCollection, "eventMetaCollection");
        this.eventMetaCollection = eventMetaCollection;
        this.hostEnabled = true;
        this.lobbyLocation = (Location)qLib.GSON.fromJson(qLib.GSON.toJson(this.eventMetaCollection.find(new Document("_id", "lobby_location")).first()), Location.class);
    }

    public final boolean getHostEnabled() {
        return this.hostEnabled;
    }

    public final void setHostEnabled(boolean bl) {
        this.hostEnabled = bl;
    }

    @Nullable
    public final Game getOngoingGame() {
        return this.ongoingGame;
    }

    public final void setOngoingGame(@Nullable Game game) {
        this.ongoingGame = game;
    }

    public final void startGame(@NotNull Game game) {
        Intrinsics.checkNotNullParameter(game, "game");
        if (this.ongoingGame != null) {
            throw new IllegalStateException("Cannot start game while there is an ongoing game");
        }
        PotPvP potPvP = PotPvP.getInstance();
        Intrinsics.checkNotNullExpressionValue((Object)potPvP, "getInstance()");
        GameStartTask gameStartTask = new GameStartTask(potPvP, game);
    }

    @Nullable
    public final Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    public final void setLobbyLocation(@NotNull Location location) {
        Intrinsics.checkNotNullParameter(location, "location");
        this.lobbyLocation = location;
        JsonObject jsonObject = qLib.GSON.toJsonTree((Object)location, (Type)((Object)Location.class)).getAsJsonObject();
        jsonObject.addProperty("_id", "lobby_location");
        this.eventMetaCollection.replaceOne((Bson)new Document("_id", "lobby_location"), Document.parse(qLib.GSON.toJson((JsonElement)jsonObject)), new ReplaceOptions().upsert(true));
    }
}

