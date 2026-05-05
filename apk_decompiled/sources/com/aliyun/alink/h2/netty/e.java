package com.aliyun.alink.h2.netty;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* JADX INFO: compiled from: NettyHttp2LoggerFactory.java */
/* JADX INFO: loaded from: classes2.dex */
public class e extends InternalLoggerFactory {
    @Override // io.netty.util.internal.logging.InternalLoggerFactory
    protected InternalLogger newInstance(String str) {
        return new NettyHttp2Logger("NettyLog");
    }
}
