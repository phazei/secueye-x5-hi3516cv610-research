package com.aliyun.iot.aep.sdk.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class SpUtil {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static SharedPreferences f4738a;

    private static SharedPreferences a(Context context) {
        if (f4738a == null) {
            if (context == null) {
                context = AApplication.getInstance();
            }
            f4738a = context.getSharedPreferences("GlobalConfigFW", 0);
        }
        return f4738a;
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static String getString(Context context, String str) {
        return a(context).getString(str, "");
    }

    public static void putBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putBoolean(str, z);
        editorEdit.commit();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return a(context).getBoolean(str, z);
    }

    public static void putLong(Context context, String str, long j) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putLong(str, j);
        editorEdit.commit();
    }

    public static long getLong(Context context, String str, long j) {
        return a(context).getLong(str, j);
    }

    public static void putInt(Context context, String str, int i) {
        SharedPreferences.Editor editorEdit = a(context).edit();
        editorEdit.putInt(str, i);
        editorEdit.commit();
    }

    public static int getInt(Context context, String str, int i) {
        return a(context).getInt(str, i);
    }

    public static <T extends Serializable> void putObject(Context context, String str, T t) {
        try {
            a(context, str, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static <T extends Serializable> T getObject(Context context, String str) {
        try {
            return (T) a(context, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Serializable> T getObject(Context context, String str, Class<T> cls) {
        try {
            return (T) new Gson().fromJson(getString(context, str), (Class) cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void putList(Context context, String str, List<? extends Serializable> list) {
        try {
            a(context, str, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <E extends Serializable> List<E> getList(Context context, String str) {
        try {
            return (List) a(context, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <K extends String, V extends String> void putMap(Context context, String str, Map<K, V> map) {
        try {
            a(context, str, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <K extends String, V extends String> Map<K, V> getMap(Context context, String str) {
        try {
            return (Map) new Gson().fromJson(getString(context, str), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void a(Context context, String str, Object obj) {
        putString(context, str, new Gson().toJson(obj));
    }

    private static Object a(Context context, String str) throws ClassNotFoundException, IOException {
        String string = getString(context, str);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(string.getBytes(), 0));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return object;
    }

    public static void remove(Context context, String str, Object obj) {
        try {
            if (obj instanceof Boolean) {
                putBoolean(context, str, false);
            } else if ((obj instanceof Integer) || (obj instanceof Float)) {
                putInt(context, str, -1);
            } else {
                a(context, str, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove(Context context, String str) {
        SharedPreferences sharedPreferencesA = a(context);
        SharedPreferences.Editor editorEdit = sharedPreferencesA.edit();
        if (sharedPreferencesA.contains(str)) {
            editorEdit.remove(str);
        }
    }

    public static void clean(Context context) {
        a(context).edit().clear();
    }
}
