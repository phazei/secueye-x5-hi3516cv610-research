package com.aliyun.iot.aep.sdk.credential.utils;

import com.aliyun.alink.linksdk.tools.ALog;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class ReflectUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AtomicBoolean f4663a = new AtomicBoolean(false);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static AtomicBoolean f4664b = new AtomicBoolean(false);

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
        }
    }

    public static boolean hasOADep() {
        if (f4663a.compareAndSet(false, true)) {
            f4664b.set(hasClass("com.aliyun.iot.aep.sdk.login.LoginBusiness"));
        }
        return f4664b.get();
    }
}
