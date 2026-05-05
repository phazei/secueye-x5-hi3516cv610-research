package com.alibaba.cloudapi.sdk.client;

import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseApiClient {
    String appKey;
    String appSecret;
    String host;
    boolean isInit = false;
    Scheme scheme;

    protected abstract void sendAsyncRequest(ApiRequest apiRequest, ApiCallback apiCallback);

    protected abstract ApiResponse sendSyncRequest(ApiRequest apiRequest);

    protected void checkIsInit() {
        if (!this.isInit) {
            throw new SdkException("MUST initial client before using");
        }
    }
}
