/*
 * Decompiled with CFR 0.152.
 */
package org.bson.conversions;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;

public interface Bson {
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> var1, CodecRegistry var2);
}

