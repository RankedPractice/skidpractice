/*
 * Decompiled with CFR 0.152.
 */
package org.bson.internal;

import java.math.BigInteger;

public final class UnsignedLongs {
    private static final long MAX_VALUE = -1L;
    private static final long[] MAX_VALUE_DIVS = new long[37];
    private static final int[] MAX_VALUE_MODS = new int[37];
    private static final int[] MAX_SAFE_DIGITS = new int[37];

    public static int compare(long first, long second) {
        return UnsignedLongs.compareLongs(first + Long.MIN_VALUE, second + Long.MIN_VALUE);
    }

    public static String toString(long value) {
        if (value >= 0L) {
            return Long.toString(value);
        }
        long quotient = (value >>> 1) / 5L;
        long remainder = value - quotient * 10L;
        return Long.toString(quotient) + remainder;
    }

    public static long parse(String string) {
        if (string.length() == 0) {
            throw new NumberFormatException("empty string");
        }
        int radix = 10;
        int maxSafePos = MAX_SAFE_DIGITS[radix] - 1;
        long value = 0L;
        for (int pos = 0; pos < string.length(); ++pos) {
            int digit = Character.digit(string.charAt(pos), radix);
            if (digit == -1) {
                throw new NumberFormatException(string);
            }
            if (pos > maxSafePos && UnsignedLongs.overflowInParse(value, digit, radix)) {
                throw new NumberFormatException("Too large for unsigned long: " + string);
            }
            value = value * (long)radix + (long)digit;
        }
        return value;
    }

    private static boolean overflowInParse(long current, int digit, int radix) {
        if (current >= 0L) {
            if (current < MAX_VALUE_DIVS[radix]) {
                return false;
            }
            if (current > MAX_VALUE_DIVS[radix]) {
                return true;
            }
            return digit > MAX_VALUE_MODS[radix];
        }
        return true;
    }

    private static int compareLongs(long x, long y) {
        return x < y ? -1 : (x == y ? 0 : 1);
    }

    private static long divide(long dividend, long divisor) {
        long quotient;
        if (divisor < 0L) {
            if (UnsignedLongs.compare(dividend, divisor) < 0) {
                return 0L;
            }
            return 1L;
        }
        if (dividend >= 0L) {
            return dividend / divisor;
        }
        long rem = dividend - (quotient = (dividend >>> 1) / divisor << 1) * divisor;
        return quotient + (long)(UnsignedLongs.compare(rem, divisor) >= 0 ? 1 : 0);
    }

    private static long remainder(long dividend, long divisor) {
        long rem;
        if (divisor < 0L) {
            if (UnsignedLongs.compare(dividend, divisor) < 0) {
                return dividend;
            }
            return dividend - divisor;
        }
        if (dividend >= 0L) {
            return dividend % divisor;
        }
        long quotient = (dividend >>> 1) / divisor << 1;
        return rem - (UnsignedLongs.compare(rem = dividend - quotient * divisor, divisor) >= 0 ? divisor : 0L);
    }

    private UnsignedLongs() {
    }

    static {
        BigInteger overflow = new BigInteger("10000000000000000", 16);
        for (int i = 2; i <= 36; ++i) {
            UnsignedLongs.MAX_VALUE_DIVS[i] = UnsignedLongs.divide(-1L, i);
            UnsignedLongs.MAX_VALUE_MODS[i] = (int)UnsignedLongs.remainder(-1L, i);
            UnsignedLongs.MAX_SAFE_DIGITS[i] = overflow.toString(i).length() - 1;
        }
    }
}

