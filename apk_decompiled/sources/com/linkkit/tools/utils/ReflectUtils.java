package com.linkkit.tools.utils;

import com.linkkit.tools.a;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes3.dex */
public class ReflectUtils {
    private static final String TAG = "ReflectUtils";

    public static void setFieldValue(Class<?> cls, String str, String str2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(cls, str2);
        } catch (Exception e) {
            a.a(TAG, "setFieldValue key=" + str + ", value=" + str2 + ",e=" + e);
        }
    }

    public static boolean hasClass(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            a.c(TAG, "hasClss=" + e);
            return false;
        } catch (Exception e2) {
            a.c(TAG, "hasClssEx=" + e2);
            return false;
        }
    }

    public static Object invokeStaticMethod(String str, String str2) {
        try {
            return Class.forName(str).getMethod(str2, new Class[0]).invoke(null, new Object[0]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return null;
        } catch (Exception e5) {
            e5.printStackTrace();
            return null;
        }
    }
}
