package com.alibaba.sdk.android.sender;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SdkInfo {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<String, String> f3179a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f3180b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f3181c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f3182d;

    String a() {
        return this.f3180b;
    }

    String b() {
        return this.f3181c;
    }

    String c() {
        return this.f3182d;
    }

    public SdkInfo setAppKey(String str) {
        this.f3182d = str;
        return this;
    }

    public SdkInfo setExt(Map<String, String> map) {
        this.f3179a = map;
        return this;
    }

    public SdkInfo setSdkId(String str) {
        this.f3180b = str;
        return this;
    }

    public SdkInfo setSdkVersion(String str) {
        this.f3181c = str;
        return this;
    }
}
