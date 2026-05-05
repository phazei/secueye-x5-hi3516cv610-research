package com.alibaba.sdk.android.crashdefend.a;

import com.alibaba.sdk.android.crashdefend.CrashDefendCallback;

/* JADX INFO: loaded from: classes.dex */
public class b implements Cloneable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f2863a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f2864b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f2865c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f2866d;
    public int e;
    public long f;
    public long g;
    public int h = 0;
    public long i = 0;
    public volatile boolean j = false;
    public CrashDefendCallback k = null;

    public Object clone() {
        try {
            return (b) super.clone();
        } catch (CloneNotSupportedException e) {
            com.alibaba.sdk.android.crashdefend.c.b.a("CrashSDK", "clone fail: ", e);
            return null;
        }
    }
}
