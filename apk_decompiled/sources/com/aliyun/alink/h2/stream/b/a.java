package com.aliyun.alink.h2.stream.b;

import com.aliyun.alink.h2.api.Http2StreamListener;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.stream.entity.StreamData;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Stream;
import java.io.IOException;

/* JADX INFO: compiled from: AbstractHttp2ResponseListener.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class a implements Http2StreamListener {
    public abstract void a(Connection connection, Http2Stream http2Stream, StreamData streamData);

    @Override // com.aliyun.alink.h2.api.Http2StreamListener
    public void onDataRead(Connection connection, Http2Stream http2Stream, byte[] bArr, boolean z) {
        com.aliyun.alink.h2.stream.c.a.a("AbstractHttp2StreamData", "receive data on connection " + connection + ", streamId " + http2Stream.id());
        StreamData streamDataA = a(connection, http2Stream);
        if (streamDataA == null) {
            onStreamError(connection, http2Stream, new IOException(connection.toString() + " received data frame on " + http2Stream.id() + ", but headers hasn't received"));
            return;
        }
        streamDataA.addData(bArr);
        if (z) {
            a(connection, http2Stream, streamDataA);
            http2Stream.closeLocalSide();
        }
    }

    @Override // com.aliyun.alink.h2.api.Http2StreamListener
    public void onHeadersRead(Connection connection, Http2Stream http2Stream, Http2Headers http2Headers, boolean z) {
        Http2Connection.PropertyKey propertyKey = connection.getPropertyKey("data_cache_key");
        StreamData streamDataA = a(connection, http2Stream);
        if (streamDataA == null) {
            streamDataA = new StreamData(http2Stream.id());
            http2Stream.setProperty(propertyKey, streamDataA);
        }
        streamDataA.addHeaders(http2Headers);
        if (z) {
            a(connection, http2Stream, streamDataA);
            http2Stream.closeLocalSide();
        }
    }

    protected StreamData a(Connection connection, Http2Stream http2Stream) {
        return (StreamData) http2Stream.getProperty(connection.getPropertyKey("data_cache_key"));
    }
}
