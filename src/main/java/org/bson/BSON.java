/*
 * Decompiled with CFR 0.152.
 */
package org.bson;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;
import org.bson.Transformer;
import org.bson.util.ClassMap;

@Deprecated
public class BSON {
    public static final byte EOO = 0;
    public static final byte NUMBER = 1;
    public static final byte STRING = 2;
    public static final byte OBJECT = 3;
    public static final byte ARRAY = 4;
    public static final byte BINARY = 5;
    public static final byte UNDEFINED = 6;
    public static final byte OID = 7;
    public static final byte BOOLEAN = 8;
    public static final byte DATE = 9;
    public static final byte NULL = 10;
    public static final byte REGEX = 11;
    public static final byte REF = 12;
    public static final byte CODE = 13;
    public static final byte SYMBOL = 14;
    public static final byte CODE_W_SCOPE = 15;
    public static final byte NUMBER_INT = 16;
    public static final byte TIMESTAMP = 17;
    public static final byte NUMBER_LONG = 18;
    public static final byte MINKEY = -1;
    public static final byte MAXKEY = 127;
    public static final byte B_GENERAL = 0;
    public static final byte B_FUNC = 1;
    public static final byte B_BINARY = 2;
    public static final byte B_UUID = 3;
    private static final int FLAG_GLOBAL = 256;
    private static final int[] FLAG_LOOKUP = new int[65535];
    private static volatile boolean encodeHooks;
    private static volatile boolean decodeHooks;
    private static final ClassMap<List<Transformer>> encodingHooks;
    private static final ClassMap<List<Transformer>> decodingHooks;

    public static boolean hasEncodeHooks() {
        return encodeHooks;
    }

    public static boolean hasDecodeHooks() {
        return decodeHooks;
    }

    public static void addEncodingHook(Class<?> clazz, Transformer transformer) {
        encodeHooks = true;
        List<Transformer> transformersForClass = encodingHooks.get(clazz);
        if (transformersForClass == null) {
            transformersForClass = new CopyOnWriteArrayList<Transformer>();
            encodingHooks.put(clazz, transformersForClass);
        }
        transformersForClass.add(transformer);
    }

    public static void addDecodingHook(Class<?> clazz, Transformer transformer) {
        decodeHooks = true;
        List<Transformer> transformersForClass = decodingHooks.get(clazz);
        if (transformersForClass == null) {
            transformersForClass = new CopyOnWriteArrayList<Transformer>();
            decodingHooks.put(clazz, transformersForClass);
        }
        transformersForClass.add(transformer);
    }

    public static Object applyEncodingHooks(Object objectToEncode) {
        Object transformedObject = objectToEncode;
        if (!BSON.hasEncodeHooks() || objectToEncode == null || encodingHooks.size() == 0) {
            return transformedObject;
        }
        List<Transformer> transformersForObject = encodingHooks.get(objectToEncode.getClass());
        if (transformersForObject != null) {
            for (Transformer transformer : transformersForObject) {
                transformedObject = transformer.transform(objectToEncode);
            }
        }
        return transformedObject;
    }

    public static Object applyDecodingHooks(Object objectToDecode) {
        Object transformedObject = objectToDecode;
        if (!BSON.hasDecodeHooks() || objectToDecode == null || decodingHooks.size() == 0) {
            return transformedObject;
        }
        List<Transformer> transformersForObject = decodingHooks.get(objectToDecode.getClass());
        if (transformersForObject != null) {
            for (Transformer transformer : transformersForObject) {
                transformedObject = transformer.transform(objectToDecode);
            }
        }
        return transformedObject;
    }

    public static List<Transformer> getEncodingHooks(Class<?> clazz) {
        return encodingHooks.get(clazz);
    }

    public static void clearEncodingHooks() {
        encodeHooks = false;
        encodingHooks.clear();
    }

    public static void removeEncodingHooks(Class<?> clazz) {
        encodingHooks.remove(clazz);
    }

    public static void removeEncodingHook(Class<?> clazz, Transformer transformer) {
        BSON.getEncodingHooks(clazz).remove(transformer);
    }

    public static List<Transformer> getDecodingHooks(Class<?> clazz) {
        return decodingHooks.get(clazz);
    }

    public static void clearDecodingHooks() {
        decodeHooks = false;
        decodingHooks.clear();
    }

    public static void removeDecodingHooks(Class<?> clazz) {
        decodingHooks.remove(clazz);
    }

    public static void removeDecodingHook(Class<?> clazz, Transformer transformer) {
        BSON.getDecodingHooks(clazz).remove(transformer);
    }

    public static void clearAllHooks() {
        BSON.clearEncodingHooks();
        BSON.clearDecodingHooks();
    }

    public static byte[] encode(BSONObject doc) {
        return new BasicBSONEncoder().encode(doc);
    }

    public static BSONObject decode(byte[] bytes) {
        return new BasicBSONDecoder().readObject(bytes);
    }

    public static int regexFlags(String s) {
        int flags = 0;
        if (s == null) {
            return flags;
        }
        for (char f : s.toLowerCase().toCharArray()) {
            flags |= BSON.regexFlag(f);
        }
        return flags;
    }

    public static int regexFlag(char c) {
        int flag = FLAG_LOOKUP[c];
        if (flag == 0) {
            throw new IllegalArgumentException(String.format("Unrecognized flag [%c]", Character.valueOf(c)));
        }
        return flag;
    }

    public static String regexFlags(int flags) {
        int processedFlags = flags;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < FLAG_LOOKUP.length; ++i) {
            if ((processedFlags & FLAG_LOOKUP[i]) <= 0) continue;
            buf.append((char)i);
            processedFlags -= FLAG_LOOKUP[i];
        }
        if (processedFlags > 0) {
            throw new IllegalArgumentException("Some flags could not be recognized.");
        }
        return buf.toString();
    }

    public static int toInt(Object number) {
        if (number == null) {
            throw new IllegalArgumentException("Argument shouldn't be null");
        }
        if (number instanceof Number) {
            return ((Number)number).intValue();
        }
        if (number instanceof Boolean) {
            return (Boolean)number != false ? 1 : 0;
        }
        throw new IllegalArgumentException("Can't convert: " + number.getClass().getName() + " to int");
    }

    static {
        BSON.FLAG_LOOKUP[103] = 256;
        BSON.FLAG_LOOKUP[105] = 2;
        BSON.FLAG_LOOKUP[109] = 8;
        BSON.FLAG_LOOKUP[115] = 32;
        BSON.FLAG_LOOKUP[99] = 128;
        BSON.FLAG_LOOKUP[120] = 4;
        BSON.FLAG_LOOKUP[100] = 1;
        BSON.FLAG_LOOKUP[116] = 16;
        BSON.FLAG_LOOKUP[117] = 64;
        encodeHooks = false;
        decodeHooks = false;
        encodingHooks = new ClassMap();
        decodingHooks = new ClassMap();
    }
}

