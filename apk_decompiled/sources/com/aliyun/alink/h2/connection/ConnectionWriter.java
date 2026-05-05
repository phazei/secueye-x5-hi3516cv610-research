package com.aliyun.alink.h2.connection;

import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.api.Http2StreamListener;
import com.aliyun.alink.h2.api.StreamWriteContext;
import io.netty.handler.codec.http2.Http2Headers;

/* JADX INFO: loaded from: classes2.dex */
public interface ConnectionWriter {
    void writeData(int i, byte[] bArr, boolean z, CompletableListener<StreamWriteContext> completableListener);

    void writeGoAway(int i, int i2, byte[] bArr, CompletableListener<Connection> completableListener);

    void writeHeaders(Http2Headers http2Headers, boolean z, Http2StreamListener http2StreamListener, CompletableListener<StreamWriteContext> completableListener);

    void writeRst(int i, int i2, CompletableListener<Connection> completableListener);
}
