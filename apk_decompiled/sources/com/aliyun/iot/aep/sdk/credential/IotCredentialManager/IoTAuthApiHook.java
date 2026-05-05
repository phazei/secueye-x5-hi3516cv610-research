package com.aliyun.iot.aep.sdk.credential.IotCredentialManager;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTAuthApiHook {
    void onInterceptResponse(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTResponse ioTResponse);

    void onInterceptSend(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload);
}
