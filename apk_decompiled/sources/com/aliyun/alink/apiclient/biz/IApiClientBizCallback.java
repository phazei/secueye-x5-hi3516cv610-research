package com.aliyun.alink.apiclient.biz;

/* JADX INFO: loaded from: classes.dex */
public interface IApiClientBizCallback {
    void onFail(Exception exc);

    void onResponse(IApiClientResponse iApiClientResponse);
}
