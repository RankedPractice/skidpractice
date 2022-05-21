/*
 * Decompiled with CFR 0.152.
 */
package org.bson;

import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

public class BsonDbPointer
extends BsonValue {
    private final String namespace;
    private final ObjectId id;

    public BsonDbPointer(String namespace, ObjectId id) {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace can not be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("id can not be null");
        }
        this.namespace = namespace;
        this.id = id;
    }

    @Override
    public BsonType getBsonType() {
        return BsonType.DB_POINTER;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public ObjectId getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BsonDbPointer dbPointer = (BsonDbPointer)o;
        if (!this.id.equals(dbPointer.id)) {
            return false;
        }
        return this.namespace.equals(dbPointer.namespace);
    }

    public int hashCode() {
        int result2 = this.namespace.hashCode();
        result2 = 31 * result2 + this.id.hashCode();
        return result2;
    }

    public String toString() {
        return "BsonDbPointer{namespace='" + this.namespace + '\'' + ", id=" + this.id + '}';
    }
}

