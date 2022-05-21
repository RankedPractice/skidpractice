/*
 * Decompiled with CFR 0.152.
 */
package org.bson.json;

import org.bson.json.JsonParseException;

class JsonBuffer {
    private final String buffer;
    private int position;
    private boolean eof;

    JsonBuffer(String buffer) {
        this.buffer = buffer;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int read() {
        if (this.eof) {
            throw new JsonParseException("Trying to read past EOF.");
        }
        if (this.position >= this.buffer.length()) {
            this.eof = true;
            return -1;
        }
        return this.buffer.charAt(this.position++);
    }

    public void unread(int c) {
        this.eof = false;
        if (c != -1 && this.buffer.charAt(this.position - 1) == c) {
            --this.position;
        }
    }

    public String substring(int beginIndex) {
        return this.buffer.substring(beginIndex);
    }

    public String substring(int beginIndex, int endIndex) {
        return this.buffer.substring(beginIndex, endIndex);
    }
}

