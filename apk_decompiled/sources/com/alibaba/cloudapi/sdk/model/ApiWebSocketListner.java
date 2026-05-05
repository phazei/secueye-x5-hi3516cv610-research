package com.alibaba.cloudapi.sdk.model;

/* JADX INFO: loaded from: classes.dex */
public interface ApiWebSocketListner {
    void onFailure(Throwable th, ApiResponse apiResponse);

    void onNotify(String str);
}
