package com.aliyun.iot.aep.sdk.apiclient;

/* JADX INFO: loaded from: classes2.dex */
public class IoTAPIClientFactory {
    public IoTAPIClient getClient() {
        return IoTAPIClientImpl.getInstance();
    }
}
