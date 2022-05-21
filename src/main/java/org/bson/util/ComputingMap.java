/*
 * Decompiled with CFR 0.152.
 */
package org.bson.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.bson.assertions.Assertions;
import org.bson.util.CopyOnWriteMap;
import org.bson.util.Function;

final class ComputingMap<K, V>
implements Map<K, V>,
Function<K, V> {
    private final ConcurrentMap<K, V> map;
    private final Function<K, V> function;

    public static <K, V> Map<K, V> create(Function<K, V> function) {
        return new ComputingMap(CopyOnWriteMap.newHashMap(), function);
    }

    ComputingMap(ConcurrentMap<K, V> map, Function<K, V> function) {
        this.map = Assertions.notNull("map", map);
        this.function = Assertions.notNull("function", function);
    }

    @Override
    public V get(Object key) {
        Object v;
        while ((v = this.map.get(key)) == null) {
            Object k = key;
            V value = this.function.apply(k);
            if (value == null) {
                return null;
            }
            this.map.putIfAbsent(k, value);
        }
        return v;
    }

    @Override
    public V apply(K k) {
        return this.get(k);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return this.map.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.map.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return this.map.replace(key, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        return this.map.replace(key, value);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this.map.equals(o);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}

