package com.aliyun.alink.business.devicecenter.utils;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public class SystemPropertiesUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static Class<?> f3775a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static Method f3776b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static Method f3777c;

    public static void a() {
        try {
            if (f3775a == null) {
                f3775a = Class.forName("android.os.SystemProperties");
                f3776b = f3775a.getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, String.class);
                f3777c = f3775a.getDeclaredMethod("getInt", String.class, Integer.TYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String str) {
        a();
        try {
            return (String) f3776b.invoke(f3775a, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getInt(String str, int i) {
        a();
        try {
            return ((Integer) f3777c.invoke(f3775a, str, Integer.valueOf(i))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}
