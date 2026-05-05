package com.aliyun.alink.linksdk.tools;

import com.aliyun.alink.linksdk.tools.log.ILogDispatcher;
import com.aliyun.alink.linksdk.tools.log.ILogUpload;

/* JADX INFO: loaded from: classes2.dex */
public class IOTLog {
    public static void setLogUpload(ILogUpload iLogUpload) {
    }

    public static void d(String str, String str2) {
        ALog.d(str, str2);
    }

    public static void i(String str, String str2) {
        ALog.i(str, str2);
    }

    public static void w(String str, String str2) {
        ALog.w(str, str2);
    }

    public static void e(String str, String str2) {
        ALog.e(str, str2);
    }

    public static void e(String str, String str2, String str3) {
        ALog.e(str, str2, str3);
    }

    public static void e(String str, String str2, Exception exc) {
        ALog.e(str, str2, exc);
    }

    public static void init(ILogDispatcher iLogDispatcher, ILogUpload iLogUpload) {
        com.aliyun.iot.aep.sdk.log.ALog.setLevel((byte) 1);
        if (iLogDispatcher != null) {
            ALog.setLogDispatcher(iLogDispatcher);
        }
        ALog.setLevel((byte) 1);
    }

    public static void setLogDispatcher(ILogDispatcher iLogDispatcher) {
        ALog.setLogDispatcher(iLogDispatcher);
    }
}
