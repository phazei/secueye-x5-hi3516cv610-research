package com.aliyun.iot.aep.sdk.apiclient.callback;

import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTCallback {
    void onFailure(IoTRequest ioTRequest, Exception exc);

    void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse);
}
