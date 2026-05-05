package com.aliyun.alink.h2.stream.entity;

import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Headers;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class StreamData {
    int dataSize;
    int streamId;
    List<byte[]> data = new ArrayList();
    Http2Headers headers = new DefaultHttp2Headers();

    public StreamData(int i) {
        this.streamId = i;
    }

    public void addData(byte[] bArr) {
        this.data.add(bArr);
        this.dataSize += bArr.length;
    }

    public byte[] readAllData() {
        int i = this.dataSize;
        if (i == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        for (byte[] bArr : this.data) {
            byteArrayOutputStream.write(bArr, 0, bArr.length);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void addHeaders(Http2Headers http2Headers) {
        this.headers.add(http2Headers);
    }

    public void clearData() {
        this.data.clear();
    }

    public Http2Headers getHeaders() {
        return this.headers;
    }

    public String toString() {
        return "headers: " + this.headers + ", content: " + new String(readAllData());
    }
}
