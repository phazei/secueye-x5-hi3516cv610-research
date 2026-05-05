package com.aliyun.alink.h2.entity;

import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Headers;

/* JADX INFO: loaded from: classes2.dex */
public class BaseHttpEntity {
    public static final String REQUEST_ID = "x-request-id";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f3881a;
    protected byte[] content;
    protected Http2Headers headers;

    public BaseHttpEntity() {
        this.f3881a = 0;
        this.headers = new DefaultHttp2Headers();
    }

    public BaseHttpEntity(Http2Headers http2Headers, byte[] bArr) {
        this.f3881a = 0;
        this.headers = http2Headers;
        this.content = bArr;
    }

    public Http2Headers getHeaders() {
        return this.headers;
    }

    public void setHeaders(Http2Headers http2Headers) {
        this.headers = http2Headers;
    }

    public String getRequestId() {
        return this.headers.get(REQUEST_ID).toString();
    }

    public void setRequestId(String str) {
        this.headers.set(REQUEST_ID, str);
    }

    public void setContent(byte[] bArr) {
        this.content = bArr;
    }

    public byte[] getContent() {
        return this.content;
    }

    public int getH2StreamId() {
        return this.f3881a;
    }

    public void setH2StreamId(int i) {
        this.f3881a = i;
    }
}
