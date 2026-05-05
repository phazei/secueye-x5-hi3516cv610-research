package com.aliyun.alink.h2.entity;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.Http2Headers;

/* JADX INFO: loaded from: classes2.dex */
public class Http2Response extends BaseHttpEntity {
    public HttpResponseStatus getStatus() {
        return HttpResponseStatus.parseLine(this.headers.status());
    }

    public Http2Response(Http2Headers http2Headers, byte[] bArr) {
        super(http2Headers, bArr);
    }

    public Http2Response() {
    }
}
