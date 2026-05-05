package com.alibaba.sdk.android.tool;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;

/* JADX INFO: loaded from: classes.dex */
public class ExceptionUtils {
    public static String getStackMsg(Throwable th) {
        StringBuilder sb = new StringBuilder();
        try {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace.length > 0) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    sb.append(stackTraceElement.toString());
                    sb.append(SdkConstant.CLOUDAPI_LF);
                }
            }
        } catch (Exception unused) {
            th.printStackTrace();
        }
        return sb.toString();
    }
}
