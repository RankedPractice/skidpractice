/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs.pojo;

import java.util.concurrent.ConcurrentMap;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.DiscriminatorLookup;
import org.bson.codecs.pojo.PojoCodec;
import org.bson.codecs.pojo.PojoCodecImpl;
import org.bson.codecs.pojo.PropertyCodecRegistry;

class LazyPojoCodec<T>
extends PojoCodec<T> {
    private final ClassModel<T> classModel;
    private final CodecRegistry registry;
    private final PropertyCodecRegistry propertyCodecRegistry;
    private final DiscriminatorLookup discriminatorLookup;
    private final ConcurrentMap<ClassModel<?>, Codec<?>> codecCache;
    private volatile PojoCodecImpl<T> pojoCodec;

    LazyPojoCodec(ClassModel<T> classModel, CodecRegistry registry, PropertyCodecRegistry propertyCodecRegistry, DiscriminatorLookup discriminatorLookup, ConcurrentMap<ClassModel<?>, Codec<?>> codecCache) {
        this.classModel = classModel;
        this.registry = registry;
        this.propertyCodecRegistry = propertyCodecRegistry;
        this.discriminatorLookup = discriminatorLookup;
        this.codecCache = codecCache;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        this.getPojoCodec().encode(writer, value, encoderContext);
    }

    @Override
    public Class<T> getEncoderClass() {
        return this.classModel.getType();
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return this.getPojoCodec().decode(reader, decoderContext);
    }

    private Codec<T> getPojoCodec() {
        if (this.pojoCodec == null) {
            this.pojoCodec = new PojoCodecImpl<T>(this.classModel, this.registry, this.propertyCodecRegistry, this.discriminatorLookup, this.codecCache, true);
        }
        return this.pojoCodec;
    }

    @Override
    ClassModel<T> getClassModel() {
        return this.classModel;
    }
}

