package com.alibaba.sdk.android.tool;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes.dex */
public class ResourceConfigUtils {
    private static int a(Context context, String str, String str2) {
        if (context == null || TextUtils.isEmpty(str)) {
            return 0;
        }
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    public static int getColorFromRes(Context context, String str) {
        int iA = a(context, str, "color");
        if (iA == 0) {
            return -1;
        }
        try {
            return context.getResources().getColor(iA);
        } catch (Exception e) {
            b.b("Res-Config", e.getMessage());
            return -1;
        }
    }

    public static String[] getStringArrayFromRes(Context context, String str) {
        int iA = a(context, str, TmpConstant.TYPE_VALUE_ARRAY);
        if (iA == 0) {
            return null;
        }
        try {
            return context.getResources().getStringArray(iA);
        } catch (Exception e) {
            b.b("Res-Config", e.getMessage());
            return null;
        }
    }

    public static String getStringFromRes(Context context, String str) {
        int iA = a(context, str, "string");
        if (iA == 0) {
            return null;
        }
        try {
            return context.getResources().getString(iA);
        } catch (Exception e) {
            b.b("Res-Config", e.getMessage());
            return null;
        }
    }
}
