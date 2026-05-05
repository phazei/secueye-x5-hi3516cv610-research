package com.aliyun.iot.aep.sdk.apiclient.callback;

/* JADX INFO: loaded from: classes2.dex */
public class IoTResponseImpl implements IoTResponse {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f4568a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f4569b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f4570c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f4571d;
    public Object e;
    public byte[] f;
    public Object g = null;

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public int getCode() {
        return this.f4569b;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public Object getData() {
        return this.e;
    }

    public Object getExtraResponseData() {
        return this.g;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public String getId() {
        return this.f4568a;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public String getLocalizedMsg() {
        return this.f4571d;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public String getMessage() {
        return this.f4570c;
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse
    public byte[] getRawData() {
        return this.f;
    }

    public void setCode(int i) {
        this.f4569b = i;
    }

    public void setData(Object obj) {
        this.e = obj;
    }

    public void setExtraResponseData(Object obj) {
        this.g = obj;
    }

    public void setId(String str) {
        this.f4568a = str;
    }

    public void setLocalizedMsg(String str) {
        this.f4571d = str;
    }

    public void setMessage(String str) {
        this.f4570c = str;
    }

    public void setRawData(byte[] bArr) {
        this.f = bArr;
    }
}
