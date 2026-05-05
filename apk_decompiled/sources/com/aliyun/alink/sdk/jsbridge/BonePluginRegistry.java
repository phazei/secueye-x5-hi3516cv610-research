package com.aliyun.alink.sdk.jsbridge;

import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class BonePluginRegistry {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static Map<String, Class<? extends IBonePlugin>> f4495a = new HashMap(10);

    public static void register(String str, Class<? extends IBonePlugin> cls) {
        if (TextUtils.isEmpty(str) || cls == null) {
            return;
        }
        f4495a.containsKey(str);
        f4495a.put(str, cls);
    }

    public static void unregister(String str) {
        if (!TextUtils.isEmpty(str) && f4495a.containsKey(str)) {
            f4495a.remove(str);
        }
    }

    public static IBonePlugin findAPlugin(String str) {
        if (f4495a.containsKey(str)) {
            Class<? extends IBonePlugin> cls = f4495a.get(str);
            try {
                return cls.newInstance();
            } catch (Exception e) {
                Log.e("APluginRegistry", "can not create instance for class:" + cls);
                e.printStackTrace();
            }
        }
        return null;
    }
}
