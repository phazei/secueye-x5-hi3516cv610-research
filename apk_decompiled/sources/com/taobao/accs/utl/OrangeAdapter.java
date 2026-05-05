package com.taobao.accs.utl;

import android.content.Context;
import android.content.SharedPreferences;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class OrangeAdapter {
    public static final String NAMESPACE = "accs";
    private static final String TAG = "OrangeAdapter";
    private static final String TNET_LOG_KEY = "tnet_log_off";
    public static final boolean mOrangeValid = false;

    public static String getConfig(String str, String str2, String str3) {
        return str3;
    }

    public static boolean isSmartHb() {
        boolean configFromSP;
        try {
            configFromSP = getConfigFromSP(GlobalClientInfo.getContext(), Constants.SP_KEY_HB_SMART_ENABLE, true);
        } catch (Throwable th) {
            ALog.e(TAG, "isSmartHb", th, new Object[0]);
            configFromSP = true;
        }
        ALog.d(TAG, "isSmartHb", "result", Boolean.valueOf(configFromSP));
        return configFromSP;
    }

    public static boolean isBindService(Context context) {
        boolean configFromSP;
        try {
            configFromSP = getConfigFromSP(context, Constants.SP_KEY_BIND_SERVICE_ENABLE, true);
        } catch (Throwable th) {
            ALog.e(TAG, "isBindService", th, new Object[0]);
            configFromSP = true;
        }
        ALog.d(TAG, "isBindService", "result", Boolean.valueOf(configFromSP));
        return configFromSP;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019 A[Catch: Throwable -> 0x003d, TryCatch #0 {Throwable -> 0x003d, blocks: (B:5:0x0006, B:6:0x0011, B:8:0x0019, B:9:0x0025), top: B:19:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0025 A[Catch: Throwable -> 0x003d, TRY_LEAVE, TryCatch #0 {Throwable -> 0x003d, blocks: (B:5:0x0006, B:6:0x0011, B:8:0x0019, B:9:0x0025), top: B:19:0x0006 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean isTnetLogOff(boolean r7) {
        /*
            r0 = 0
            r1 = 1
            java.lang.String r2 = "default"
            if (r7 == 0) goto L11
            java.lang.String r7 = "accs"
            java.lang.String r2 = "tnet_log_off"
            java.lang.String r3 = "default"
            java.lang.String r2 = getConfig(r7, r2, r3)     // Catch: java.lang.Throwable -> L3d
        L11:
            java.lang.String r7 = "default"
            boolean r7 = r2.equals(r7)     // Catch: java.lang.Throwable -> L3d
            if (r7 == 0) goto L25
            android.content.Context r7 = com.taobao.accs.client.GlobalClientInfo.getContext()     // Catch: java.lang.Throwable -> L3d
            java.lang.String r2 = "tnet_log_off"
            boolean r7 = getConfigFromSP(r7, r2, r1)     // Catch: java.lang.Throwable -> L3d
            goto L49
        L25:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)     // Catch: java.lang.Throwable -> L3d
            boolean r7 = r7.booleanValue()     // Catch: java.lang.Throwable -> L3d
            android.content.Context r2 = com.taobao.accs.client.GlobalClientInfo.getContext()     // Catch: java.lang.Throwable -> L38
            java.lang.String r3 = "tnet_log_off"
            saveConfigToSP(r2, r3, r7)     // Catch: java.lang.Throwable -> L38
            goto L49
        L38:
            r2 = move-exception
            r6 = r2
            r2 = r7
            r7 = r6
            goto L3f
        L3d:
            r7 = move-exception
            r2 = r1
        L3f:
            java.lang.String r3 = "OrangeAdapter"
            java.lang.String r4 = "isTnetLogOff"
            java.lang.Object[] r5 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r3, r4, r7, r5)
            r7 = r2
        L49:
            java.lang.String r2 = "OrangeAdapter"
            java.lang.String r3 = "isTnetLogOff"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.String r5 = "result"
            r4[r0] = r5
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r7)
            r4[r1] = r0
            com.taobao.accs.utl.ALog.i(r2, r3, r4)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.OrangeAdapter.isTnetLogOff(boolean):boolean");
    }

    private static boolean getConfigFromSP(Context context, String str, boolean z) {
        try {
            return context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getBoolean(str, z);
        } catch (Exception e) {
            ALog.e(TAG, "getConfigFromSP fail:", e, "key", str);
            return z;
        }
    }

    private static void saveConfigToSP(Context context, String str, boolean z) {
        try {
        } catch (Exception e) {
            ALog.e(TAG, "saveConfigToSP fail:", e, "key", str, "value", Boolean.valueOf(z));
        }
        if (context == null) {
            ALog.e(TAG, "saveTLogOffToSP context null", new Object[0]);
            return;
        }
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
        editorEdit.putBoolean(str, z);
        editorEdit.apply();
        ALog.i(TAG, "saveConfigToSP", "key", str, "value", Boolean.valueOf(z));
    }

    public static void saveConfigToSP(Context context, String str, int i) {
        try {
        } catch (Exception e) {
            ALog.e(TAG, "saveConfigToSP fail:", e, "key", str, "value", Integer.valueOf(i));
        }
        if (context == null) {
            ALog.e(TAG, "saveTLogOffToSP context null", new Object[0]);
            return;
        }
        SharedPreferences.Editor editorEdit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
        editorEdit.putInt(str, i);
        editorEdit.apply();
        ALog.i(TAG, "saveConfigToSP", "key", str, "value", Integer.valueOf(i));
    }
}
