/*
 * Decompiled with CFR 0.152.
 */
package org.bson.json;

import org.bson.BsonTimestamp;
import org.bson.internal.UnsignedLongs;
import org.bson.json.Converter;
import org.bson.json.StrictJsonWriter;

class ExtendedJsonTimestampConverter
implements Converter<BsonTimestamp> {
    ExtendedJsonTimestampConverter() {
    }

    @Override
    public void convert(BsonTimestamp value, StrictJsonWriter writer) {
        writer.writeStartObject();
        writer.writeStartObject("$timestamp");
        writer.writeNumber("t", UnsignedLongs.toString(this.toUnsignedLong(value.getTime())));
        writer.writeNumber("i", UnsignedLongs.toString(this.toUnsignedLong(value.getInc())));
        writer.writeEndObject();
        writer.writeEndObject();
    }

    private long toUnsignedLong(int value) {
        return (long)value & 0xFFFFFFFFL;
    }
}

