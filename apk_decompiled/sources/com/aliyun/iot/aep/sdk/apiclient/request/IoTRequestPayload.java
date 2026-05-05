package com.aliyun.iot.aep.sdk.apiclient.request;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class IoTRequestPayload {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f4595a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f4596b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, Object> f4597c = new HashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Map<String, Object> f4598d = new HashMap();

    public IoTRequestPayload(String str, String str2) {
        this.f4595a = str;
        this.f4596b = str2;
    }

    public String getId() {
        return this.f4595a;
    }

    public Map<String, Object> getParams() {
        return this.f4598d;
    }

    public Map<String, Object> getRequest() {
        return this.f4597c;
    }

    public String getVersion() {
        return this.f4596b;
    }
}
