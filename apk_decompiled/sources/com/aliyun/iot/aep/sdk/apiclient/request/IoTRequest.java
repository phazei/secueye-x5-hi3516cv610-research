package com.aliyun.iot.aep.sdk.apiclient.request;

import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public interface IoTRequest {
    String getAPIVersion();

    String getAuthType();

    String getHost();

    String getId();

    Method getMethod();

    String getMockType();

    Map<String, Object> getParams();

    String getPath();

    Scheme getScheme();
}
