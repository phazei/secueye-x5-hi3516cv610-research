package tools;

import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.log.IALogCloud;

/* JADX INFO: loaded from: classes4.dex */
public class LogEx {
    private static boolean ALogCloudOpen = false;

    public static void setALogCloud(IALogCloud iALogCloud) {
        ALog.setALogCloud(iALogCloud);
        if (iALogCloud != null) {
            ALogCloudOpen = true;
        }
    }

    public static void v(boolean z, String str, String str2) {
        if (!z && ALogCloudOpen) {
            ALog.v(str, str2);
        }
    }

    public static void d(boolean z, String str, String str2) {
        if (!z && ALogCloudOpen) {
            ALog.d(str, str2);
        }
    }

    public static void e(boolean z, String str, String str2) {
        if (!z && ALogCloudOpen) {
            ALog.e(str, str2);
        }
    }

    public static void e(boolean z, String str, String str2, Exception exc) {
        if (!z && ALogCloudOpen) {
            ALog.e(str, str2, exc);
        }
    }

    public static void i(boolean z, String str, String str2) {
        if (!z && ALogCloudOpen) {
            ALog.i(str, str2);
        }
    }

    public static void w(boolean z, String str, String str2) {
        if (!z && ALogCloudOpen) {
            ALog.w(str, str2);
        }
    }
}
