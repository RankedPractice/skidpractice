/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs;

final class UuidCodecHelper {
    public static void reverseByteArray(byte[] data2, int start, int length) {
        int left = start;
        for (int right = start + length - 1; left < right; ++left, --right) {
            byte temp = data2[left];
            data2[left] = data2[right];
            data2[right] = temp;
        }
    }

    private UuidCodecHelper() {
    }
}

