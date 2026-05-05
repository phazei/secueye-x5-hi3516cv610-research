package com.aliyun.iot.push.impl;

import android.app.Application;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.aliyun.iot.push.utils.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class FCMRegister {
    public static void register(Application application, String str, String str2, String str3, String str4) {
        ALog.i("FCMRegister", "register FCM push channel.");
        try {
            GcmRegister.register(application, str, str2, str3, str4);
        } catch (Throwable th) {
            ALog.e("FCMRegister", "register " + th);
        }
    }

    public static void unregister() {
        GcmRegister.unregister();
    }
}
