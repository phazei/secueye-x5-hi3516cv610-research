package com.aliyun.alink.linksdk.tmp.listener;

import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface IDevRawDataListener {
    void onFail(Object obj, ErrorInfo errorInfo);

    void onSuccess(Object obj, Object obj2);
}
