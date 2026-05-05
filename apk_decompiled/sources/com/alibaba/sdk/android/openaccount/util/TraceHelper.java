package com.alibaba.sdk.android.openaccount.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;

/* JADX INFO: loaded from: classes.dex */
public final class TraceHelper {
    private static final String DEFAULT_CHANNEL = "0";
    private static final String TAG = "TraceHelper";
    public static int ttidVersion = 2;
    public static String clientTTID = null;
    public static String webTTID = clientTTID;
    public static String channel = "0";

    /* JADX WARN: Removed duplicated region for block: B:10:0x0015 A[Catch: all -> 0x006a, RuntimeException -> 0x006c, TryCatch #1 {RuntimeException -> 0x006c, blocks: (B:5:0x0005, B:8:0x000f, B:10:0x0015, B:11:0x0031, B:13:0x0037, B:14:0x003b, B:18:0x0063, B:16:0x0041, B:17:0x0056, B:19:0x0065, B:7:0x000b), top: B:30:0x0005, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0037 A[Catch: all -> 0x006a, RuntimeException -> 0x006c, TryCatch #1 {RuntimeException -> 0x006c, blocks: (B:5:0x0005, B:8:0x000f, B:10:0x0015, B:11:0x0031, B:13:0x0037, B:14:0x003b, B:18:0x0063, B:16:0x0041, B:17:0x0056, B:19:0x0065, B:7:0x000b), top: B:30:0x0005, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized void init(android.content.Context r5, java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            java.lang.Class<com.alibaba.sdk.android.openaccount.util.TraceHelper> r0 = com.alibaba.sdk.android.openaccount.util.TraceHelper.class
            monitor-enter(r0)
            if (r7 == 0) goto Lb
            int r1 = r7.length()     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            if (r1 != 0) goto Lf
        Lb:
            java.lang.String r7 = getChannel(r5)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
        Lf:
            boolean r5 = checkChannel(r7)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            if (r5 != 0) goto L31
            java.lang.String r5 = "oa"
            java.lang.String r1 = "kernel"
            java.lang.String r2 = "initChannel"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r3.<init>()     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r4 = "Channel chars must in [0-9][a-z][A-Z], now : "
            r3.append(r4)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r3.append(r7)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r7 = r3.toString()     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.e(r5, r1, r2, r7)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r7 = "0"
        L31:
            com.alibaba.sdk.android.openaccount.util.TraceHelper.channel = r7     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r5 = com.alibaba.sdk.android.openaccount.util.TraceHelper.clientTTID     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            if (r5 != 0) goto L65
            int r5 = com.alibaba.sdk.android.openaccount.util.TraceHelper.ttidVersion     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r7 = 0
            r1 = 1
            switch(r5) {
                case 1: goto L56;
                case 2: goto L41;
                default: goto L3e;
            }     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
        L3e:
            java.lang.String r5 = "2014@taobao_h5_3.0.0"
            goto L63
        L41:
            java.lang.String r5 = "2014_%s_%s@openaccount_android_%s"
            r2 = 3
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r3 = com.alibaba.sdk.android.openaccount.util.TraceHelper.channel     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r2[r7] = r3     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r2[r1] = r6     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r6 = 2
            r2[r6] = r8     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r5 = java.lang.String.format(r5, r2)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            com.alibaba.sdk.android.openaccount.util.TraceHelper.clientTTID = r5     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            goto L65
        L56:
            java.lang.String r5 = "2014@taobao_h5_3.0.0$%s"
            java.lang.Object[] r8 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            r8[r7] = r6     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            java.lang.String r5 = java.lang.String.format(r5, r8)     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            com.alibaba.sdk.android.openaccount.util.TraceHelper.clientTTID = r5     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            goto L65
        L63:
            com.alibaba.sdk.android.openaccount.util.TraceHelper.clientTTID = r5     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
        L65:
            java.lang.String r5 = com.alibaba.sdk.android.openaccount.util.TraceHelper.clientTTID     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            com.alibaba.sdk.android.openaccount.util.TraceHelper.webTTID = r5     // Catch: java.lang.Throwable -> L6a java.lang.RuntimeException -> L6c
            goto L87
        L6a:
            r5 = move-exception
            goto L89
        L6c:
            r5 = move-exception
            java.lang.String r6 = com.alibaba.sdk.android.openaccount.util.TraceHelper.TAG     // Catch: java.lang.Throwable -> L6a
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6a
            r7.<init>()     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = "init trace info error: "
            r7.append(r8)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r8 = r5.getMessage()     // Catch: java.lang.Throwable -> L6a
            r7.append(r8)     // Catch: java.lang.Throwable -> L6a
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L6a
            com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.e(r6, r7, r5)     // Catch: java.lang.Throwable -> L6a
        L87:
            monitor-exit(r0)
            return
        L89:
            monitor-exit(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.openaccount.util.TraceHelper.init(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private static boolean checkChannel(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if ((cCharAt < '0' || cCharAt > '9') && ((cCharAt < 'a' || cCharAt > 'z') && (cCharAt < 'A' || cCharAt > 'Z'))) {
                return false;
            }
        }
        return true;
    }

    private static String getChannel(Context context) {
        try {
            String property = OpenAccountSDK.getProperty("channel");
            if (property != null && property.length() > 0) {
                String property2 = OpenAccountSDK.getProperty("channelType");
                if (property2 == null || property2.length() <= 0) {
                    return property;
                }
                if (property2.equals("umeng")) {
                    return "u" + property;
                }
                if (property2.equals("baidu")) {
                    return "b" + property;
                }
                return "0" + property;
            }
            String metaConfig = getMetaConfig(context, OpenAccountConstants.CHANNEL_META_CONFIG_KEY_ALISDK);
            if (metaConfig != null && metaConfig.length() > 0) {
                return "0" + metaConfig;
            }
            String metaConfig2 = getMetaConfig(context, OpenAccountConstants.CHANNEL_META_CONFIG_KEY_UMENG);
            if (metaConfig2 == null || metaConfig2.length() <= 0) {
                return "0";
            }
            return "u" + metaConfig2;
        } catch (RuntimeException e) {
            AliSDKLogger.e(TAG, "getChannel error: " + e.getMessage(), e);
            return "0";
        }
    }

    private static String getMetaConfig(Context context, String str) {
        Object obj;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 16512).applicationInfo;
            return (applicationInfo.metaData == null || (obj = applicationInfo.metaData.get(str)) == null) ? "" : obj.toString();
        } catch (PackageManager.NameNotFoundException unused) {
            AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "Meta config not found: " + str);
            return "";
        }
    }
}
