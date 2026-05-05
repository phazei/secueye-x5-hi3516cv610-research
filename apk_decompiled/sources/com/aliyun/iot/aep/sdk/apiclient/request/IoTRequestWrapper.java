package com.aliyun.iot.aep.sdk.apiclient.request;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes2.dex */
public class IoTRequestWrapper {
    public IoTCallback callback;
    public IoTRequestPayload payload;
    public IoTRequest request;

    public byte[] buildBody() {
        try {
            return JSON.toJSONString(this.payload).getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return new byte[0];
        }
    }
}
