package com.aliyun.alink.linksdk.tmp.api;

import com.aliyun.alink.linksdk.tmp.listener.IDevListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IProvision {
    void provisionInit(Object obj, IDevListener iDevListener);

    boolean setConfiData(Object obj, Object obj2, IDevListener iDevListener);

    void unInit();
}
