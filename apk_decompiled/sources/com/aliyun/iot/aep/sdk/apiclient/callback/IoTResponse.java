package com.aliyun.iot.aep.sdk.apiclient.callback;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTResponse {
    int getCode();

    Object getData();

    String getId();

    String getLocalizedMsg();

    String getMessage();

    byte[] getRawData();
}
