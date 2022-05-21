/*
 * Decompiled with CFR 0.152.
 */
package org.bson.codecs.configuration;

import java.util.NoSuchElementException;

abstract class Optional<T> {
    private static final Optional<Object> NONE = new Optional<Object>(){

        @Override
        public Object get() {
            throw new NoSuchElementException(".get call on None!");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    Optional() {
    }

    public static <T> Optional<T> empty() {
        return NONE;
    }

    public static <T> Optional<T> of(T it) {
        if (it == null) {
            return NONE;
        }
        return new Some<T>(it);
    }

    public abstract T get();

    public abstract boolean isEmpty();

    public String toString() {
        return "None";
    }

    public boolean isDefined() {
        return !this.isEmpty();
    }

    public static class Some<T>
    extends Optional<T> {
        private final T value;

        Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            return String.format("Some(%s)", this.value);
        }
    }
}

