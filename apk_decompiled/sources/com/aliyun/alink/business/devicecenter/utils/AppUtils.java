package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;

/* JADX INFO: loaded from: classes2.dex */
public class AppUtils {
    public static Context getContext() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
            return (Context) objInvoke.getClass().getMethod("getApplication", new Class[0]).invoke(objInvoke, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
