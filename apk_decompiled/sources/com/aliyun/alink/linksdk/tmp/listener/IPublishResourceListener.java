package com.aliyun.alink.linksdk.tmp.listener;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IPublishResourceListener {
    void onError(String str, AError aError);

    void onSuccess(String str, Object obj);
}
