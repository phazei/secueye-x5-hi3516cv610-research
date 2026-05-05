package com.aliyun.iot.aep.sdk.apiclient.hook;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTAPIHook {
    void onInterceptFailure(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, Exception exc, IoTCallback ioTCallback);

    void onInterceptResponse(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTResponse ioTResponse, IoTCallback ioTCallback);

    void onInterceptSend(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, IoTRequestPayloadCallback ioTRequestPayloadCallback);
}
