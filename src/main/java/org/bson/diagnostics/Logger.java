/*
 * Decompiled with CFR 0.152.
 */
package org.bson.diagnostics;

public interface Logger {
    public String getName();

    public boolean isTraceEnabled();

    public void trace(String var1);

    public void trace(String var1, Throwable var2);

    public boolean isDebugEnabled();

    public void debug(String var1);

    public void debug(String var1, Throwable var2);

    public boolean isInfoEnabled();

    public void info(String var1);

    public void info(String var1, Throwable var2);

    public boolean isWarnEnabled();

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public boolean isErrorEnabled();

    public void error(String var1);

    public void error(String var1, Throwable var2);
}

