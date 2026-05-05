package com.aliyun.alink.apiclient.biz.a;

import com.aliyun.alink.apiclient.biz.IApiClientResponse;

/* JADX INFO: compiled from: ApiClientResponseImpl.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements IApiClientResponse {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f3235a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f3236b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f3237c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f3238d;
    private Object e;
    private byte[] f;

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public String getId() {
        return this.f3235a;
    }

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public int getCode() {
        return this.f3236b;
    }

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public String getMessage() {
        return this.f3237c;
    }

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public String getLocalizedMsg() {
        return this.f3238d;
    }

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public Object getData() {
        return this.e;
    }

    @Override // com.aliyun.alink.apiclient.biz.IApiClientResponse
    public byte[] getRawData() {
        return this.f;
    }

    public void a(String str) {
        this.f3235a = str;
    }

    public void a(int i) {
        this.f3236b = i;
    }

    public void b(String str) {
        this.f3237c = str;
    }

    public void c(String str) {
        this.f3238d = str;
    }

    public void a(Object obj) {
        this.e = obj;
    }

    public void a(byte[] bArr) {
        this.f = bArr;
    }

    public String toString() {
        return "{\"id\":\"" + this.f3235a + "\",\"code\":\"" + this.f3236b + "\",\"message\":\"" + this.f3237c + "\",\"localizedMsg\":\"" + this.f3238d + "\",\"data\":\"" + this.e + "\",\"rawData\":\"" + this.f + "\"}";
    }
}
