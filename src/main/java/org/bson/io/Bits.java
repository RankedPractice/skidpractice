/*
 * Decompiled with CFR 0.152.
 */
package org.bson.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class Bits {
    public static void readFully(InputStream inputStream, byte[] buffer) throws IOException {
        Bits.readFully(inputStream, buffer, buffer.length);
    }

    public static void readFully(InputStream inputStream, byte[] buffer, int length) throws IOException {
        Bits.readFully(inputStream, buffer, 0, length);
    }

    public static void readFully(InputStream inputStream, byte[] buffer, int offset, int length) throws IOException {
        if (buffer.length < length + offset) {
            throw new IllegalArgumentException("Buffer is too small");
        }
        int arrayOffset = offset;
        int bytesToRead = length;
        while (bytesToRead > 0) {
            int bytesRead = inputStream.read(buffer, arrayOffset, bytesToRead);
            if (bytesRead < 0) {
                throw new EOFException();
            }
            bytesToRead -= bytesRead;
            arrayOffset += bytesRead;
        }
    }

    public static int readInt(InputStream inputStream) throws IOException {
        return Bits.readInt(inputStream, new byte[4]);
    }

    public static int readInt(InputStream inputStream, byte[] buffer) throws IOException {
        Bits.readFully(inputStream, buffer, 4);
        return Bits.readInt(buffer);
    }

    public static int readInt(byte[] buffer) {
        return Bits.readInt(buffer, 0);
    }

    public static int readInt(byte[] buffer, int offset) {
        int x = 0;
        x |= (0xFF & buffer[offset + 0]) << 0;
        x |= (0xFF & buffer[offset + 1]) << 8;
        x |= (0xFF & buffer[offset + 2]) << 16;
        return x |= (0xFF & buffer[offset + 3]) << 24;
    }

    public static int readIntBE(byte[] buffer, int offset) {
        int x = 0;
        x |= (0xFF & buffer[offset + 0]) << 24;
        x |= (0xFF & buffer[offset + 1]) << 16;
        x |= (0xFF & buffer[offset + 2]) << 8;
        return x |= (0xFF & buffer[offset + 3]) << 0;
    }

    public static long readLong(InputStream inputStream) throws IOException {
        return Bits.readLong(inputStream, new byte[8]);
    }

    public static long readLong(InputStream inputStream, byte[] buffer) throws IOException {
        Bits.readFully(inputStream, buffer, 8);
        return Bits.readLong(buffer);
    }

    public static long readLong(byte[] buffer) {
        return Bits.readLong(buffer, 0);
    }

    public static long readLong(byte[] buffer, int offset) {
        long x = 0L;
        x |= (0xFFL & (long)buffer[offset + 0]) << 0;
        x |= (0xFFL & (long)buffer[offset + 1]) << 8;
        x |= (0xFFL & (long)buffer[offset + 2]) << 16;
        x |= (0xFFL & (long)buffer[offset + 3]) << 24;
        x |= (0xFFL & (long)buffer[offset + 4]) << 32;
        x |= (0xFFL & (long)buffer[offset + 5]) << 40;
        x |= (0xFFL & (long)buffer[offset + 6]) << 48;
        return x |= (0xFFL & (long)buffer[offset + 7]) << 56;
    }
}

