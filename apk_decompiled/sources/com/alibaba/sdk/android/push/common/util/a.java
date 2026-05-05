package com.alibaba.sdk.android.push.common.util;

import android.content.Context;
import com.alibaba.sdk.android.tool.NetworkUtils;

/* JADX INFO: loaded from: classes.dex */
public class a {
    public static boolean a(Context context) {
        try {
            return NetworkUtils.isNetworkConnected(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
