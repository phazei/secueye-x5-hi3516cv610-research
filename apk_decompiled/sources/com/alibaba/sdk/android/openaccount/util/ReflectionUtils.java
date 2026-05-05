package com.alibaba.sdk.android.openaccount.util;

import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ReflectionUtils {
    private static final Map<String, Class<?>> PRIMITIVE_CLASSES = new HashMap();
    private static final String TAG = ReflectionUtils.class.getSimpleName();

    static {
        PRIMITIVE_CLASSES.put("short", Short.TYPE);
        PRIMITIVE_CLASSES.put("int", Integer.TYPE);
        PRIMITIVE_CLASSES.put("long", Long.TYPE);
        PRIMITIVE_CLASSES.put(TmpConstant.TYPE_VALUE_DOUBLE, Double.TYPE);
        PRIMITIVE_CLASSES.put("float", Float.TYPE);
        PRIMITIVE_CLASSES.put("char", Character.TYPE);
    }

    public static Object invoke(Class<?> cls, String str, String[] strArr, Object obj, Object[] objArr) {
        Method method;
        if (strArr != null) {
            try {
                if (strArr.length == 0) {
                    method = cls.getMethod(str, new Class[0]);
                } else {
                    method = cls.getMethod(str, toClasses(strArr));
                }
            } catch (Exception e) {
                AliSDKLogger.e(TAG, "Fail to invoke the " + cls.getName() + "." + str + ", the error is " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            method = cls.getMethod(str, new Class[0]);
        }
        return method.invoke(obj, objArr);
    }

    public static Object invoke(String str, String str2, String[] strArr, Object obj, Object[] objArr) {
        try {
            return invoke(Class.forName(str), str2, strArr, obj, objArr);
        } catch (Exception e) {
            AliSDKLogger.e(TAG, "Fail to invoke the " + str + "." + str2 + ", the error is " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void set(Object obj, String str, Object obj2) {
        try {
            Field field = obj.getClass().getField(str);
            field.setAccessible(true);
            field.set(obj, obj2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    public static <T> T newInstance(Class<T> cls, Class<?>[] clsArr, Object[] objArr) {
        if (clsArr != null) {
            try {
                if (clsArr.length != 0) {
                    return cls.getConstructor(clsArr).newInstance(objArr);
                }
            } catch (Exception e) {
                AliSDKLogger.e(TAG, "Fail to create the instance of type " + cls.getName() + ", the error is " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return cls.newInstance();
    }

    public static Object newInstance(String str, String[] strArr, Object[] objArr) {
        try {
            return newInstance(Class.forName(str), toClasses(strArr), objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            AliSDKLogger.e(TAG, "Fail to create the instance of type " + str + ", the error is " + e2.getMessage());
            throw new RuntimeException(e2);
        }
    }

    public static Class<?>[] toClasses(String[] strArr) throws Exception {
        if (strArr == null) {
            return null;
        }
        Class<?>[] clsArr = new Class[strArr.length];
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            String str = strArr[i];
            if (str.length() < 7) {
                clsArr[i] = PRIMITIVE_CLASSES.get(str);
            }
            if (clsArr[i] == null) {
                clsArr[i] = Class.forName(str);
            }
        }
        return clsArr;
    }

    public static Class<?> loadClassQuietly(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static Field getField(Class<?> cls, String str) {
        if (cls == null) {
            return null;
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Exception unused) {
            return getField(cls.getSuperclass(), str);
        }
    }

    public static Object getFieldValue(Class<?> cls, String str, Object obj) {
        Field field = getField(cls, str);
        if (field == null) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to find field named " + str + " from class " + cls.getName());
            return null;
        }
        try {
            return field.get(obj);
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to get field value ", e);
            return null;
        }
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
        if (cls == null) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception unused) {
            return getMethod(cls.getSuperclass(), str, new Class[0]);
        }
    }
}
