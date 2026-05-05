package com.linkkit.tools.utils;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes3.dex */
public class SystemPropertiesUtils {
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
        }
    }

    public static String get(String str) {
        init();
        try {
            return (String) mGetMethod.invoke(mClassType, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getInt(String str, int i) {
        init();
        try {
            return ((Integer) mGetIntMethod.invoke(mClassType, str, Integer.valueOf(i))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}
