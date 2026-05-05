package com.aliyun.alink.linksdk.channel.core.utils;

/* JADX INFO: compiled from: ReflectUtils.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {
    public static boolean a(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            a.b("ReflectUtils", "hasClss=" + e);
            return false;
        } catch (Exception e2) {
            a.b("ReflectUtils", "hasClssEx=" + e2);
            return false;
        }
    }
}
