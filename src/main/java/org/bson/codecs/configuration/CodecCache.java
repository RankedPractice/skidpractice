/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs.configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.Optional;

final class CodecCache {
    private final ConcurrentMap<Class<?>, Optional<? extends Codec<?>>> codecCache = new ConcurrentHashMap();

    CodecCache() {
    }

    public boolean containsKey(Class<?> clazz) {
        return this.codecCache.containsKey(clazz);
    }

    public void put(Class<?> clazz, Codec<?> codec) {
        this.codecCache.put(clazz, Optional.of(codec));
    }

    public <T> Codec<T> getOrThrow(Class<T> clazz) {
        Optional optionalCodec;
        if (this.codecCache.containsKey(clazz) && !(optionalCodec = (Optional)this.codecCache.get(clazz)).isEmpty()) {
            return (Codec)optionalCodec.get();
        }
        throw new CodecConfigurationException(String.format("Can't find a codec for %s.", clazz));
    }
}

