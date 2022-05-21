/*
 * Decompiled with CFR 0.152.
 */
package org.bson.diagnostics;

import java.util.logging.Level;
import org.bson.diagnostics.Logger;

class JULLogger
implements Logger {
    private final java.util.logging.Logger delegate;

    JULLogger(String name) {
        this.delegate = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(Level.FINER);
    }

    @Override
    public void trace(String msg) {
        this.log(Level.FINER, msg);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.log(Level.FINER, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(Level.FINE);
    }

    @Override
    public void debug(String msg) {
        this.log(Level.FINE, msg);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.log(Level.FINE, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate.isLoggable(Level.INFO);
    }

    @Override
    public void info(String msg) {
        this.log(Level.INFO, msg);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.log(Level.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String msg) {
        this.log(Level.WARNING, msg);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.log(Level.WARNING, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String msg) {
        this.log(Level.SEVERE, msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.log(Level.SEVERE, msg, t);
    }

    private boolean isEnabled(Level level) {
        return this.delegate.isLoggable(level);
    }

    private void log(Level level, String msg) {
        this.delegate.log(level, msg);
    }

    public void log(Level level, String msg, Throwable t) {
        this.delegate.log(level, msg, t);
    }
}

