package com.aliyun.alink.business.devicecenter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;

/* JADX INFO: loaded from: classes2.dex */
public class ProvisionSPUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static SharedPreferences f3774a;

    public static SharedPreferences a(Context context) {
        if (f3774a == null) {
            f3774a = context.getSharedPreferences(WifiProvisionUtConst.ARG_CONNECTION, 0);
        }
        return f3774a;
    }

    public static int getInt(Context context, String str, int i) {
        return a(context).getInt(str, i);
    }

    public static String getString(Context context, String str, String str2) {
        return a(context).getString(str, str2);
    }

    public static void putInt(Context context, String str, int i) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putInt(str, i);
        editorEdit.apply();
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }
}
