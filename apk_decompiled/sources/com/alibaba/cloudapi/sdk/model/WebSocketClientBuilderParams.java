package com.alibaba.cloudapi.sdk.model;

/* JADX INFO: loaded from: classes.dex */
public class WebSocketClientBuilderParams extends BaseClientInitialParam {
    ApiWebSocketListner apiWebSocketListner;
    int requestExpiredTime = 10000;
    int callbackThreadPoolCount = 1;

    public ApiWebSocketListner getApiWebSocketListner() {
        return this.apiWebSocketListner;
    }

    public void setApiWebSocketListner(ApiWebSocketListner apiWebSocketListner) {
        this.apiWebSocketListner = apiWebSocketListner;
    }

    public int getRequestExpiredTime() {
        return this.requestExpiredTime;
    }

    public void setRequestExpiredTime(int i) {
        this.requestExpiredTime = i;
    }

    public int getCallbackThreadPoolCount() {
        return this.callbackThreadPoolCount;
    }

    public void setCallbackThreadPoolCount(int i) {
        this.callbackThreadPoolCount = i;
    }
}
