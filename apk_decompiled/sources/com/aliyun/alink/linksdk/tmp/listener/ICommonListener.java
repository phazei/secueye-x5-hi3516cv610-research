package com.aliyun.alink.linksdk.tmp.listener;

import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface ICommonListener<T> {
    void onFailed(ErrorInfo errorInfo);

    void onSuccess(T t);
}
