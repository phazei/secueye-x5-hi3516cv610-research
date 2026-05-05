package com.aliyun.alink.h2.api;

import com.aliyun.alink.h2.connection.Connection;
import io.netty.handler.codec.http2.Http2Stream;

/* JADX INFO: loaded from: classes2.dex */
public class StreamWriteContext {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Http2Stream f3816a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Connection f3817b;

    public StreamWriteContext(Http2Stream http2Stream, Connection connection) {
        this.f3816a = http2Stream;
        this.f3817b = connection;
    }

    public void writeData(byte[] bArr, boolean z, CompletableListener<StreamWriteContext> completableListener) {
        this.f3817b.writeData(this.f3816a.id(), bArr, z, completableListener);
    }

    public void closeStream() {
        this.f3816a.close();
    }

    public Http2Stream getStream() {
        return this.f3816a;
    }
}
