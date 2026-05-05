package com.aliyun.alink.h2.stream.a;

import com.aliyun.alink.h2.api.Http2StreamListener;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.stream.api.IDownStreamListener;
import com.aliyun.alink.h2.stream.utils.StreamUtil;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Stream;
import java.io.IOException;

/* JADX INFO: compiled from: DownStreamListenerAdapter.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements Http2StreamListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    IDownStreamListener f3902a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    String f3903b;

    protected a(IDownStreamListener iDownStreamListener) {
        this.f3902a = iDownStreamListener;
    }

    @Override // com.aliyun.alink.h2.api.Http2StreamListener
    public void onDataRead(Connection connection, Http2Stream http2Stream, byte[] bArr, boolean z) {
        IDownStreamListener iDownStreamListener = this.f3902a;
        if (iDownStreamListener != null) {
            iDownStreamListener.onDataRead(this.f3903b, bArr, z);
        }
    }

    @Override // com.aliyun.alink.h2.api.Http2StreamListener
    public void onHeadersRead(Connection connection, Http2Stream http2Stream, Http2Headers http2Headers, boolean z) {
        this.f3903b = StreamUtil.getDataStreamId(http2Headers);
        IDownStreamListener iDownStreamListener = this.f3902a;
        if (iDownStreamListener != null) {
            iDownStreamListener.onHeadersRead(this.f3903b, http2Headers, z);
        }
    }

    @Override // com.aliyun.alink.h2.api.Http2StreamListener
    public void onStreamError(Connection connection, Http2Stream http2Stream, IOException iOException) {
        IDownStreamListener iDownStreamListener = this.f3902a;
        if (iDownStreamListener != null) {
            iDownStreamListener.onStreamError(this.f3903b, iOException);
        }
    }
}
