/*
 * Decompiled with CFR 0.152.
 */
package cc.fyre.potpvp.util;

import java.util.LinkedList;
import java.util.List;

public class ListWrapper<T> {
    private List<T> backingList;

    public List<T> ensure() {
        return this.isPresent() ? this.backingList : (this.backingList = new LinkedList<T>());
    }

    public boolean isPresent() {
        return this.backingList != null;
    }
}

