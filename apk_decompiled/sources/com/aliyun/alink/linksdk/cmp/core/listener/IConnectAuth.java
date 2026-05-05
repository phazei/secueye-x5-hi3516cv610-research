package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectAuth<T> {
    void onAuth(T t);

    void onPrepareAuthFail(AError aError);
}
