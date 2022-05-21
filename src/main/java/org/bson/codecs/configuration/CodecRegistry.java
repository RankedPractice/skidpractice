/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs.configuration;

import org.bson.codecs.Codec;

public interface CodecRegistry {
    public <T> Codec<T> get(Class<T> var1);
}

