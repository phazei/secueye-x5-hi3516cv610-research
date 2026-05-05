package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectRrpcListener {
    void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle);

    void onResponseFailed(ARequest aRequest, AError aError);

    void onResponseSuccess(ARequest aRequest);

    void onSubscribeFailed(ARequest aRequest, AError aError);

    void onSubscribeSuccess(ARequest aRequest);
}
