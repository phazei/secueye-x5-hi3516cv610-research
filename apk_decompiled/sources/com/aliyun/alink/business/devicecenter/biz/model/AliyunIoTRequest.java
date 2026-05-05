package com.aliyun.alink.business.devicecenter.biz.model;

import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class AliyunIoTRequest implements IoTRequest, IRequest, Serializable {
    public String apiVersion;
    public String authType;
    public Map<String, Object> params = new HashMap();
    public String path;

    public void addParam(String str, Object obj) {
        this.params.put(str, obj);
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getAPIVersion() {
        return this.apiVersion;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getAuthType() {
        return this.authType;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getHost() {
        return null;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getId() {
        return UUID.randomUUID().toString();
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
        return this.params;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public String getPath() {
        return this.path;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
    public Scheme getScheme() {
        return Scheme.HTTPS;
    }

    public void setApiVersion(String str) {
        this.apiVersion = str;
    }

    public void setAuthType(String str) {
        this.authType = str;
    }

    public void setParams(Map<String, Object> map) {
        this.params = map;
    }

    public void setPath(String str) {
        this.path = str;
    }
}
