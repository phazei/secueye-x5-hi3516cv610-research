package com.aliyun.iot.aep.sdk.apiclient.tracker;

import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestWrapper;

/* JADX INFO: loaded from: classes2.dex */
public interface Tracker {
    void onFailure(IoTRequest ioTRequest, Exception exc);

    void onRawFailure(IoTRequestWrapper ioTRequestWrapper, Exception exc);

    void onRawResponse(IoTRequestWrapper ioTRequestWrapper, IoTResponse ioTResponse);

    void onRealSend(IoTRequestWrapper ioTRequestWrapper);

    void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse);

    void onSend(IoTRequest ioTRequest);
}
