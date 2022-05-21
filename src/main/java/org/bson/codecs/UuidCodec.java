/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs;

import java.util.UUID;
import org.bson.BSONException;
import org.bson.BsonBinary;
import org.bson.BsonBinarySubType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.UuidRepresentation;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.internal.UuidHelper;

public class UuidCodec
implements Codec<UUID> {
    private final UuidRepresentation encoderUuidRepresentation;
    private final UuidRepresentation decoderUuidRepresentation;

    public UuidCodec(UuidRepresentation uuidRepresentation) {
        this.encoderUuidRepresentation = uuidRepresentation;
        this.decoderUuidRepresentation = uuidRepresentation;
    }

    public UuidCodec() {
        this.encoderUuidRepresentation = UuidRepresentation.JAVA_LEGACY;
        this.decoderUuidRepresentation = UuidRepresentation.JAVA_LEGACY;
    }

    @Override
    public void encode(BsonWriter writer, UUID value, EncoderContext encoderContext) {
        byte[] binaryData = UuidHelper.encodeUuidToBinary(value, this.encoderUuidRepresentation);
        if (this.encoderUuidRepresentation == UuidRepresentation.STANDARD) {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_STANDARD, binaryData));
        } else {
            writer.writeBinaryData(new BsonBinary(BsonBinarySubType.UUID_LEGACY, binaryData));
        }
    }

    @Override
    public UUID decode(BsonReader reader, DecoderContext decoderContext) {
        byte subType = reader.peekBinarySubType();
        if (subType != BsonBinarySubType.UUID_LEGACY.getValue() && subType != BsonBinarySubType.UUID_STANDARD.getValue()) {
            throw new BSONException("Unexpected BsonBinarySubType");
        }
        byte[] bytes = reader.readBinaryData().getData();
        return UuidHelper.decodeBinaryToUuid(bytes, subType, this.decoderUuidRepresentation);
    }

    @Override
    public Class<UUID> getEncoderClass() {
        return UUID.class;
    }
}

