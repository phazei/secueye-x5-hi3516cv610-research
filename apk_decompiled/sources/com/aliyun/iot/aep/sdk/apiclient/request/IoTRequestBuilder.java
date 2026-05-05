package com.aliyun.iot.aep.sdk.apiclient.request;

import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes2.dex */
public class IoTRequestBuilder {
    public static Scheme defaultScheme = Scheme.HTTPS;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Scheme f4587a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f4589c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f4590d;
    public String e;
    public String f;
    public String g;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Method f4588b = Method.POST;
    public Map<String, Object> h = new HashMap();

    public static class b implements IoTRequest {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public Scheme f4591a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public Method f4592b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f4593c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public String f4594d;
        public String e;
        public String f;
        public String g;
        public String h;
        public Map<String, Object> i;

        public b() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getAPIVersion() {
            return this.e;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getAuthType() {
            return this.f;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getHost() {
            return this.f4593c;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getId() {
            return this.h;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public Method getMethod() {
            return this.f4592b;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getMockType() {
            return this.g;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public Map<String, Object> getParams() {
            return this.i;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public String getPath() {
            return this.f4594d;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest
        public Scheme getScheme() {
            return this.f4591a;
        }
    }

    public IoTRequestBuilder() {
        this.f4587a = Scheme.HTTPS;
        this.f4587a = defaultScheme;
    }

    public static boolean a(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (!a(it.next().getValue())) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public IoTRequestBuilder addParam(String str, Object obj) {
        this.h.put(str, obj);
        return this;
    }

    public IoTRequest build() {
        b bVar = new b();
        bVar.f4591a = this.f4587a;
        bVar.f4592b = this.f4588b;
        bVar.f4593c = this.f4589c;
        if (TextUtils.isEmpty(this.f4590d)) {
            throw new IllegalArgumentException("path can not be empty");
        }
        if (!this.f4590d.startsWith("/")) {
            this.f4590d = "/" + this.f4590d;
        }
        bVar.f4594d = this.f4590d;
        if (TextUtils.isEmpty(this.e)) {
            throw new IllegalArgumentException("apiVersion can not be empty");
        }
        bVar.e = this.e;
        bVar.h = UUID.randomUUID().toString();
        bVar.f = this.f;
        bVar.g = this.g;
        if (this.h == null) {
            this.h = new HashMap();
        }
        bVar.i = this.h;
        if (a(this.h)) {
            return bVar;
        }
        throw new IllegalArgumentException("params contains illegal value");
    }

    public IoTRequestBuilder setApiVersion(String str) {
        this.e = str;
        return this;
    }

    public IoTRequestBuilder setAuthType(String str) {
        this.f = str;
        return this;
    }

    public IoTRequestBuilder setHost(String str) {
        this.f4589c = str;
        return this;
    }

    public IoTRequestBuilder setMockType(String str) {
        this.g = str;
        return this;
    }

    public IoTRequestBuilder setParams(Map<String, Object> map) {
        this.h = map;
        return this;
    }

    public IoTRequestBuilder setPath(String str) {
        this.f4590d = str;
        return this;
    }

    public IoTRequestBuilder setScheme(Scheme scheme) {
        this.f4587a = scheme;
        return this;
    }

    public IoTRequestBuilder addParam(String str, String str2) {
        this.h.put(str, str2);
        return this;
    }

    public static boolean a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (!a(it.next())) {
                return false;
            }
        }
        return true;
    }

    public IoTRequestBuilder addParam(String str, int i) {
        this.h.put(str, Integer.valueOf(i));
        return this;
    }

    public IoTRequestBuilder addParam(String str, long j) {
        this.h.put(str, Long.valueOf(j));
        return this;
    }

    public static boolean a(Object obj) {
        if (obj == null || (obj instanceof Boolean) || (obj instanceof String) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof BigDecimal)) {
            return true;
        }
        if (obj instanceof List) {
            return a((List) obj);
        }
        if (obj instanceof Map) {
            return a((Map<String, Object>) obj);
        }
        return false;
    }

    public IoTRequestBuilder addParam(String str, float f) {
        this.h.put(str, Float.valueOf(f));
        return this;
    }

    public IoTRequestBuilder addParam(String str, double d2) {
        this.h.put(str, Double.valueOf(d2));
        return this;
    }

    public IoTRequestBuilder addParam(String str, List list) {
        this.h.put(str, list);
        return this;
    }

    public IoTRequestBuilder addParam(String str, Map map) {
        this.h.put(str, map);
        return this;
    }
}
