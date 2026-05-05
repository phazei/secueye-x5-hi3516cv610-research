package com.alibaba.sdk.android.utils.crashdefend;

import android.util.Log;

/* JADX INFO: compiled from: CrashDefendSDKInfo.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements Cloneable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f3220a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    public long f42a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    public String f44a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3221b;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    public long f45b;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    public String f46b;
    public int crashCount;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f3222c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public volatile boolean f3223d = false;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    public SDKMessageCallback f43a = null;

    public Object clone() {
        try {
            return (c) super.clone();
        } catch (CloneNotSupportedException e) {
            Log.e("CrashSDK", "clone fail:", e);
            return null;
        }
    }
}
