package com.alibaba.sdk.android.push.register;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.push.register.ReporterFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class ThirdPushManager {
    private static final String TAG = ThirdPushManager.class.getCanonicalName();

    public static void registerImpl(BaseNotifyClickActivity.INotifyListener iNotifyListener) {
        if (iNotifyListener != null) {
            BaseNotifyClickActivity.addNotifyListener(iNotifyListener);
        } else {
            ALog.e(TAG, "BaseNotifyClickActivity.INotifyListener cannot be empty.", new Object[0]);
        }
    }

    public static void reportToken(Context context, String str, String str2, String str3) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            ReporterFactory.ITokenReporter tokenReporter = ReporterFactory.getTokenReporter();
            String strAddPrefix = ReporterFactory.addPrefix(str, str3);
            if (tokenReporter instanceof ReporterFactory.ITokenReporterV2) {
                try {
                    ALog.i(TAG, "report sdkVer:-SNAPSHOT, source: " + str + ", ThirdToken: " + str2 + ", version: " + strAddPrefix, new Object[0]);
                    ((ReporterFactory.ITokenReporterV2) tokenReporter).reportToken(context, "-SNAPSHOT", str, strAddPrefix, str2);
                    return;
                } catch (Throwable th) {
                    ALog.e(TAG, "reportToken", th, new Object[0]);
                    return;
                }
            }
            try {
                ALog.i(TAG, "report " + str + " ThirdToken: " + str2 + ", version: " + strAddPrefix, new Object[0]);
                tokenReporter.reportToken(context, str, strAddPrefix, str2);
                return;
            } catch (Throwable th2) {
                ALog.e(TAG, "reportToken", th2, new Object[0]);
                return;
            }
        }
        ALog.e(TAG, "Incorrect parameter", new Object[0]);
    }

    public static void onPushMsg(Context context, String str, String str2) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                ReporterFactory.getMsgReporter().sendMsgToDecrypt(context, str, str2.getBytes("UTF-8"), null);
                return;
            } catch (Throwable th) {
                ALog.e(TAG, "onPushMsg", th, new Object[0]);
                return;
            }
        }
        ALog.e(TAG, "Incorrect parameter", new Object[0]);
    }

    public enum ThirdPushReportKeyword {
        HONOR("HONOR_TOKEN", AgooConstants.MESSAGE_SYSTEM_SOURCE_HONOR, "ho_"),
        HUAWEI("HW_TOKEN", AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI, "h_"),
        XIAOMI("MI_TOKEN", "xiaomi", "mi_"),
        OPPO("OPPO_TOKEN", AgooConstants.MESSAGE_SYSTEM_SOURCE_OPPO, "o_"),
        VIVO("VIVO_TOKEN", "vivo", "v_"),
        MEIZU("MZ_TOKEN", AgooConstants.MESSAGE_SYSTEM_SOURCE_MEIZU, "mz_"),
        FCM("gcm", "gcm", "g_");

        public final String thirdMsgKeyword;
        public final String thirdSdkVersionPrefix;
        public final String thirdTokenKeyword;

        ThirdPushReportKeyword(String str, String str2, String str3) {
            this.thirdTokenKeyword = str;
            this.thirdMsgKeyword = str2;
            this.thirdSdkVersionPrefix = str3;
        }
    }
}
