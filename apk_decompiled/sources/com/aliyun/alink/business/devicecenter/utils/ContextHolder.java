package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
public class ContextHolder {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3756a = null;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final ContextHolder f3757a = new ContextHolder();
    }

    public static ContextHolder getInstance() {
        return SingletonHolder.f3757a;
    }

    public Context getAppContext() {
        return this.f3756a;
    }

    public void init(Context context) {
        this.f3756a = context.getApplicationContext();
        if (this.f3756a == null) {
            this.f3756a = context;
        }
    }
}
