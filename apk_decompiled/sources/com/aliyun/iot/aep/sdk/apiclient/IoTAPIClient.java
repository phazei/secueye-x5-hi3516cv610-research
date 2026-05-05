package com.aliyun.iot.aep.sdk.apiclient;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTAPIClient {
    void send(IoTRequest ioTRequest, IoTCallback ioTCallback);
}
