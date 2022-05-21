/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.util;

public class BridgeUtil {
    public static String barBuilder(int wins, String color) {
        if (wins == 1) {
            return color + "\u2b24&f\u2b24\u2b24";
        }
        if (wins == 2) {
            return color + "\u2b24\u2b24&f\u2b24";
        }
        if (wins == 3) {
            return color + "\u2b24\u2b24\u2b24";
        }
        return "&f\u2b24\u2b24\u2b24";
    }
}

