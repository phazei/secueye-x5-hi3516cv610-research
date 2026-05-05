package com.aliyun.alink.apiclient.biz;

/* JADX INFO: loaded from: classes.dex */
public interface IApiClientResponse {
    int getCode();

    Object getData();

    String getId();

    String getLocalizedMsg();

    String getMessage();

    byte[] getRawData();
}
