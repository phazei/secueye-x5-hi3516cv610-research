package com.alibaba.sdk.android.crashdefend;

import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class CrashDefendApi {
    public static void registerCrashDefendSdk(Context context, String str, String str2, int i, int i2, CrashDefendCallback crashDefendCallback) {
        a.a(context).a(str, str2, i, i2, crashDefendCallback);
    }
}
