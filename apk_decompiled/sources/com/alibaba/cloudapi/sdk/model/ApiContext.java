package com.alibaba.cloudapi.sdk.model;

import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class ApiContext {
    ApiCallback callback;
    ApiRequest request;
    long startTime = new Date().getTime();

    public ApiContext(ApiCallback apiCallback, ApiRequest apiRequest) {
        this.callback = apiCallback;
        this.request = apiRequest;
    }

    public ApiCallback getCallback() {
        return this.callback;
    }

    public void setCallback(ApiCallback apiCallback) {
        this.callback = apiCallback;
    }

    public ApiRequest getRequest() {
        return this.request;
    }

    public void setRequest(ApiRequest apiRequest) {
        this.request = apiRequest;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long j) {
        this.startTime = j;
    }
}
