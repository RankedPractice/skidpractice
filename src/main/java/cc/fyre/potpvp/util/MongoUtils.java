/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.util;

import cc.fyre.potpvp.PotPvP;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

public final class MongoUtils {
    public static final UpdateOptions UPSERT_OPTIONS = new UpdateOptions().upsert(true);

    public static MongoCollection<Document> getCollection(String collectionId) {
        return PotPvP.getInstance().getMongoDatabase().getCollection(collectionId);
    }

    private MongoUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

