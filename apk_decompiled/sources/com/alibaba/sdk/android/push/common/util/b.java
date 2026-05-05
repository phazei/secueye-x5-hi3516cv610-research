package com.alibaba.sdk.android.push.common.util;

import android.content.Context;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static long a(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(str, 0L);
    }

    public static void a(Context context, String str, long j) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(str, j).commit();
    }
}
