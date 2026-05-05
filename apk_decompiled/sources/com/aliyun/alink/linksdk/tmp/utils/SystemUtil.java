package com.aliyun.alink.linksdk.tmp.utils;

import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public class SystemUtil {
    private static final String TAG = "SystemUtil";
    private static Class<?> mClassType;
    private static Method mGetIntMethod;
    private static Method mGetMethod;

    private static void init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");
                mGetMethod = mClassType.getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, String.class);
                mGetIntMethod = mClassType.getDeclaredMethod("getInt", String.class, Integer.TYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static String get(String str) {
        String str2;
        init();
        try {
            str2 = (String) mGetMethod.invoke(mClassType, str);
        } catch (Exception e) {
            e = e;
            str2 = null;
        }
        try {
            LogCat.d(TAG, "get key:" + str + " value:" + str2);
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
        }
        return str2;
    }

    public static int getInt(String str, int i) {
        init();
        try {
            Integer num = (Integer) mGetIntMethod.invoke(mClassType, str, Integer.valueOf(i));
            i = num.intValue();
            LogCat.d(TAG, "getInt key:" + str + " value:" + num);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}
