package com.aliyun.alink.h2.stream.a;

import com.aliyun.alink.h2.entity.Http2Response;
import com.aliyun.alink.h2.stream.utils.StreamUtil;

/* JADX INFO: compiled from: StreamResponse.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends Http2Response {
    public c(Http2Response http2Response) {
        super(http2Response.getHeaders(), http2Response.getContent());
    }

    public String a() {
        return StreamUtil.getDataStreamId(this.headers);
    }

    public String toString() {
        return this.headers + ", content:[" + new String(this.content) + "]";
    }
}
