package com.aliyun.alink.linksdk.channel.core.persistent;

import com.aliyun.alink.linksdk.channel.core.base.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IOnSubscribeRrpcListener {
    boolean needUISafety();

    void onReceived(String str, PersistentRequest persistentRequest, IOnRrpcResponseHandle iOnRrpcResponseHandle);

    void onResponseFailed(String str, AError aError);

    void onResponseSuccess(String str);

    void onSubscribeFailed(String str, AError aError);

    void onSubscribeSuccess(String str);
}
