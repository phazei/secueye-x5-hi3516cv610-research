package com.aliyun.iot.aep.sdk.apiclient.callback;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class IoTJsonCallback implements IoTCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public IoTCallback f4566a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Class f4567b;

    public IoTJsonCallback(IoTCallback ioTCallback, Class cls) {
        this.f4566a = ioTCallback;
        this.f4567b = cls;
    }

    public final Object a(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        if (ioTResponse.getData() instanceof JSONObject) {
            return JSON.parseObject(ioTResponse.getData().toString(), this.f4567b);
        }
        if (ioTResponse.getData() instanceof JSONArray) {
            return JSON.parseArray(ioTResponse.getData().toString(), this.f4567b);
        }
        if (ioTResponse.getData() != null) {
            return ioTResponse.getData().toString();
        }
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onFailure(IoTRequest ioTRequest, Exception exc) {
        IoTCallback ioTCallback = this.f4566a;
        if (ioTCallback != null) {
            ioTCallback.onFailure(ioTRequest, exc);
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
        Object objA = a(ioTRequest, ioTResponse);
        IoTResponseImpl ioTResponseImpl = (IoTResponseImpl) ioTResponse;
        ioTResponseImpl.setData(objA);
        IoTCallback ioTCallback = this.f4566a;
        if (ioTCallback != null) {
            ioTCallback.onResponse(ioTRequest, ioTResponseImpl);
        }
    }
}
