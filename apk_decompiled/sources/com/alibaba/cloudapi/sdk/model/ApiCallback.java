package com.alibaba.cloudapi.sdk.model;

/* JADX INFO: loaded from: classes.dex */
public interface ApiCallback {
    void onFailure(ApiRequest apiRequest, Exception exc);

    void onResponse(ApiRequest apiRequest, ApiResponse apiResponse);
}
