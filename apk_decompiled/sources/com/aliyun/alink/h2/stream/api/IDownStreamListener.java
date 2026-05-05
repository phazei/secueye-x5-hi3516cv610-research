package com.aliyun.alink.h2.stream.api;

import io.netty.handler.codec.http2.Http2Headers;
import java.io.IOException;

/* JADX INFO: loaded from: classes2.dex */
public interface IDownStreamListener {
    void onDataRead(String str, byte[] bArr, boolean z);

    void onHeadersRead(String str, Http2Headers http2Headers, boolean z);

    void onStreamError(String str, IOException iOException);
}
