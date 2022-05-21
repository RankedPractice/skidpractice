/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.profile;

import cc.fyre.potpvp.killeffects.effects.KillEffect;
import cc.fyre.potpvp.util.MongoUtils;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Profile {
    private final UUID uniqueId;
    private KillEffect killEffect = KillEffect.none;
    private int gamesWon = 0;
    private int winStreak = 0;
    private int highestWinStreak = 0;
    private int gamesPlayed = 0;
    private int kills = 0;
    private int deaths = 0;
    private int matchKills = 0;
    private int highestCombo = 0;
    private int loses = 0;
    private int coins = 0;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.load();
    }

    public void load() {
        Document document = (Document)MongoUtils.getCollection("profiles").find(Filters.eq("uuid", this.uniqueId.toString())).first();
        if (document == null) {
            return;
        }
        this.gamesPlayed = document.getInteger("gamesPlayed");
        this.gamesWon = document.getInteger("gamesWon");
        this.kills = document.getInteger("kills");
        this.deaths = document.getInteger("deaths");
        this.loses = document.getInteger("loses");
        this.coins = document.getInteger("stars");
        this.highestWinStreak = document.getInteger("highestWinStreak");
        this.winStreak = document.getInteger("winStreak");
        this.highestCombo = document.getInteger("highestCombo");
        this.killEffect = KillEffect.valueOf(document.getString("killEffect"));
    }

    public void save() {
        CompletableFuture.runAsync(() -> {
            Document document = new Document();
            document.put("uuid", (Object)this.uniqueId.toString());
            document.put("gamesPlayed", (Object)this.gamesPlayed);
            document.put("gamesWon", (Object)this.gamesWon);
            document.put("kills", (Object)this.kills);
            document.put("deaths", (Object)this.deaths);
            document.put("highestCombo", (Object)this.highestCombo);
            document.put("loses", (Object)this.loses);
            document.put("stars", (Object)this.coins);
            document.put("highestWinStreak", (Object)this.highestWinStreak);
            document.put("winStreak", (Object)this.winStreak);
            document.put("killEffect", (Object)this.killEffect.getName());
            Bson filter = Filters.eq("uuid", this.uniqueId.toString());
            MongoUtils.getCollection("profiles").replaceOne(filter, document, new ReplaceOptions().upsert(true));
        });
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public KillEffect getKillEffect() {
        return this.killEffect;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public int getWinStreak() {
        return this.winStreak;
    }

    public int getHighestWinStreak() {
        return this.highestWinStreak;
    }

    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getMatchKills() {
        return this.matchKills;
    }

    public int getHighestCombo() {
        return this.highestCombo;
    }

    public int getLoses() {
        return this.loses;
    }

    public int getCoins() {
        return this.coins;
    }

    public void setKillEffect(KillEffect killEffect) {
        this.killEffect = killEffect;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public void setHighestWinStreak(int highestWinStreak) {
        this.highestWinStreak = highestWinStreak;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setMatchKills(int matchKills) {
        this.matchKills = matchKills;
    }

    public void setHighestCombo(int highestCombo) {
        this.highestCombo = highestCombo;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        Profile other = (Profile)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getGamesWon() != other.getGamesWon()) {
            return false;
        }
        if (this.getWinStreak() != other.getWinStreak()) {
            return false;
        }
        if (this.getHighestWinStreak() != other.getHighestWinStreak()) {
            return false;
        }
        if (this.getGamesPlayed() != other.getGamesPlayed()) {
            return false;
        }
        if (this.getKills() != other.getKills()) {
            return false;
        }
        if (this.getDeaths() != other.getDeaths()) {
            return false;
        }
        if (this.getMatchKills() != other.getMatchKills()) {
            return false;
        }
        if (this.getHighestCombo() != other.getHighestCombo()) {
            return false;
        }
        if (this.getLoses() != other.getLoses()) {
            return false;
        }
        if (this.getCoins() != other.getCoins()) {
            return false;
        }
        UUID this$uniqueId = this.getUniqueId();
        UUID other$uniqueId = other.getUniqueId();
        if (this$uniqueId == null ? other$uniqueId != null : !((Object)this$uniqueId).equals(other$uniqueId)) {
            return false;
        }
        KillEffect this$killEffect = this.getKillEffect();
        KillEffect other$killEffect = other.getKillEffect();
        return !(this$killEffect == null ? other$killEffect != null : !((Object)((Object)this$killEffect)).equals((Object)other$killEffect));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Profile;
    }

    public int hashCode() {
        int PRIME = 59;
        int result2 = 1;
        result2 = result2 * 59 + this.getGamesWon();
        result2 = result2 * 59 + this.getWinStreak();
        result2 = result2 * 59 + this.getHighestWinStreak();
        result2 = result2 * 59 + this.getGamesPlayed();
        result2 = result2 * 59 + this.getKills();
        result2 = result2 * 59 + this.getDeaths();
        result2 = result2 * 59 + this.getMatchKills();
        result2 = result2 * 59 + this.getHighestCombo();
        result2 = result2 * 59 + this.getLoses();
        result2 = result2 * 59 + this.getCoins();
        UUID $uniqueId = this.getUniqueId();
        result2 = result2 * 59 + ($uniqueId == null ? 43 : ((Object)$uniqueId).hashCode());
        KillEffect $killEffect = this.getKillEffect();
        result2 = result2 * 59 + ($killEffect == null ? 43 : ((Object)((Object)$killEffect)).hashCode());
        return result2;
    }

    public String toString() {
        return "Profile(uniqueId=" + this.getUniqueId() + ", killEffect=" + (Object)((Object)this.getKillEffect()) + ", gamesWon=" + this.getGamesWon() + ", winStreak=" + this.getWinStreak() + ", highestWinStreak=" + this.getHighestWinStreak() + ", gamesPlayed=" + this.getGamesPlayed() + ", kills=" + this.getKills() + ", deaths=" + this.getDeaths() + ", matchKills=" + this.getMatchKills() + ", highestCombo=" + this.getHighestCombo() + ", loses=" + this.getLoses() + ", coins=" + this.getCoins() + ")";
    }
}

