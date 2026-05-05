package com.aliyun.alink.h2.netty;

import io.netty.util.internal.logging.AbstractInternalLogger;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/* JADX INFO: loaded from: classes2.dex */
public class NettyHttp2Logger extends AbstractInternalLogger {
    static final String SELF = "com.aliyun.alink.h2.netty.NettyHttp2Logger";
    private Level logLevel;

    protected NettyHttp2Logger(String str) {
        super(str);
        this.logLevel = Level.FINEST;
    }

    public Level getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(Level level) {
        this.logLevel = level;
    }

    private boolean isLoggable(Level level) {
        return this.logLevel.intValue() <= level.intValue();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isTraceEnabled() {
        return isLoggable(Level.FINEST);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String str) {
        if (isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, str, (Throwable) null);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String str, Object obj) {
        if (isLoggable(Level.FINEST)) {
            a aVarA = f.a(str, obj);
            log(SELF, Level.FINEST, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String str, Object obj, Object obj2) {
        if (isLoggable(Level.FINEST)) {
            a aVarA = f.a(str, obj, obj2);
            log(SELF, Level.FINEST, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String str, Object... objArr) {
        if (isLoggable(Level.FINEST)) {
            a aVarA = f.a(str, objArr);
            log(SELF, Level.FINEST, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String str, Throwable th) {
        if (isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, str, th);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isDebugEnabled() {
        return isLoggable(Level.FINE);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String str) {
        if (isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, str, (Throwable) null);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String str, Object obj) {
        if (isLoggable(Level.FINE)) {
            a aVarA = f.a(str, obj);
            log(SELF, Level.FINE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String str, Object obj, Object obj2) {
        if (isLoggable(Level.FINE)) {
            a aVarA = f.a(str, obj, obj2);
            log(SELF, Level.FINE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String str, Object... objArr) {
        if (isLoggable(Level.FINE)) {
            a aVarA = f.a(str, objArr);
            log(SELF, Level.FINE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String str, Throwable th) {
        if (isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, str, th);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isInfoEnabled() {
        return isLoggable(Level.INFO);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String str) {
        if (isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, str, (Throwable) null);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String str, Object obj) {
        if (isLoggable(Level.INFO)) {
            a aVarA = f.a(str, obj);
            log(SELF, Level.INFO, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String str, Object obj, Object obj2) {
        if (isLoggable(Level.INFO)) {
            a aVarA = f.a(str, obj, obj2);
            log(SELF, Level.INFO, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String str, Object... objArr) {
        if (isLoggable(Level.INFO)) {
            a aVarA = f.a(str, objArr);
            log(SELF, Level.INFO, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String str, Throwable th) {
        if (isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, str, th);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isWarnEnabled() {
        return isLoggable(Level.WARNING);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String str) {
        if (isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, str, (Throwable) null);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String str, Object obj) {
        if (isLoggable(Level.WARNING)) {
            a aVarA = f.a(str, obj);
            log(SELF, Level.WARNING, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String str, Object obj, Object obj2) {
        if (isLoggable(Level.WARNING)) {
            a aVarA = f.a(str, obj, obj2);
            log(SELF, Level.WARNING, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String str, Object... objArr) {
        if (isLoggable(Level.WARNING)) {
            a aVarA = f.a(str, objArr);
            log(SELF, Level.WARNING, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String str, Throwable th) {
        if (isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, str, th);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isErrorEnabled() {
        return isLoggable(Level.SEVERE);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String str) {
        if (isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, str, (Throwable) null);
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String str, Object obj) {
        if (isLoggable(Level.SEVERE)) {
            a aVarA = f.a(str, obj);
            log(SELF, Level.SEVERE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String str, Object obj, Object obj2) {
        if (isLoggable(Level.SEVERE)) {
            a aVarA = f.a(str, obj, obj2);
            log(SELF, Level.SEVERE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String str, Object... objArr) {
        if (isLoggable(Level.SEVERE)) {
            a aVarA = f.a(str, objArr);
            log(SELF, Level.SEVERE, aVarA.a(), aVarA.b());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String str, Throwable th) {
        if (isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, str, th);
        }
    }

    private void log(String str, Level level, String str2, Throwable th) {
        LogRecord logRecord = new LogRecord(level, str2);
        logRecord.setLoggerName(name());
        logRecord.setThrown(th);
        if (level == Level.SEVERE) {
            com.aliyun.alink.h2.b.a.d(str, str2 + th);
            return;
        }
        if (level == Level.WARNING) {
            com.aliyun.alink.h2.b.a.c(str, str2 + th);
            return;
        }
        if (level == Level.FINE || level == Level.FINER || level == Level.FINEST || level == Level.ALL) {
            com.aliyun.alink.h2.b.a.a(str, str2 + th);
            return;
        }
        if (level == Level.CONFIG || level == Level.INFO) {
            com.aliyun.alink.h2.b.a.b(str, str2 + th);
        }
    }
}
