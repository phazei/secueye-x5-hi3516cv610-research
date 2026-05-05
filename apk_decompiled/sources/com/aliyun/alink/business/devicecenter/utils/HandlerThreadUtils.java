package com.aliyun.alink.business.devicecenter.utils;

import android.os.HandlerThread;
import android.os.Looper;

/* JADX INFO: loaded from: classes2.dex */
public class HandlerThreadUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f3761a = "defaultName";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public HandlerThread f3762b;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final HandlerThreadUtils f3763a = new HandlerThreadUtils();
    }

    public static HandlerThreadUtils getInstance() {
        return SingletonHolder.f3763a;
    }

    public Looper getLooper() {
        if (this.f3762b == null) {
            this.f3762b = new HandlerThread(f3761a);
        }
        Looper looper = this.f3762b.getLooper();
        if (looper != null) {
            return looper;
        }
        this.f3762b.start();
        return this.f3762b.getLooper();
    }

    public HandlerThreadUtils() {
        this.f3762b = new HandlerThread(f3761a);
        this.f3762b.start();
    }
}
