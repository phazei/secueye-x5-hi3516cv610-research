package com.aliyun.alink.h2.stream.api;

import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.entity.Http2Request;
import com.aliyun.alink.h2.entity.Http2Response;

/* JADX INFO: loaded from: classes2.dex */
public interface IH2FileManager {
    void upload(String str, String str2, Http2Request http2Request, CompletableListener<Http2Response> completableListener);
}
