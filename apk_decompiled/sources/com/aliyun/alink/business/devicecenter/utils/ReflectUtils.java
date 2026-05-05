package com.aliyun.alink.business.devicecenter.utils;

import android.app.Application;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes2.dex */
public class ReflectUtils {
    public static Class<?>[] a(Object... objArr) {
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            if (obj == null) {
                throw new IllegalArgumentException("objects has null value.");
            }
            clsArr[i] = obj.getClass();
        }
        return clsArr;
    }

    public static Object callStaticMethod(String str, String str2, Object... objArr) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException {
        return Class.forName(str).getMethod(str2, a(objArr)).invoke(null, objArr);
    }

    public static Object getIdentifyId() {
        ALog.d("ReflectUtils", "getIdentifyId() called");
        try {
            Object objInvoke = Class.forName("com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl").getDeclaredMethod("getInstance", Application.class).invoke(null, ContextHolder.getInstance().getAppContext());
            return objInvoke.getClass().getDeclaredMethod("getIoTIdentity", null).invoke(objInvoke, null);
        } catch (ClassNotFoundException e) {
            ALog.d("ReflectUtils", "ClassNotFoundException e=" + e);
            return null;
        } catch (IllegalAccessException e2) {
            ALog.d("ReflectUtils", "IllegalAccessException e=" + e2);
            return null;
        } catch (NoSuchMethodException e3) {
            ALog.d("ReflectUtils", "NoSuchMethodException e=" + e3);
            return null;
        } catch (InvocationTargetException e4) {
            ALog.d("ReflectUtils", "InvocationTargetException e=" + e4);
            return null;
        } catch (Exception e5) {
            ALog.d("ReflectUtils", "Exception e=" + e5);
            return null;
        }
    }

    public static boolean hasClass(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            ALog.e("ReflectUtils", "hasClss=" + e);
            return false;
        } catch (Exception e2) {
            ALog.e("ReflectUtils", "hasClssEx=" + e2);
            return false;
        } catch (NoClassDefFoundError e3) {
            ALog.e("ReflectUtils", "hasClssNCDFE=" + e3);
            return false;
        } catch (Throwable th) {
            ALog.e("ReflectUtils", "hasClssT=" + th);
            return false;
        }
    }

    public static Object newInstance(Class<?> cls, Object... objArr) {
        Class<?>[] clsArrA = a(objArr);
        try {
            Constructor<?> constructorA = a(cls, clsArrA);
            if (constructorA != null) {
                return constructorA.newInstance(objArr);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("newInstance not found match construct, className: ");
            sb.append(cls.getName());
            ALog.w("ReflectUtils", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            for (Class<?> cls2 : clsArrA) {
                sb2.append(cls2.getName());
                sb2.append("'");
            }
            sb2.deleteCharAt(sb2.length() - 1);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("args: ");
            sb3.append(sb2.toString());
            ALog.w("ReflectUtils", sb3.toString());
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            ALog.w("ReflectUtils", "newInstance IllegalAccessException=" + e);
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            ALog.w("ReflectUtils", "newInstance InstantiationException=" + e2);
            return null;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            ALog.w("ReflectUtils", "newInstance InvocationTargetException=" + e3);
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            ALog.w("ReflectUtils", "newInstance exception=" + th);
            return null;
        }
    }

    public static void setFieldValue(Class<?> cls, String str, String str2) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(cls, str2);
        } catch (Exception e) {
            ALog.d("ReflectUtils", "setFieldValue key=" + str + ", value=" + str2 + ",e=" + e);
        }
    }

    public static Constructor<?> a(Class<?> cls, Class<?>[] clsArr) {
        Constructor<?>[] constructors = cls.getConstructors();
        int length = constructors.length;
        int i = 0;
        Constructor<?> constructor = null;
        while (true) {
            if (i >= length) {
                break;
            }
            Constructor<?> constructor2 = constructors[i];
            Class<?>[] parameterTypes = constructor2.getParameterTypes();
            if (parameterTypes.length == clsArr.length) {
                int i2 = 0;
                while (true) {
                    if (i2 >= clsArr.length) {
                        constructor = constructor2;
                        break;
                    }
                    if (!parameterTypes[i2].isAssignableFrom(clsArr[i2])) {
                        constructor = null;
                        break;
                    }
                    i2++;
                }
                if (constructor != null) {
                    ALog.i("ReflectUtils", "found constructor for classType: " + cls.getName());
                    break;
                }
            }
            i++;
        }
        return constructor;
    }
}
