package com.aliyun.alink.h2.stream.api;

import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.api.StreamWriteContext;
import com.aliyun.alink.h2.entity.Http2Request;
import com.aliyun.alink.h2.entity.Http2Response;

/* JADX INFO: loaded from: classes2.dex */
public interface IStreamSender extends IH2FileManager {
    void closeStream(String str, Http2Request http2Request, CompletableListener<Http2Response> completableListener);

    void connect(CompletableListener completableListener);

    void disconnect(CompletableListener completableListener);

    boolean isConnected();

    void openStream(String str, Http2Request http2Request, CompletableListener<Http2Response> completableListener);

    void sendStream(String str, Http2Request http2Request, IDownStreamListener iDownStreamListener, CompletableListener<StreamWriteContext> completableListener);
}
