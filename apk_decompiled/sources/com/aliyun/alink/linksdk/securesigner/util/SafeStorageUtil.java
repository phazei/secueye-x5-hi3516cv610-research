package com.aliyun.alink.linksdk.securesigner.util;

import android.content.Context;
import android.provider.Settings;

/* JADX INFO: loaded from: classes2.dex */
public class SafeStorageUtil {
    private static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getDeviceId(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return string == null ? "" : string;
    }
}
