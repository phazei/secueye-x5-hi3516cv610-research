package com.taobao.accs.utl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.accs.common.Constants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class Utils {
    public static final String SP_AGOO_BIND_FILE_NAME = "EMAS_AGOO_BIND";
    private static final String TAG = "Utils";
    private static final byte[] mLock = new byte[0];
    private static int targetSdkVersion = -1;
    private static int debugMode = 0;

    public static boolean isTarget26(Context context) {
        if (context == null) {
            return false;
        }
        if (targetSdkVersion == -1) {
            targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        }
        return targetSdkVersion >= 26 && Build.VERSION.SDK_INT >= 26;
    }

    public static void setMode(Context context, int i) {
        debugMode = i;
    }

    public static int getMode(Context context) {
        return debugMode;
    }

    public static void clearAllSharePreferences(Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                editorEdit.clear();
                editorEdit.apply();
            }
        } catch (Throwable th) {
            ALog.e(TAG, "clearAllSharePreferences", th, new Object[0]);
        }
    }

    public static void killService(Context context) {
        try {
            Class<?> clsLoadClass = com.taobao.accs.b.a.a().b().loadClass("com.taobao.accs.utl.UtilityImpl");
            clsLoadClass.getMethod("killService", Context.class).invoke(clsLoadClass, context);
        } catch (Throwable th) {
            ALog.e(TAG, "killService", th, new Object[0]);
        }
    }

    public static void setAgooAppkey(Context context, String str) {
        try {
            Class<?> clsLoadClass = com.taobao.accs.b.a.a().b().loadClass("org.android.agoo.common.Config");
            clsLoadClass.getMethod("setAgooAppKey", Context.class, String.class).invoke(clsLoadClass, context, str);
        } catch (Throwable th) {
            ALog.e(TAG, "setAgooAppkey", th, new Object[0]);
            th.printStackTrace();
        }
    }

    public static void setSpValue(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            ALog.e(TAG, "setSpValue null", new Object[0]);
            return;
        }
        try {
            synchronized (mLock) {
                SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                editorEdit.putString(str, str2);
                editorEdit.apply();
            }
            ALog.i(TAG, "setSpValue", "key", str, "value", str2);
        } catch (Exception e) {
            ALog.e(TAG, "setSpValue fail", e, new Object[0]);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:15:0x0038
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public static java.lang.String getSpValue(android.content.Context r4, java.lang.String r5, java.lang.String r6) {
        /*
            r0 = 0
            r1 = 0
            byte[] r2 = com.taobao.accs.utl.Utils.mLock     // Catch: java.lang.Throwable -> L3b
            monitor-enter(r2)     // Catch: java.lang.Throwable -> L3b
            java.lang.String r3 = "EMAS_ACCS_SDK"
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r3, r1)     // Catch: java.lang.Throwable -> L38
            java.lang.String r4 = r4.getString(r5, r0)     // Catch: java.lang.Throwable -> L38
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L35
            java.lang.String r5 = "Utils"
            java.lang.String r0 = "getSpValue"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L33
            java.lang.String r3 = "value"
            r2[r1] = r3     // Catch: java.lang.Throwable -> L33
            r3 = 1
            r2[r3] = r4     // Catch: java.lang.Throwable -> L33
            com.taobao.accs.utl.ALog.i(r5, r0, r2)     // Catch: java.lang.Throwable -> L33
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Throwable -> L33
            if (r5 == 0) goto L46
            java.lang.String r5 = "Utils"
            java.lang.String r0 = "getSpValue use default!"
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L33
            com.taobao.accs.utl.ALog.e(r5, r0, r2)     // Catch: java.lang.Throwable -> L33
            r4 = r6
            goto L46
        L33:
            r5 = move-exception
            goto L3d
        L35:
            r5 = move-exception
            r0 = r4
            goto L39
        L38:
            r5 = move-exception
        L39:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L38
            throw r5     // Catch: java.lang.Throwable -> L3b
        L3b:
            r5 = move-exception
            r4 = r0
        L3d:
            java.lang.String r6 = "Utils"
            java.lang.String r0 = "getSpValue fail"
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.taobao.accs.utl.ALog.e(r6, r0, r5, r1)
        L46:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.Utils.getSpValue(android.content.Context, java.lang.String, java.lang.String):java.lang.String");
    }

    public static void clearAgooBindCache(Context context) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences("EMAS_AGOO_BIND", 0).edit();
            editorEdit.clear();
            editorEdit.apply();
        } catch (Exception e) {
            ALog.e(TAG, "clearAgooBindCache", e, new Object[0]);
        }
    }

    public static Bundle getMetaInfo(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null) {
                return applicationInfo.metaData;
            }
            return null;
        } catch (Throwable th) {
            ALog.e(TAG, "getMetaInfo", th, new Object[0]);
            return null;
        }
    }

    public static boolean isIPV6Address(String str) {
        int i;
        boolean z;
        int i2;
        int i3;
        boolean z2;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            return false;
        }
        if (charArray[0] != ':') {
            i = 0;
            z = false;
            i2 = 0;
            i3 = 0;
            z2 = true;
        } else {
            if (charArray[1] != ':') {
                return false;
            }
            z = false;
            i3 = 0;
            i = 1;
            i2 = 1;
            z2 = true;
        }
        while (i < charArray.length) {
            char c2 = charArray[i];
            int iDigit = Character.digit(c2, 16);
            if (iDigit != -1) {
                i3 = (i3 << 4) + iDigit;
                if (i3 > 65535) {
                    return false;
                }
                z2 = false;
            } else {
                if (c2 != ':' || (i2 = i2 + 1) > 7) {
                    return false;
                }
                if (!z2) {
                    i3 = 0;
                    z2 = true;
                } else {
                    if (z) {
                        return false;
                    }
                    z = true;
                }
            }
            i++;
        }
        return z || i2 >= 7;
    }
}
