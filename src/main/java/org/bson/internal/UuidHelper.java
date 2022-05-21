/*
 * Decompiled with CFR 0.152.
 */
package org.bson.internal;

import java.util.UUID;
import org.bson.BSONException;
import org.bson.BsonBinarySubType;
import org.bson.BsonSerializationException;
import org.bson.UuidRepresentation;

public final class UuidHelper {
    private static void writeLongToArrayBigEndian(byte[] bytes, int offset, long x) {
        bytes[offset + 7] = (byte)(0xFFL & x);
        bytes[offset + 6] = (byte)(0xFFL & x >> 8);
        bytes[offset + 5] = (byte)(0xFFL & x >> 16);
        bytes[offset + 4] = (byte)(0xFFL & x >> 24);
        bytes[offset + 3] = (byte)(0xFFL & x >> 32);
        bytes[offset + 2] = (byte)(0xFFL & x >> 40);
        bytes[offset + 1] = (byte)(0xFFL & x >> 48);
        bytes[offset] = (byte)(0xFFL & x >> 56);
    }

    private static long readLongFromArrayBigEndian(byte[] bytes, int offset) {
        long x = 0L;
        x |= 0xFFL & (long)bytes[offset + 7];
        x |= (0xFFL & (long)bytes[offset + 6]) << 8;
        x |= (0xFFL & (long)bytes[offset + 5]) << 16;
        x |= (0xFFL & (long)bytes[offset + 4]) << 24;
        x |= (0xFFL & (long)bytes[offset + 3]) << 32;
        x |= (0xFFL & (long)bytes[offset + 2]) << 40;
        x |= (0xFFL & (long)bytes[offset + 1]) << 48;
        return x |= (0xFFL & (long)bytes[offset]) << 56;
    }

    private static void reverseByteArray(byte[] data2, int start, int length) {
        int left = start;
        for (int right = start + length - 1; left < right; ++left, --right) {
            byte temp = data2[left];
            data2[left] = data2[right];
            data2[right] = temp;
        }
    }

    public static byte[] encodeUuidToBinary(UUID uuid, UuidRepresentation uuidRepresentation) {
        byte[] binaryData = new byte[16];
        UuidHelper.writeLongToArrayBigEndian(binaryData, 0, uuid.getMostSignificantBits());
        UuidHelper.writeLongToArrayBigEndian(binaryData, 8, uuid.getLeastSignificantBits());
        switch (uuidRepresentation) {
            case C_SHARP_LEGACY: {
                UuidHelper.reverseByteArray(binaryData, 0, 4);
                UuidHelper.reverseByteArray(binaryData, 4, 2);
                UuidHelper.reverseByteArray(binaryData, 6, 2);
                break;
            }
            case JAVA_LEGACY: {
                UuidHelper.reverseByteArray(binaryData, 0, 8);
                UuidHelper.reverseByteArray(binaryData, 8, 8);
                break;
            }
            case PYTHON_LEGACY: 
            case STANDARD: {
                break;
            }
            default: {
                throw new BSONException("Unexpected UUID representation");
            }
        }
        return binaryData;
    }

    public static UUID decodeBinaryToUuid(byte[] data2, byte type2, UuidRepresentation uuidRepresentation) {
        if (data2.length != 16) {
            throw new BsonSerializationException(String.format("Expected length to be 16, not %d.", data2.length));
        }
        if (type2 == BsonBinarySubType.UUID_LEGACY.getValue()) {
            switch (uuidRepresentation) {
                case C_SHARP_LEGACY: {
                    UuidHelper.reverseByteArray(data2, 0, 4);
                    UuidHelper.reverseByteArray(data2, 4, 2);
                    UuidHelper.reverseByteArray(data2, 6, 2);
                    break;
                }
                case JAVA_LEGACY: {
                    UuidHelper.reverseByteArray(data2, 0, 8);
                    UuidHelper.reverseByteArray(data2, 8, 8);
                    break;
                }
                case PYTHON_LEGACY: 
                case STANDARD: {
                    break;
                }
                default: {
                    throw new BSONException("Unexpected UUID representation");
                }
            }
        }
        return new UUID(UuidHelper.readLongFromArrayBigEndian(data2, 0), UuidHelper.readLongFromArrayBigEndian(data2, 8));
    }

    private UuidHelper() {
    }
}

