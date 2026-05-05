package com.aliyun.alink.h2.api;

import com.aliyun.alink.h2.connection.Connection;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Stream;
import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public interface Http2StreamListener {
    void onDataRead(Connection connection, Http2Stream http2Stream, byte[] bArr, boolean z);

    void onHeadersRead(Connection connection, Http2Stream http2Stream, Http2Headers http2Headers, boolean z);

    void onStreamError(Connection connection, Http2Stream http2Stream, IOException iOException);
}
