package com.aliyun.alink.apiclient;

/* JADX INFO: loaded from: classes.dex */
public interface IoTCallback {
    void onFailure(CommonRequest commonRequest, Exception exc);

    void onResponse(CommonRequest commonRequest, CommonResponse commonResponse);
}
