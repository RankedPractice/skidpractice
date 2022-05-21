/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package cc.fyre.potpvp.setting.repository;

import cc.fyre.potpvp.PotPvP;
import cc.fyre.potpvp.setting.Setting;
import cc.fyre.potpvp.setting.repository.SettingRepository;
import cc.fyre.potpvp.util.MongoUtils;
import com.google.common.collect.ImmutableMap;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.bson.conversions.Bson;

public final class MongoSettingRepository
implements SettingRepository {
    private static final String MONGO_COLLECTION_NAME = "playerSettings";

    @Override
    public Map<Setting, Boolean> loadSettings(UUID playerUuid) throws IOException {
        Document settingsDocument;
        MongoCollection<Document> settingsCollection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        try {
            settingsDocument = (Document)settingsCollection.find(this.buildQuery(playerUuid)).first();
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
        if (settingsDocument == null) {
            return ImmutableMap.of();
        }
        Document rawSettings = (Document)((Object)settingsDocument.get((Object)"settings", Document.class));
        HashMap parsedSettings = new HashMap();
        rawSettings.forEach((rawSetting, value) -> {
            try {
                parsedSettings.put(Setting.valueOf(rawSetting), (Boolean)value);
            }
            catch (Exception ex) {
                PotPvP.getInstance().getLogger().info("Failed to load setting " + rawSetting + " (value=" + value + ") for " + playerUuid + ".");
            }
        });
        return ImmutableMap.copyOf(parsedSettings);
    }

    @Override
    public void saveSettings(UUID playerUuid, Map<Setting, Boolean> settings) throws IOException {
        MongoCollection<Document> settingsCollection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Document settingsDocument = new Document();
        settings.forEach((setting, value) -> settingsDocument.put(setting.name(), value));
        Document update = new Document("$set", new Document("settings", settingsDocument));
        try {
            settingsCollection.updateOne(this.buildQuery(playerUuid), (Bson)update, MongoUtils.UPSERT_OPTIONS);
        }
        catch (MongoException ex) {
            throw new IOException(ex);
        }
    }

    private Document buildQuery(UUID playerUuid) {
        return new Document("_id", playerUuid.toString());
    }
}

