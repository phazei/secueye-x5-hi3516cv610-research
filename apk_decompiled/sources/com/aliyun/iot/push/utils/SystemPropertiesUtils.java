package com.aliyun.iot.push.utils;

import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes2.dex */
public class SystemPropertiesUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Class<?> f4957a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Method f4958b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Method f4959c;

    private static void a() {
        try {
            if (f4957a == null) {
                f4957a = Class.forName("android.os.SystemProperties");
                f4958b = f4957a.getDeclaredMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, String.class);
                f4959c = f4957a.getDeclaredMethod("getInt", String.class, Integer.TYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String str) {
        a();
        try {
            return (String) f4958b.invoke(f4957a, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getInt(String str, int i) {
        a();
        try {
            return ((Integer) f4959c.invoke(f4957a, str, Integer.valueOf(i))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }
}
