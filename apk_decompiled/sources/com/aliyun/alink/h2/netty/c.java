package com.aliyun.alink.h2.netty;

import io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.Http2ConnectionDecoder;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: NettyHttp2HandlerBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends AbstractHttp2ConnectionHandlerBuilder<b, c> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f3888a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f3889b = "debug";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Map<String, LogLevel> f3890c = new HashMap<String, LogLevel>() { // from class: com.aliyun.alink.h2.netty.NettyHttp2HandlerBuilder$1
        {
            put(AgooConstants.MESSAGE_TRACE, LogLevel.TRACE);
            put("debug", LogLevel.DEBUG);
            put("info", LogLevel.INFO);
            put("warn", LogLevel.WARN);
            put("error", LogLevel.ERROR);
        }
    };

    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    protected boolean isServer() {
        return false;
    }

    public c(long j) {
        this.f3888a = j;
        frameLogger(b());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public b build() {
        return (b) super.build();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http2.AbstractHttp2ConnectionHandlerBuilder
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public b build(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder, Http2Settings http2Settings) {
        b bVar = new b(http2ConnectionDecoder, http2ConnectionEncoder, http2Settings, this.f3888a);
        frameListener(bVar);
        initialSettings().pushEnabled(true);
        initialSettings().initialWindowSize(1073741824);
        initialSettings().maxFrameSize(1048576);
        return bVar;
    }

    private Http2FrameLogger b() {
        LogLevel logLevel = this.f3890c.get(this.f3889b.toLowerCase());
        if (logLevel == null) {
            logLevel = LogLevel.INFO;
        }
        InternalLoggerFactory.setDefaultFactory(new e());
        return new Http2FrameLogger(logLevel, (Class<?>) e.class);
    }
}
