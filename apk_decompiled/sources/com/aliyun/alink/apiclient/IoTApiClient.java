package com.aliyun.alink.apiclient;

/* JADX INFO: loaded from: classes.dex */
public interface IoTApiClient {
    void init(InitializeConfig initializeConfig);

    void send(CommonRequest commonRequest, IoTCallback ioTCallback);
}
