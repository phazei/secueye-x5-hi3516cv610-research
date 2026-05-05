package com.aliyun.alink.business.devicecenter.biz.model;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.io.Serializable;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AliyunCheckBindTokenRequest implements IRequest, IoTRequest, Serializable {
    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getAPIVersion() {
        return "1.0.1";
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getAuthType() {
        return AlinkConstants.KEY_IOT_AUTH;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getHost() {
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getId() {
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public Method getMethod() {
        return Method.POST;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getMockType() {
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public Map<String, Object> getParams() {
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getPath() {
        return AlinkConstants.HTTP_PATH_ILOP_CHECK_BIND_TOKEN;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public Scheme getScheme() {
        return Scheme.HTTPS;
    }
}
