package com.alibaba.sdk.android.push.common.util.a;

import com.alibaba.sdk.android.error.ErrorCode;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f3055a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3056b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ErrorCode f3057c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public d f3058d;

    public b() {
        this.f3057c = com.alibaba.sdk.android.push.common.a.d.f3049a;
        this.f3055a = "";
        this.f3056b = 0;
        this.f3058d = d.UNKNOWN_TYPE;
    }

    public b(int i) {
        this.f3057c = com.alibaba.sdk.android.push.common.a.d.f3049a;
        for (d dVar : d.values()) {
            if (dVar.a() == i) {
                this.f3058d = dVar;
            }
        }
        this.f3055a = "";
        this.f3056b = 0;
    }
}
