package com.aliyun.alink.apiclient;

/* JADX INFO: loaded from: classes.dex */
public class IoTAPIClientFactory {
    public IoTApiClient getClient() {
        return IoTAPIClientImpl.getInstance();
    }
}
