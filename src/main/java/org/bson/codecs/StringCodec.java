/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class StringCodec
implements Codec<String> {
    @Override
    public void encode(BsonWriter writer, String value, EncoderContext encoderContext) {
        writer.writeString(value);
    }

    @Override
    public String decode(BsonReader reader, DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == BsonType.SYMBOL) {
            return reader.readSymbol();
        }
        return reader.readString();
    }

    @Override
    public Class<String> getEncoderClass() {
        return String.class;
    }
}

