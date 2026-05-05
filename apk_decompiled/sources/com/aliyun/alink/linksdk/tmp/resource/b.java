package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;

/* JADX INFO: compiled from: ITResRequestInnerHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public interface b extends IDevListener {
    void onProcess(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback);
}
