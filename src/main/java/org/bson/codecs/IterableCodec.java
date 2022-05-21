/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs;

import java.util.ArrayList;
import java.util.UUID;
import org.bson.BsonBinarySubType;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Transformer;
import org.bson.assertions.Assertions;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.BsonTypeCodecMap;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class IterableCodec
implements Codec<Iterable> {
    private final CodecRegistry registry;
    private final BsonTypeCodecMap bsonTypeCodecMap;
    private final Transformer valueTransformer;

    public IterableCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap) {
        this(registry, bsonTypeClassMap, null);
    }

    public IterableCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
        this.registry = Assertions.notNull("registry", registry);
        this.bsonTypeCodecMap = new BsonTypeCodecMap(Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), registry);
        this.valueTransformer = valueTransformer != null ? valueTransformer : new Transformer(){

            @Override
            public Object transform(Object objectToTransform) {
                return objectToTransform;
            }
        };
    }

    @Override
    public Iterable decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartArray();
        ArrayList<Object> list = new ArrayList<Object>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            list.add(this.readValue(reader, decoderContext));
        }
        reader.readEndArray();
        return list;
    }

    @Override
    public void encode(BsonWriter writer, Iterable value, EncoderContext encoderContext) {
        writer.writeStartArray();
        for (Object cur : value) {
            this.writeValue(writer, encoderContext, cur);
        }
        writer.writeEndArray();
    }

    @Override
    public Class<Iterable> getEncoderClass() {
        return Iterable.class;
    }

    private void writeValue(BsonWriter writer, EncoderContext encoderContext, Object value) {
        if (value == null) {
            writer.writeNull();
        } else {
            Codec<?> codec = this.registry.get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    private Object readValue(BsonReader reader, DecoderContext decoderContext) {
        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType == BsonType.NULL) {
            reader.readNull();
            return null;
        }
        if (bsonType == BsonType.BINARY && BsonBinarySubType.isUuid(reader.peekBinarySubType()) && reader.peekBinarySize() == 16) {
            return this.registry.get(UUID.class).decode(reader, decoderContext);
        }
        return this.valueTransformer.transform(this.bsonTypeCodecMap.get(bsonType).decode(reader, decoderContext));
    }
}

