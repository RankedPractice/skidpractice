/*
 * Decompiled with CFR 0.152.
 */
package org.bson.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.bson.util.CopyOnWriteMap;

class ClassAncestry {
    private static final ConcurrentMap<Class<?>, List<Class<?>>> _ancestryCache = CopyOnWriteMap.newHashMap();

    ClassAncestry() {
    }

    public static <T> List<Class<?>> getAncestry(Class<T> c) {
        ConcurrentMap<Class<?>, List<Class<?>>> cache2 = ClassAncestry.getClassAncestryCache();
        List cachedResult;
        while ((cachedResult = (List)cache2.get(c)) == null) {
            cache2.putIfAbsent(c, ClassAncestry.computeAncestry(c));
        }
        return cachedResult;
    }

    private static List<Class<?>> computeAncestry(Class<?> c) {
        ArrayList result2 = new ArrayList();
        result2.add(Object.class);
        ClassAncestry.computeAncestry(c, result2);
        Collections.reverse(result2);
        return Collections.unmodifiableList(new ArrayList(result2));
    }

    private static <T> void computeAncestry(Class<T> c, List<Class<?>> result2) {
        if (c == null || c == Object.class) {
            return;
        }
        Class<?>[] interfaces = c.getInterfaces();
        for (int i = interfaces.length - 1; i >= 0; --i) {
            ClassAncestry.computeAncestry(interfaces[i], result2);
        }
        ClassAncestry.computeAncestry(c.getSuperclass(), result2);
        if (!result2.contains(c)) {
            result2.add(c);
        }
    }

    private static ConcurrentMap<Class<?>, List<Class<?>>> getClassAncestryCache() {
        return _ancestryCache;
    }
}

