package com.alibaba.ailabs.tg.utils;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/* JADX INFO: loaded from: classes.dex */
public class ObjectOutputFormatUtils {
    private static final String LOG_PREFIX = "        ";

    public static String getString(Object obj, Class<?> cls) {
        String str = "";
        if (obj == null || cls == null) {
            return "";
        }
        try {
            str = cls.getSimpleName() + ":\n";
            for (Field field : cls.getDeclaredFields()) {
                if (field != null) {
                    makeAccessible(field);
                    str = str + LOG_PREFIX + field.getName() + " = " + field.get(obj) + SdkConstant.CLOUDAPI_LF;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private static void makeAccessible(Field field) {
        if (Modifier.isPublic(field.getModifiers()) && Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            return;
        }
        field.setAccessible(true);
    }
}
