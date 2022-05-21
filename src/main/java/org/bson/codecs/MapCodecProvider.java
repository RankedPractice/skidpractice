/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs;

import java.util.Map;
import org.bson.Transformer;
import org.bson.assertions.Assertions;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.Codec;
import org.bson.codecs.MapCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class MapCodecProvider
implements CodecProvider {
    private final BsonTypeClassMap bsonTypeClassMap;
    private final Transformer valueTransformer;

    public MapCodecProvider() {
        this(new BsonTypeClassMap());
    }

    public MapCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
        this(bsonTypeClassMap, null);
    }

    public MapCodecProvider(Transformer valueTransformer) {
        this(new BsonTypeClassMap(), valueTransformer);
    }

    public MapCodecProvider(BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
        this.bsonTypeClassMap = Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
        this.valueTransformer = valueTransformer;
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (Map.class.isAssignableFrom(clazz)) {
            return new MapCodec(registry, this.bsonTypeClassMap, this.valueTransformer);
        }
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MapCodecProvider that = (MapCodecProvider)o;
        if (!this.bsonTypeClassMap.equals(that.bsonTypeClassMap)) {
            return false;
        }
        return !(this.valueTransformer != null ? !this.valueTransformer.equals(that.valueTransformer) : that.valueTransformer != null);
    }

    public int hashCode() {
        int result2 = this.bsonTypeClassMap.hashCode();
        result2 = 31 * result2 + (this.valueTransformer != null ? this.valueTransformer.hashCode() : 0);
        return result2;
    }
}

