package com.aliyun.iot.aep.sdk.framework.network;

import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.framework.network.BaseRequest;
import java.io.Serializable;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class ILopRequest<V extends BaseRequest> implements Serializable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    V f4703a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Scheme f4704b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4705c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f4706d;
    private String e;
    private String f;

    ILopRequest(V v, String str, String str2) {
        this.f4704b = Scheme.HTTPS;
        this.f4705c = "";
        this.e = "1.0.4";
        this.f = "";
        this.f4703a = v;
        this.f4706d = str;
        this.e = str2;
    }

    ILopRequest(V v, String str, String str2, String str3, String str4) {
        this.f4704b = Scheme.HTTPS;
        this.f4705c = "";
        this.e = "1.0.4";
        this.f = "";
        this.f4703a = v;
        this.f4706d = str2;
        this.e = str3;
        this.f4705c = str;
        this.f = str4;
    }

    public Scheme getScheme() {
        return this.f4704b;
    }

    public void setScheme(Scheme scheme) {
        this.f4704b = scheme;
    }

    public String getHost() {
        return this.f4705c;
    }

    public void setHost(String str) {
        this.f4705c = str;
    }

    public String getPath() {
        return this.f4706d;
    }

    public void setPath(String str) {
        this.f4706d = str;
    }

    public String getApiVersion() {
        return this.e;
    }

    public void setApiVersion(String str) {
        this.e = str;
    }

    public V getData() {
        return this.f4703a;
    }

    public void setData(V v) {
        this.f4703a = v;
    }

    public Map<String, Object> getParams() {
        if (getData() == null) {
            return null;
        }
        return getData().getParams();
    }

    public String getAuthType() {
        return this.f;
    }

    public void setAuthType(String str) {
        this.f = str;
    }
}
