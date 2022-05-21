/*
 * Decompiled with CFR 0.152.
 */
package org.bson.json;

public class JsonParseException
extends RuntimeException {
    private static final long serialVersionUID = -6722022620020198727L;

    public JsonParseException() {
    }

    public JsonParseException(String s) {
        super(s);
    }

    public JsonParseException(String pattern, Object ... args2) {
        super(String.format(pattern, args2));
    }

    public JsonParseException(Throwable t) {
        super(t);
    }
}

