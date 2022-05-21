/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonWriter
 */
package cc.fyre.potpvp.kittype;

import cc.fyre.potpvp.kittype.KitType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public final class KitTypeJsonAdapter
extends TypeAdapter<KitType> {
    public void write(JsonWriter writer, KitType type2) throws IOException {
        writer.value(type2.getId());
    }

    public KitType read(JsonReader reader) throws IOException {
        return KitType.byId(reader.nextString());
    }
}

